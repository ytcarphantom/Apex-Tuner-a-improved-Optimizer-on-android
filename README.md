### Apex Tuner // BAKED-IN (Android 17 Kernel & Thermal Update)

# 🚀 Android 17 ("Cinnamon Bun" / API 37) Settings & Live System Integrations

A comprehensive suite of new Android 17 features, telemetry tools, and system integrations has been added to the **Settings** tab, organized via a new quick category filter bar (`All Settings`, `New in Android 17`, `Thermal & RAM`, `Display`, `NPU & Audio`, `Battery & Shield`, and `Per-Game`).

---

## 🔋 Charging Optimization & 80% Thermal Bypass
* **Charging Policies:** Toggle between Limit to 80% + Gaming Bypass, Adaptive Charging Pacing, and Unrestricted 100%.
* **Live Telemetry:** Real-time monitoring of battery cell charge percentage, temperature (°C), voltage (mV), and power source status.
* **Thermal Controls:** Customizable Plugged-In Thermal Bypass Cutoff slider (`36°C–45°C`).
* **Shortcuts:** Direct access link to System Battery Settings.

## 💬 App Bubbles, Bubble Bar & Split Shade Shield
* **"Anything-to-Bubble":** System-wide floating window controls with modes for Suppress All, Edge Dock, or Chat Only.
* **Alert Management:** Notification Cooldown for rapid consecutive alerts.
* **Gaming Guard:** Split Quick-Shade Accidental Swipe Guard optimized for landscape gaming sessions.

## 🧠 ProfilingManager Pre-OOM Watchdog & API 37 RAM Boundaries
* **Watchdog Integration:** `TRIGGER_TYPE_ANOMALY` Pre-OOM Watchdog monitoring.
* **Memory Profiles:** Selectable Process Memory Boundary profiles (`Strict API 37`, `Balanced`, `Game Priority`).
* **Heap Snapshot Tool:** Live inspection of native heap, ART/JVM heap, and kernel LMKD margins with automatic memory reclamation under high pressure.

## 📱 Large-Screen Adaptive Resizability & Foldable Posture
* **Resizability Tier:** Automatically detects device `smallestScreenWidthDp` against Android 17's >600dp tier.
* **Viewport Overrides:** Game Viewport Aspect Ratio Overrides (`Full Native`, `21:9 FOV`, `16:9 Pro`, `4:3 Stretch`).
* **Posture Shift:** Foldable & Dual-Screen Gaming Posture Shift support.

## ⚡ NPU Offload, Autonomous BT & L4S QoS
* **Hardware Telemetry:** Inspect hardware Vulkan API version, NPU feature availability, and active audio output routing (BLE Audio, Bluetooth A2DP, USB-C DAC, Wired, or Speaker).
* **Performance Toggles:** NPU Frame & Upscaling Offload, Autonomous Bluetooth Link Recovery, and L4S DSCP Socket Tagging (`TrafficStats 0xA170`).

## 🛡️ Advanced Protection Mode (AAPM) & Liquid Glass Styling
* **Security Auditing:** Keyguard and developer/USB surface status monitoring with an AAPM Gaming Guard and direct System Security & App Lock access.
* **UI Styling:** Toggle for Android 17's frosted Liquid Glass translucent border aesthetic.
  

