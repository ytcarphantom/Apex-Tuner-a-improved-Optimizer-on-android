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

* ### 🔧 Graphics Engine & Stability Fixes
* **Correct Property Mapping**: Mapped `Default` to `setprop debug.hwui.renderer default` to prevent syntax errors caused by empty value strings. Added comprehensive pipeline support for `Default`, `GraphicsROM / SkiaGL (skiagl)`, `SkiaVulkan (skiavk)`, and `Vulkan Direct (vulkan)`.
* **Unified Settings Persistence**: Consolidated the SharedPreferences key under `KEY_SELECTED_GPU_RENDERER` to ensure user configuration choice persists reliably across application restarts.
* **Eliminated SystemUI Crashes**: Removed disruptive background `am crash com.android.systemui` system calls that were previously triggering OS-level crashes when toggling rendering options.
* **UI State Synchronization**: Streamlined option selection state logic within `MainActivity.kt` to ensure the active hardware renderer remains accurately selected and highlighted in the user interface.

### 🧠 Memory Management & RAM Optimizer
* **Permission Declaration**: Verified and declared the `KILL_BACKGROUND_PROCESSES` permission in the `AndroidManifest.xml` layer.
* **RAM Logic Engine**: Implemented precise system memory utilization metrics via `ActivityManager.MemoryInfo()` inside `RamOptimizer.kt`. Added active background task memory cleanup across non-system application packages combined with targeted runtime Garbage Collection (`System.gc()`) triggers.
* **Compose UI Layer**: Engineered `RamOptimizerScreen.kt` using Material 3 components, featuring live real-time memory statistics, an animated storage utilization/progress bar, status feedback strings, and a dedicated **BOOST RAM NOW** interaction trigger.
* **App Layout Integration**: Successfully embedded the `RamOptimizerScreen()` composable into the main interface scroll container within `MainActivity.kt`.

### 🌡️ Dynamic Thermal Frame Rate Stabilizer
* **Automatic Thermal Guard & FPS Stabilization**: Monitors core temperatures to automatically lock frame rate pacing at a stable target (e.g., 60 FPS) when thermal thresholds are breached, successfully eliminating severe lag stutters and destructive frame spikes.
* **Integrated Mitigation Stack**: Automatically engages Swappy Frame Pacing, the ADPF Thermal Headroom Governor, 0.85x Dynamic Resolution Downscaling, and Panel Eco-Thermal Controls under heavy thermal loads to quickly lower device temperatures.
* **Live Temperature Badges**: Engineered color-coded core temperature monitoring states (`NORMAL`, `THROTTLED`, `CRITICAL`) inside the `ThermalFpsStabilizerCard` UI to give immediate visual telemetry feedback.
* **Heat Protection Status Box**: Added a real-time status UI block showcasing active hardware mitigation parameters and running background cooling algorithms.
* **Manual Protection Trigger**: Placed a one-tap interaction button (`btn_engage_thermal_stabilizer`) to let users manually initialize thermal cooling routines and lock down the frame pacing engine on demand.
