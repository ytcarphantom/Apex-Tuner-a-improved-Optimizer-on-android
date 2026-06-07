<div align="center">
<img width="1200" height="475" alt="GHBanner" src="https://ai.google.dev/static/site-assets/images/share-ais-513315318.png" />
</div>

# Run and deploy your AI Studio app

This contains everything you need to run your app locally.

View your app in AI Studio: https://ai.studio/apps/d86756cf-196f-4143-b7f9-1e83d1a2b6fd

## Run Locally

**Prerequisites:**  [Android Studio](https://developer.android.com/studio)


1. Open Android Studio
2. Select **Open** and choose the directory containing this project
3. Allow Android Studio to fix any incompatibilities as it imports the project.
4. Create a file named `.env` in the project directory and set `GEMINI_API_KEY` in that file to your Gemini API key (see `.env.example` for an example)
5. Remove this line from the app's `build.gradle.kts` file: `signingConfig = signingConfigs.getByName("debugConfig")`
6. Run the app on an emulator or physical device


What Was Fixed & Optimized:
AutoMirrored Icons: Updated all deprecated usages of Icons.Filled.Launch to Icons.AutoMirrored.Filled.Launch along with the appropriate auto-mirrored import statement.
Horizontal Dividers: Mitigated Material 3 deprecation warnings by cleanly replacing older Divider components with modern HorizontalDivider.
Warning Suppression: Applied @Suppress("DEPRECATION") to scanInstalledApps in TunerViewModel.kt to safely and cleanly handle the older API package checks.
Test Integrity: Ensured that the compilation succeeds fully, of which both the Android compilation check (compile_applet) and the entire local test suite (gradle :app:testDebugUnitTest) pass flawlessly.