Android 17 Optimization & Settings Suite Upgrade
1. Dedicated Android 17 Settings Tab
Added a dedicated Settings destination (NavigationScreen.SETTINGS) accessible from both the top navigation bar and via direct link in the Tuning tab.
Categorized into hardware-level tuning cards following Material 3 Cyberpunk design guidelines.
2. ADPF Predictive Thermal Headroom Autopilot
Configurable Thermal Trigger Threshold: Slider (60% to 95%, default 85%) to set when automated thermal defense engages.
Automated Thermal Actions:
Dynamic VRR Step-Down: Automatically downclocks frame cadence (e.g., 120Hz/144Hz down to 90Hz/60Hz) to prevent hard thermal throttling and frame-time spikes.
Proactive RAM Purge: Suppresses non-gaming background pools when temperatures rise.
Warning Alert Only: Emits notification alerts when thermal margins narrow.
Predictive Lookahead Window: Switchable 10-second vs 20-second forecast horizon passed directly to powerManager.getThermalHeadroom(seconds).
3. 16KB Memory Page Size & Native ELF Alignment Inspector
Auto-Trim 16KB Page Cache: Proactively trims fragmented Linux page cache buffers on game launch and exit.
16KB ELF Binary Alignment Inspector: Audits installed game .so binaries by inspecting ELF64 PT_LOAD segments for p_align >= 16384 compliance with Android 17 zero-overhead execution standards.
Real-time audit card displaying verified native libraries vs legacy 4KB boundary libraries.
4. Graphics Layer, Drivers & VRR Cadence
Graphics Driver Preference: Toggle between System Vulkan (Vulkan 1.3+ Native), ANGLE (OpenGL ES over Vulkan), and Prerelease Game Driver.
Variable Rate Shading (VRS) Hint: Driver-level hint for rendering efficiency on high-refresh displays.
Target VRR Refresh Cadence: Lock target display refresh rate (60Hz, 90Hz, 120Hz, 144Hz).
Thread Scheduling: Toggle Process.THREAD_PRIORITY_URGENT_DISPLAY (-8) and GameState.isLoading = true burst clocks.
5. Gaming Shield & Distraction Suppression (DND)
Configurable interruption filters: Priority Only (critical contacts/alarms), Total Silence (block all heads-up banners), or Visual Banners Only.
Auto-restore sound and ringer volume profile when games exit.

6. Haptic Sensory Feedback & Audio MMAP
Haptic Feedback: Off, Crisp Tactile Click, or Firm Pulse via VibratorManager / VibrationEffect with an interactive "Test Haptic Pulse" button.
FastMixer Audio Buffer: Low-latency AAudio MMAP toggle for zero-lag in-game spatial audio.
7. Floating FPS HUD Customization & Per-Game Profiles
Dynamic HUD Opacity: Live slider (40% to 100%) controlling overlay background transparency.
Telemetry Density: Minimal (FPS only), Balanced (FPS + RAM + Ping), or Full ADPF Telemetry.
Per-Game Custom Profiles: Assign custom Game Mode and target FPS to individual installed titles.



1. Android Dynamic Performance Framework (ADPF) & Predictive Thermal Headroom
Predictive Headroom Forecasting: Queries PowerManager.getThermalHeadroom() with predictive models (+10s and +20s ahead) to anticipate thermal throttling before it occurs.
Thermal Status Level Telemetry: Live readouts for THERMAL_STATUS_NONE (Peak Boost Available), LIGHT, MODERATE, and SEVERE.
Target Frame Pacing Hints: Frame cadence synchronization mapped to target display refresh rates.
2. 16KB Memory Page Size & Kernel Architecture
Hardware Page Architecture Detector: Queries Os.sysconf(OsConstants._SC_PAGESIZE) to identify 16KB page size hardware and kernel alignments.
16KB Page Cache Trim Action: Trims dirty page cache buffers and flushes unaligned memory allocations for faster game asset loading.
3. Android 17 Game Mode Manager (GameManager)
Game Mode Profiles:
⚡ Performance Mode: Maximizes CPU/GPU clock governors and minimizes input latency.
🔋 Battery Saver Mode: Directs scheduling onto efficiency cores with a 60 FPS cap.
⚖️ Balanced Mode: System default dynamic scaling.
🛠️ Custom Tuned Mode: Unlocks Variable Refresh Rate (VRR) targets.
GameState CPU Burst Signaling: Signals GameState(isLoading = true) during game launch to burst CPU clocks for faster shader compilation, switching to MODE_GAMEPLAY_UNINTERRUPTIBLE during active sessions.
4. Variable Refresh Rate (VRR) Cadence Lock
Display Mode Discovery: Discovers native hardware display modes from Display.supportedModes (60Hz, 90Hz, 120Hz, 144Hz, 165Hz).
Cadence Selector: Selectable chip selector to lock target vsync frame pacing.
5. Urgent Display Thread Priority
Kernel Scheduling Priority: Escalates active game process and HUD threads to Process.THREAD_PRIORITY_URGENT_DISPLAY (-8) to eliminate frame stuttering from background task interruptions.
6. Wi-Fi 7 / 6E Multi-Link Operation (MLO) Low Latency
Ultra-Low Latency Wi-Fi Lock: Engages WIFI_MODE_FULL_LOW_LATENCY with socket congestion bypass for competitive multiplayer game


