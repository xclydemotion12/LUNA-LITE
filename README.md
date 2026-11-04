# LUNA LITE — Android Graphics Companion

Starter Android project for Android 10+ (minSdk 29), with a pure-black/purple UI.

## Included
- LOW / BALANCED / DEFAULT preset selection saved locally.
- Basic device and display refresh-rate information.
- Copyable Lua example for a Roblox experience you own/control.
- No executor, injection, key bypass, anti-cheat evasion, or Roblox process modification.

## Build
1. Open this folder in Android Studio (recent version).
2. Allow Gradle sync and install the Android SDK Platform 35 if prompted.
3. Select Build > Build Bundle(s) / APK(s) > Build APK(s).
4. The debug APK will be under `app/build/outputs/apk/debug/`.

## Important limitations
This is a companion app. It cannot change graphics settings inside the separate Roblox app.
The displayed refresh rate is the screen's refresh rate, not Roblox FPS.
60 FPS is a target only; actual performance depends on device and game.
The Lua example is intended only for a Roblox experience you own/control. Review and test it before use.
