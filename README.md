### RELEASING 10 AM PST OR 5 PM PST
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