7. Interactive UI Integration
Tuning Tab: Added the interactive Android 17 Next-Gen Kernel Suite card with 1-Tap "Apply Android 17 Kernel Optimizations".
Dashboard Tab: Added the Android 17 Kernel Telemetry summary card with quick boost controls.
Hardware Tab: Displays deep kernel architecture metrics (Page Size Architecture, 16KB Page Cache Status, ADPF Margin, Thermal Forecasts, Game Mode, and Scheduler Priority).
Game Launch Screen: Displays the Android 17 GameState Burst clock indicator during launch sequences.

# 📶 Wi-Fi 6 Latency Lock, Priority Game Focus & Auto-Restore Shield

A suite of new networking optimizations, single-app process isolation features, and dynamic system state management tools has been added to improve online stability and maximize available system resources during gaming sessions.

---

## 📶 Wi-Fi 6 Latency & Jitter Lock
* **Low-Latency Wi-Fi Lock:** Locks Wi-Fi 6 (802.11ax / 6E / 7) radios into `WIFI_MODE_FULL_LOW_LATENCY`, suppresses background multicast/mDNS broadcast wakeups, and binds sockets to the active Wi-Fi interface via `ConnectivityManager.bindProcessToNetwork` to eliminate cellular handoff spikes.
* **QoS Packet Prioritization:** Prioritizes network packets using configurable DSCP markings:
  * `EF Gaming (0xB8) / WMM AC_VO`
  * `CS4 Realtime (0x88) / WMM AC_VI`
  * `Standard`
* **Socket Tuning & Diagnostics:** Applies `TCP_NODELAY` and `TrafficStats` thread tagging, backed by a multi-sample Test Jitter benchmark displaying live Ping and ±ms Jitter telemetry.

