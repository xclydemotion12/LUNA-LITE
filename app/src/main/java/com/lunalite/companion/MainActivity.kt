package com.lunalite.companion

import android.app.Activity
import android.os.Bundle
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.graphics.Color
import android.view.Gravity
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast

class MainActivity : Activity() {
    private val bg = Color.rgb(9, 7, 13)
    private val panel = Color.rgb(25, 19, 34)
    private val purple = Color.rgb(181, 138, 255)
    private val white = Color.rgb(245, 240, 250)
    private val muted = Color.rgb(174, 160, 190)
    private lateinit var presetInfo: TextView
    private val prefs by lazy {
        getSharedPreferences("luna_settings", Context.MODE_PRIVATE)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = bg
        window.navigationBarColor = bg
        buildUi()
        showPreset(prefs.getString("preset", "BALANCED") ?: "BALANCED")
    }

    private fun buildUi() {
        val scroll = ScrollView(this).apply {
            setBackgroundColor(bg)
            isFillViewport = true
        }

        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(dp(20), dp(24), dp(20), dp(28))
        }

        val brand = TextView(this).apply {
            text = "LUNA LITE"
            textSize = 30f
            setTextColor(purple)
            gravity = Gravity.CENTER
            letterSpacing = 0.12f
            typeface = android.graphics.Typeface.create(
                "sans-serif",
                android.graphics.Typeface.BOLD
            )
        }
        root.addView(brand, matchWrap())

        val subtitle = TextView(this).apply {
            text = "GRAPHICS COMPANION  •  ANDROID 10+"
            textSize = 11f
            setTextColor(muted)
            gravity = Gravity.CENTER
            setPadding(0, dp(5), 0, dp(22))
        }
        root.addView(subtitle, matchWrap())

        root.addView(section("PERFORMANCE PRESET"))

        presetInfo = TextView(this).apply {
            textSize = 14f
            setTextColor(white)
            setPadding(dp(14), dp(14), dp(14), dp(14))
            setBackgroundColor(panel)
        }
        root.addView(
            presetInfo,
            matchWidthWrap().apply { bottomMargin = dp(10) }
        )

        root.addView(actionButton("LOW  •  Minimal visual effects") {
            showPreset("LOW")
        })
        root.addView(actionButton("BALANCED  •  Default visuals") {
            showPreset("BALANCED")
        })
        root.addView(actionButton("RESTORE DEFAULT") {
            showPreset("DEFAULT")
        })

        root.addView(section("DEVICE INFORMATION"))

        val status = TextView(this).apply {
            textSize = 13f
            setTextColor(muted)
            setPadding(dp(14), dp(14), dp(14), dp(14))
            setBackgroundColor(panel)
            text = deviceSummary()
        }
        root.addView(
            status,
            matchWidthWrap().apply { bottomMargin = dp(12) }
        )

        root.addView(section("ROBLOX STUDIO CONFIG"))

        val explanation = TextView(this).apply {
            text = "Salin contoh konfigurasi Lua untuk game Roblox milikmu sendiri. Aplikasi ini tidak mengubah game Roblox lain atau mengatur FPS Roblox secara paksa."
            textSize = 13f
            setTextColor(muted)
            setPadding(0, 0, 0, dp(12))
        }
        root.addView(explanation, matchWrap())

        root.addView(actionButton("COPY LOW PRESET LUA") {
            copyLua()
        })

        val footer = TextView(this).apply {
            text = "LUNA LITE 1.0  •  Independent companion app\n60 FPS adalah target, bukan jaminan. FPS aktual bergantung pada perangkat dan game."
            textSize = 11f
            setTextColor(muted)
            gravity = Gravity.CENTER
            setPadding(0, dp(24), 0, 0)
        }
        root.addView(footer, matchWrap())

        scroll.addView(root)
        setContentView(scroll)
    }

    private fun showPreset(name: String) {
        prefs.edit().putString("preset", name).apply()

        val message = when (name) {
            "LOW" -> "LOW tersimpan. Gunakan pengaturan visual rendah di game milikmu."
            "DEFAULT" -> "Preset DEFAULT tersimpan."
            else -> "BALANCED tersimpan. Visual menggunakan nilai bawaan."
        }

        presetInfo.text = "ACTIVE PRESET\n$name\n\n$message"
        Toast.makeText(this, "$name preset saved", Toast.LENGTH_SHORT).show()
    }

    private fun copyLua() {
        val lua = """
-- LUNA LITE: contoh preset untuk game milik sendiri
-- Pasang sebagai LocalScript di StarterPlayerScripts.
local Lighting = game:GetService("Lighting")
local Workspace = game:GetService("Workspace")

Lighting.GlobalShadows = false
Lighting.FogEnd = 100000
Lighting.Brightness = 2
Lighting.ExposureCompensation = 0.05

for _, obj in ipairs(Workspace:GetDescendants()) do
    if obj:IsA("BasePart") then
        obj.CastShadow = false
    elseif obj:IsA("ParticleEmitter")
        or obj:IsA("Trail")
        or obj:IsA("Beam") then
        obj.Enabled = false
    end
end
        """.trimIndent()

        val clipboard =
            getSystemService(CLIPBOARD_SERVICE) as ClipboardManager

        clipboard.setPrimaryClip(
            ClipData.newPlainText("LUNA LITE Lua preset", lua)
        )

        Toast.makeText(this, "Lua preset copied", Toast.LENGTH_SHORT).show()
    }

    private fun deviceSummary(): String {
        val dm = resources.displayMetrics

        val refresh = try {
            @Suppress("DEPRECATION")
            windowManager.defaultDisplay.refreshRate
        } catch (_: Exception) {
            0f
        }

        return "Android: ${android.os.Build.VERSION.RELEASE} (API ${android.os.Build.VERSION.SDK_INT})\n" +
            "Device: ${android.os.Build.MANUFACTURER} ${android.os.Build.MODEL}\n" +
            "Screen: ${dm.widthPixels} × ${dm.heightPixels}\n" +
            "Display refresh rate: ${"%.0f".format(refresh)} Hz\n\n" +
            "Catatan: refresh rate layar bukan pengukuran FPS di Roblox."
    }

    private fun section(label: String): TextView =
        TextView(this).apply {
            text = label
            textSize = 12f
            setTextColor(purple)
            letterSpacing = 0.08f
            typeface = android.graphics.Typeface.DEFAULT_BOLD
            setPadding(0, dp(18), 0, dp(9))
        }

    private fun actionButton(
        label: String,
        action: () -> Unit
    ): Button =
        Button(this).apply {
            text = label
            isAllCaps = false
            textSize = 13f
            setTextColor(white)
            backgroundTintList =
                android.content.res.ColorStateList.valueOf(panel)
            setOnClickListener { action() }
            layoutParams = matchWidthWrap().apply {
                bottomMargin = dp(8)
            }
        }

    private fun dp(value: Int): Int =
        (value * resources.displayMetrics.density).toInt()

    private fun matchWrap() =
        LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )

    private fun matchWidthWrap() = matchWrap()
}
