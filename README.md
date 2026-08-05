# Release v1.9 — ULTIMATE PERFORMANCE VERSION

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

