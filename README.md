NEW VERSION 1.8
# Release v1.8 — The Gemini Intelligence & Pipeline Update 🚀

## 🎮 Core Graphics & Rendering Engine
* **Interactive GPU Renderer Selector**: Added a tab-segmented component under the **Dev Tweaks** tab.
* **Pipeline Toggling**: Dynamically switch between Default, GraphicsROM / SkiaGL, and SkiaVulkan hardware acceleration.
* **State Persistence**: Component selections securely persist across app launches via local client storage boundaries.
* **Educational Panel**: Integrated a high-contrast Material 3 card explaining modern GPU microcode, parallel processing, and shader math.

## 🤖 Gemini 3.5 Flash AI Integration
* **Retrofit Client Configuration**: Deployed a resilient architecture to interface with the `gemini-3.5-flash:generateContent` REST endpoint.
* **Network Reliability**: Built via `OkHttpClient` with 60-second timeouts and full `MoshiConverterFactory` serialization.
* **Intelligent Telemetry Analyzer**: Automatically aggregates live metrics (FPS, ping, thermals, RAM, ADPF governor status) for AI analysis.
* **Auto-Apply Hardware Tweaks**: Executes AI optimization vectors instantly across device profiles (`ULTIMATE_PERFORMANCE`, thread priority pinning, peak refresh rates).

## 📊 Diagnostics, Logging & UI
* **Real-Time Log Tracing**: Appends microcode-level pipeline injection logs directly to the active daemon logs.
* **Debug Console Output**: Emits instant configuration traces whenever a rendering framework swap is initialized.
* **Gemini UI Optimizer Card**: Positioned a control hub in the **Tune-Up** tab equipped with real-time stat pills and a one-click auto-apply utility.


### 🔧 Graphics Engine & Stability Fixes
* **Correct Property Mapping**: Mapped `Default` to `setprop debug.hwui.renderer default` to prevent syntax errors caused by empty value strings. Added comprehensive pipeline support for `Default`, `GraphicsROM / SkiaGL (skiagl)`, `SkiaVulkan (skiavk)`, and `Vulkan Direct (vulkan)`.
* **Unified Settings Persistence**: Consolidated the SharedPreferences key under `KEY_SELECTED_GPU_RENDERER` to ensure user configuration choice persists reliably across application restarts.
* **Eliminated SystemUI Crashes**: Removed disruptive background `am crash com.android.systemui` system calls that were previously triggering OS-level crashes when toggling rendering options.
* **UI State Synchronization**: Streamlined option selection state logic within `MainActivity.kt` to ensure the active hardware renderer remains accurately selected and highlighted in the user interface.