## 🎯 Close All Apps & Prioritize 1 Game
* **Priority Game Focus Card:** Added a dedicated focus card on the Home screen, quick-pin (`★`) buttons in **My Games**, and a **Prioritize Only This Game (#1)** toggle inside individual game customization sheets.
* **Continuous Watchdog:** Features a persistent `ApexGameWatcherService` re-kill watchdog that sweeps running, recent, and cached background processes.
* **Aggressive Resource Allocation:** One-tap **Close All Others** and **Boost & Play** actions isolate your primary game—sparing only essential system services and Apex Tuner—while applying `THREAD_PRIORITY_URGENT_DISPLAY` and `MODE_GAMEPLAY_UNINTERRUPTIBLE`.

## ⚙️ Turn Off Performance-Draining Settings (With Auto-Restore)
* **Automated Setting Toggles:** Automatically disables resource-draining features during gameplay:
  * Background Auto-Sync
  * Adaptive Auto-Brightness
  * System Touch Sounds & Haptics
  * Pop-ups, Chat Bubbles & Notifications (DND)
* **Throttling Detection:** Alerts you with a one-tap shortcut if system Battery Saver mode is active and throttling CPU clocks.
* **State Snapshot & Auto-Restore:** Takes a snapshot of your device's original configuration before boosting and restores your initial settings automatically when the game session ends or the toggle is switched off.* 



# Apex Tuner `v1.9` WILL BE CALLED LEGITIMATE VERSION OF THE APP I DONT KNOW IF THIS VERSION WILL BE FINAL

**Apex Tuner** is an authentic, non-placebo performance monitoring suite, game launcher, and system telemetry engine built strictly around official Android SDK APIs and Linux kernel sysfs interfaces. 

Unlike typical "booster" utilities that rely on fake animations or claim impossible unrooted tweaks, Apex Tuner operates with total transparency—offering real-time frame pacing, hardware HAL queries, low-latency Wi-Fi management, and precise system telemetry.

---

## Key Features

### 1. Legitimate "Launch-Only Games" Engine
* **Certified Package Classification:** Strictly filters your device library using `ApplicationInfo.CATEGORY_GAME`, `ApplicationInfo.FLAG_IS_GAME`, and embedded 3D engine signatures (**Unity, Unreal Engine, Godot, Native Vulkan/OpenGL binaries**).
* **Automated Pre-Flight Routine:** Executed automatically upon game launch:
  * Cleans background app cache via standard framework APIs to free headroom prior to LMK intervention.
  * Acquires a `WifiManager.WIFI_MODE_FULL_LOW_LATENCY` lock to minimize packet sleep jitter.
  * Toggles Do Not Disturb (DND) mode to eliminate notification interruptions.
  * Attaches the in-game floating performance HUD (when granted overlay permission).
* **Strict Mode Toggle:** Allows filtering strictly to certified games or switching to view all installed packages.

### 2. Authentic FPS Meter & Frame Pacing Engine
* **Choreographer VSYNC Tracking:** Hooks into `android.view.Choreographer.postFrameCallback` to capture nanosecond-precise delta times between display refresh cycles.
* **Core Metrics Delivered:**
  * Real-time FPS readout with color-coded stability indicators.
  * Precise frame times in milliseconds ($16.6\text{ ms}$ for 60Hz and $8.3\text{ ms}$ for 120Hz baselines).
  * Average FPS, 1% Low / Min FPS, Frame Jitter ($\text{ms}$), and Dropped Frame Percentage.
  * Live rolling canvas waveform depicting the last 30 frames of frame-pacing stability.

### 3. Physical RAM Telemetry & LMK Insights
* **Hardware Memory Paging:** Queries `ActivityManager.getMemoryInfo` to report exact physical memory used, available, and total in gigabytes.
* **Linux LMK Margin:** Displays the exact Linux Low Memory Killer threshold in megabytes, outlining the exact safety boundary.
* **Standard Process Clearing:** Safely releases cached background memory via `killBackgroundProcesses` without risking core OS service stability or making false "virtual RAM" claims.

### 4. Multi-Core CPU & Thermal Telemetry
* **Hardware Frequency Breakdown:** Inspects Linux sysfs paths (`/sys/devices/system/cpu/cpu*/cpufreq/scaling_cur_freq`) to output real-time clock rates across Efficiency, Big, and Prime core clusters.
* **Thermal Headroom:** Tracks dynamic thermal status via Android's **Application Dynamic Performance Framework (ADPF)** to detect thermal throttling states before performance degrades.

### 5. GPU Hardware & 3D HAL Inspection
* **Hardware HAL Query:** Utilizes `EGL10` to pull live OpenGL ES and Vulkan driver profiles directly from the hardware HAL (Renderer Name, Silicon Vendor, API version, and Max Texture Resolution).
* **Anti-Scam Transparency:** Educates users on why third-party applications claiming to "overclock unrooted GPUs" are deceptive due to SELinux kernel protections.

### 6. In-Game Floating Performance HUD
* **Draggable Window Overlay:** Built with `WindowManager` (`TYPE_APPLICATION_OVERLAY`) to float real-time performance indicators—FPS, RAM load, CPU frequencies, and GPU status—over any active game.
* **Quick Access:** Seamless toggle available across the Dashboard, Hardware Monitor, and Game Launcher.

### 7. Advanced System & Network Diagnostics
* **Wi-Fi Low-Latency Lock (`WIFI_MODE_FULL_LOW_LATENCY`):** Suspends 802.11 power-saving sleep states (API 29+) to eliminate latency spikes. Tracks Link Speed (Mbps), RSSI signal strength, and detects 2.4 GHz vs 5 GHz/6 GHz spectrum congestion with Bluetooth.
* **Display Refresh Rate & Panel Inspector:** Leverages `WindowManager` and `Display.getMode()` to catch thermal/battery throttling on 90Hz, 120Hz, and 144Hz displays. Provides a direct shortcut to display settings.
* **Audio Latency & FastMixer Inspector:** Evaluates `AudioManager.PROPERTY_OUTPUT_FRAMES_PER_BUFFER` and native sample rates to calculate base hardware buffer latency ($\sim 4.0\text{ ms}$) versus high-latency Bluetooth codecs ($150\text{--}250\text{ ms}$).
* **Storage Micro-Stutter Monitor:** Evaluates F2FS/ext4 flash storage conditions using `StatFs` and warns when free storage drops below 15% (where write amplification degrades asset streaming).
* **Screen Awake Lock:** Employs `FLAG_KEEP_SCREEN_ON` to prevent panel timeout during extended downloads, matchmaking queues, or spectator modes.
* **Reality Check Audits:** Built-in architectural explanations debunking common mobile performance myths (e.g., unrooted kernel tweaking, placebo RAM creation, and sandbox boundary limits).

---

## App Build & Version Metadata

* **App Version:** `v1.9`
* **Version Code:** `19`
* **Package Name:** `com.aistudio.gameboost.qzvtr`
* **Target SDK:** API 35 (Android 15)
* **Architecture:** `arm64-v8a`

### 8. GitHub Release Manager & Update Telemetry
* **Automatic GitHub Release Querying:** Features an integrated background update checker that polls the official GitHub API (`/repos/ytcarphantom/Apex-Tuner-a-improved-Optimizer-on-android/releases/latest`) on launch or on-demand to parse semantic version tags (e.g., `v1.9` vs `1.8`), release notes, and attached `.apk` binaries.
* **Prominent Update Banner:** Displays a high-visibility, dismissible banner across app screens when a new version is detected, offering one-tap options to **Update Now** or view **What's New**.
* **GitHub Release Verifier Card:** Dedicated Overview dashboard card detailing verified repository legitimacy, current vs. latest GitHub release tags, direct APK download sizes, release timestamps, and quick actions.
* **In-App Changelog & Direct Download:** Allows users to read complete Markdown release notes directly inside the app and trigger instant downloads of the official `.apk` release asset.
* **Version Alert Simulation Mode:** Includes a **"Test Older Version Alert"** toggle within the verifier card to instantly preview and test the update UI flow as seen by users on older builds.

### 9. Power Management & Thermal Efficiency Diagnostics
* **Power Saver Mode Detection:** Monitors Android's `PowerManager.isPowerSaveMode()` in real time to alert when system-wide battery saver settings are actively throttling CPU/GPU clock rates or capping screen refresh rates to 60Hz.
* **Smart Thermal & Battery Governor:** Tracks battery temperature (`BatteryManager.EXTRA_TEMPERATURE`) alongside ADPF thermal status to identify thermal-induced throttling early, helping prevent sudden FPS drops during prolonged sessions.
* **Low-Power Profile Toggles:** Provides one-tap shortcuts to system battery saver settings and display refresh rate controls, allowing users to balance battery longevity and maximum frame-pacing performance on demand.

## ALL OF THIS STUFF BELOW IS ALL FAKE I WANTED IT BOOST PERFORMANCE NOT DECREASE GAMING PERFORMANCE PLEASE NOTE THAT A NEW VERSION WILL COME OUT LATER TOMORROW 



# Release v1.9 — ULTIMATE PERFORMANCE VERSION & INTERNATIONAL TWEAKS & FIXED CODE UPDATE FOR BETTER PERFORMANCE 

### Core Settings Module

The Core Settings Module provides system-wide performance tuning through an interactive Material 3 (M3) interface. Users can toggle individual parameters or utilize the master activation control. All configurations persist across system reboots.

#### Key Features

*   **Maximize FPS**
    *   Unlocks system-wide higher frame rate caps aiming for a 120 FPS target.
    *   Forces the display panel to maintain its peak refresh rate dynamically.
    *   Engages Android Swappy frame pacing to eliminate micro-stutters and frame drops.
*   **Minimize Input Lag**
    *   Activates zero-delay input handling using `ON_BOOST` low-latency responses.
    *   Enhances touch responsiveness via Ultra-Gaming touch sensitivity profiles.
    *   Pins critical gaming thread priorities and opens low-latency audio pipelines.
*   **Optimize Game Configs**
    *   Applies pro-tested visual clarity presets optimized for competitive speed.
    *   Enables Vulkan pre-transform scaling to reduce hardware rendering overhead.
    *   Optimizes GPU hardware queue scheduling for faster frame dispatching.
*   **Strip System Bloat**
    *   Terminates hidden background process trees during active gaming sessions.
    *   Suppresses background telemetry data tracking to reclaim network bandwidth.
    *   Triggers automatic memory sweeps to free up idle RAM blocks.
*   **Prioritize Your Game**
    *   Pauses non-essential background tasks temporarily to maximize resource allocation.
    *   Locks CPU and GPU governors into explicit high-performance modes.
    *   Suppresses incoming notifications to prevent overlay lag and distractions.
*   **Monitor System Health**
    *   Displays a real-time hardware telemetry overlay during gameplay.
    *   Tracks live metrics: FPS, CPU temperature, battery temperature, ping, and RAM.
    *   Identifies immediate hardware bottlenecks before they impact performance.

#### User Interface

The module utilizes an intuitive layout built on modern design principles:
*   **Individual Interactive Switches**: Six independent Material 3 switches for granular preference control.
*   **Master Control Button**: A prominent "APPLY ALL 6 CORE SETTINGS NOW" button for instant, single-tap optimization.
*   **Persistent Storage**: All user selections are saved locally and re-applied automatically.

### 🚀 Max Performance & Zero Stutter Turbo Engine
* **One-Click Master Action**: Added `boostAllToMaxPerformanceAndZeroStutter()` in `TunerViewModel` to unlock 100% hardware capabilities.
* **Stutter Elimination Pipeline**: Engages Swappy Frame Pacing fences, ADPF Thermal Governors, thread priority pinning, zero-latency input mode (`ON_BOOST`), `ULTRA_GAMING` touch response, and GPU queue optimization.
* **Hero Dashboard Banner**: Added `MaxPerformanceZeroStutterCard` at the top of `MainActivity.kt` with live status indicators (120 FPS Locked, 0% Stutter Pacing, GPU 950MHz Peak) and a prominent "BOOST TO MAX PERFORMANCE & DELETE STUTTERING NOW" action button.

### 🤖 AI Game Mode — Smart Per-Game Real-Time Performance Tuning
* **Smart Real-Time Adaptation**: Continuously analyzes play session metrics (FPS stability, CPU/GPU thermal load, frame rendering time, and stutter variance).
* **Dynamic CPU & GPU Limits**: Dynamically adapts GPU clock targets (up to 950MHz Peak) and CPU power capacity limits in real time to prevent frame drops before stutters occur.
* **Per-Game Target Profiles**: Includes selectable per-game profile chips (Call of Duty Mobile, Genshin Impact, PUBG Mobile, Asphalt 9, Wild Rift, Auto-Detect Session).
* **Real-Time Adaptive Telemetry HUD**: Displays live session stats including GPU Clock Target (MHz), CPU Power Limit (%), Frame Render Time (ms), and Live Stutter Rate (0%).

### ⚙️ Core Optimization Engine (`OptimizationEngine.kt`)
Added low-level system optimization utilities including:
* **Display Refresh Rate Locking (`forceDisplayFreshness`)**: Forces hardware display parameters (`peak_refresh_rate` & `min_refresh_rate`) to lock at peak refresh rates (e.g. 120Hz).
* **RAM Plus Virtual Swap Control (`toggleRamPlus`)**: Disables ZRAM virtual swapping (`ram_expand_size_list 0`) to prioritize physical high-speed DRAM.
* **Logging Overhead Stripping (`stripLoggingOverhead`)**: Quiets background logging daemons (`logd`) and tracing loops to reduce CPU context switching.
* **Native ADPF Performance Hints (`targetAdpfSession`)**: Binds active render and processing thread IDs to performance core clusters using Android's `PerformanceHintManager`.

### 🔄 ViewModel & UI Integration
* Connected `OptimizationEngine` directly to the Max Performance & Zero Stutter action in `TunerViewModel.kt` and `MainActivity.kt`.

## 📋 Summary of What Was Implemented

### 🧹 `CacheCleaner.kt` (Option 1 — Storage Cache Cleaner)
* Clears internal (`context.cacheDir`) and external (`context.externalCacheDir`) cache directories.
* Recursively traverses sub-directories, deletes temporary files, and calculates the exact amount of storage space freed in MB.

### 🧠 `MemoryWatcher.kt` (Option 2 — ComponentCallbacks2 Listener)
* Implements `ComponentCallbacks2` to listen for system-wide memory pressure notifications.
* Handles `TRIM_MEMORY_RUNNING_CRITICAL`, `TRIM_MEMORY_BACKGROUND`, and `onLowMemory()` events by clearing image/bitmap memory pools and calling `System.gc()`.
* Automatically registered on the `MainActivity` lifecycle (`onCreate`/`onDestroy`).

### 🔗 `openSystemStorageSettings` (Option 3 — System Storage Deep Link)
* Deep-links directly to Android OS System Storage Settings (`Settings.ACTION_INTERNAL_STORAGE_SETTINGS`) with a fallback to general system settings.
* Exposed via `OptimizationEngine.kt` and `TunerViewModel.kt` (`clearStorageCache` & `openStorageSettings`).


### Summary of Architectural Configurations AND new features 

* **Large Heap Allocation (`android:largeHeap="true"`)**: Successfully integrated within the `<application>` tag inside the `AndroidManifest.xml` layout. This structurally commands the Android Runtime (ART) to maximize the Java heap ceiling for our process, elevating memory thresholds from standard caps up to 512MB+ based on physical hardware.
* **Native OS Execution Model**: Bypasses artificial memory bottlenecks by utilizing official system configuration architectures. Unlike iOS constraints which require custom signing workflows or entitlement injections, the optimization layer directly declares its hardware capacity natively.

### Implemented Dynamic Resolution Downscaling

* **ResolutionScaler Engine (`SurfaceHolder.setFixedSize`)**: Integrated internal backbuffer resizing that decouples rendering backbuffer memory dimensions from physical display layout size. This reduces GPU pixel-shading and memory bandwidth costs directly inside the graphics pipeline without requiring Root access or ADB shell privileges.
* **Vulkan / Native Viewport Calculation (`VulkanViewportConfig`)**: Added dynamic viewport mapping (`VkViewport`) and scissor rect constraint (`VkRect2D`) calculations for native rendering pipeline passes.
* **Multi-Tier Resolution Downscaler**: Integrated `ResolutionScaler` into `OptimizationEngine.downscaleResolution()` as the primary zero-privilege method, while maintaining Root `wm size` as a secondary system-wide fallback option.
* 

# Release Notes: International Gaming Tweaks & Kernel Optimization

## Overview
This update introduces the dedicated **International Gaming & Kernel Optimization Module**, designed to stabilize cross-region routing, optimize SoC core scheduling, accelerate touch sampling, and eliminate JIT compilation micro-stutters.

---

## Features

### Dedicated International Tweaks Dashboard
* Added an **INTERNATIONAL** tab to both mobile and tablet navigation bars.
* Integrated real-time diagnostic gauges for:
  * Cross-region server ping
  * Sustained FPS boost
  * Frame stability percentage
  * Touch polling rates

### 1-Click Master International Optimizer
* Single-tap action to sequentially execute:
  1. Network congestion control
  2. CPU scheduling
  3. GPU composition
  4. Touch overclocking
  5. ART ahead-of-time (AOT) compilation
  6. Frame pacing
* Includes live progress feedback during optimization.

### International Gaming Presets
* **eSports Global Tournament:** Locks target frame rate at 120/144 FPS.
* **Low-Ping Multiplayer:** Uses Anycast routing and TCP FastOpen for reduced latency.
* **Sustained High-FPS:** Utilizes Energy-Aware Scheduling (EAS) core pinning and the Vulkan pipeline.
* **Low-End Overhaul:** Enables RAM compression swap (zRAM) and forces GPU hardware overlays.

### Cross-Region Low-Ping Routing & DNS
* Integrated regional routing nodes:
  * Tokyo
  * Singapore
  * Frankfurt
  * Virginia
  * São Paulo
* Integrated gaming Anycast DNS stacks:
  * Cloudflare 1.1.1.1 WARP Gaming
  * Google Public DNS (8.8.8.8)
  * Quad9 (9.9.9.9)
  * OpenDNS

---

## Kernel & Hardware Tweak Controls

* **TCP BBR & FastOpen Protocol:** Minimizes bufferbloat and packet loss across high-latency cross-region multiplayer routes.
* **Universal EAS SoC CPU Scheduler:** Pins primary game render threads to Prime/Cortex-X cores with elevated `top-app` `schedtune` boost.
* **Vulkan & SkiaVK Hardware Composition:** Enables zero-copy Vulkan composition and unsignaled buffer latching to minimize GPU pipeline overhead.
* **Touch Digitizer 480Hz Overclock:** Upscales touch polling sampling rates and assigns real-time `InputFlinger` thread scheduling priority.
* **ART Speed-Profile AOT Dexopt:** Pre-compiles application bytecode into native machine instructions to eliminate runtime JIT micro-stuttering.
* **Frame Pacing & Dynamic Jitter Lock:** Synchronizes engine frame delivery to display refresh rates using Android Swappy frame pacing.

---

## Live Kernel & Routing Console
* Added an interactive, real-time monospace console displaying executed `sysctl` properties, active network routes, and live hardware power states.


# Release Notes: Android Kernel Engine & Hardware Telemetry Inspector

## Overview
This update introduces the **Direct Android Linux Kernel Engine (`AndroidKernelEngine`)** alongside a real-time **Kernel Telemetry & Hardware Node Inspector**. Designed for high-performance low-level system tuning, this release enables direct POSIX syscall execution, sysfs/procfs node manipulation, dynamic governor swapping, and live system telemetry.

---

## Features

### Direct Android Linux Kernel Engine (`AndroidKernelEngine`)

* **Direct POSIX Syscalls:**
  * Elevates render and game thread priorities directly at the kernel level via the `setpriority` syscall using `Process.setThreadPriority(Process.THREAD_PRIORITY_URGENT_DISPLAY)`.

* **Virtual Filesystem (Sysfs & Procfs) Node Injector:**
  * **CPU & EAS Scheduler:** Writes performance flags directly to `/dev/stune/top-app/schedtune.boost`, `/dev/stune/top-app/schedtune.prefer_idle`, and `/proc/sys/kernel/sched_energy_aware`.
  * **CPU Frequency Scaling:** Applies policy updates across all CPU policy nodes via `/sys/devices/system/cpu/cpufreq/policy*/scaling_governor` and `/sys/devices/system/cpu/cpu*/cpufreq/scaling_governor`.
  * **GPU KGSL & Devfreq:** Enforces performance governor locks and bus clock locks on `/sys/class/kgsl/kgsl-3d0/devfreq/governor`.
  * **Linux TCP/IP Stack:** Injects network performance tuning via `/proc/sys/net/ipv4/tcp_congestion_control` (BBR), `/proc/sys/net/ipv4/tcp_fastopen`, and `/proc/sys/net/core/rmem_max`.
  * **Virtual Memory & Pagecache:** Directs runtime memory behavior via `/proc/sys/vm/swappiness`, `/proc/sys/vm/vfs_cache_pressure`, `/proc/sys/vm/drop_caches`, and `/proc/sys/vm/compact_memory`.
  * **I/O Queue Optimization:** Tunes read-ahead buffer sizes and queue schedulers on `/sys/block/*/queue/read_ahead_kb`.

---

### Live Linux Kernel Telemetry & Hardware Node Inspector

* **Real-Time Kernel Telemetry:**
  * Inspects kernel release strings, hardware features, and dynamic system state live from `/proc/version`, `/proc/sys/kernel/osrelease`, `/proc/cpuinfo`, `/proc/meminfo`, and active CPU frequency paths.

* **Dynamic Clock Governor Controller:**
  * Supports real-time switching between `performance`, `schedutil`, and `powersave` governors with instant sysfs node commitment.

* **Kernel VM Swappiness Selector:**
  * Configurable swappiness presets to eliminate background paging latency during workload bursts:
    * `10%` — Gaming Lock
    * `30%` — Balanced
    * `60%` — System Default
   
  

* **Pagecache & Dentry Memory Compaction:**
  * Single-tap utility to flush page caches, drop dentries/inodes (`drop_caches = 3`), and trigger immediate physical memory defragmentation (`compact_memory`).
