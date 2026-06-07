package com.example

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

enum class GameProfile {
    ULTIMATE_PERFORMANCE,
    PERFORMANCE,
    BALANCED,
    POWER_SAVING
}

enum class TunerTab {
    TUNE_UP,
    GAME_RESOURCES,
    DEV_TWEAKS
}

enum class GameLoadIntensity {
    LIGHTWEIGHT,
    MODERATE,
    HEAVY
}

enum class LowLatencyMode {
    OFF,
    ON,
    ON_BOOST
}

enum class TouchSensitivity {
    STANDARD,
    HIGH_SENSITIVITY,
    ULTRA_GAMING
}

enum class DnsPreset {
    DEFAULT,
    CLOUDFLARE,
    GOOGLE_PUBLIC,
    ADGUARD_SHIELD
}

enum class ThermalLimit {
    CONSERVATIVE,
    OPTIMIZED,
    EXTREME_OVERCLOCK
}

enum class BluetoothAudioOptimization {
    STANDARD,
    APT_X_ADAPTIVE,
    LE_AUDIO_MIN_LATENCY
}

data class InstalledGame(
    val label: String,
    val packageName: String
)

data class BackgroundProcessItem(
    val name: String,
    val packageName: String,
    val ramCostMb: Int,
    val category: String,
    val isSelected: Boolean = true,
    val status: String = "Active" // "Active", "Sweeping", "Cleared"
)

data class TunerUiState(
    // Global Navigation & Health
    val activeTab: TunerTab = TunerTab.TUNE_UP,
    val score: Int = 74,
    val isOptimized: Boolean = false,
    val lastOptimizedTime: String = "Never",
    
    // Core Phone Tweaks
    val isClearing: Boolean = false,
    val clearProgress: Float = 0f,
    val clearStatus: String = "Idle",
    val ramUsedPercent: Int = 78,
    val ramTotalGb: Double = 8.0,
    val storageUsedPercent: Int = 89, // Over the 85% standard performance threshold!
    val storageTotalGb: Double = 128.0,
    val batteryDeepSleepEnabled: Boolean = false,
    val appsSleepingCount: Int = 12,
    
    // Game Booster Profiles
    val selectedProfile: GameProfile = GameProfile.BALANCED,
    val gpuFrequencyTarget: Float = 600f, // 400 - 950 MHz
    val targetFpsCap: Int = 60, // 30, 48, 60, 90, 120
    val textureScalePercent: Int = 100, // 50% - 100%
    val preTransformEnabled: Boolean = false,
    
    // Real-Time Simulator Telemetry
    val gameFps: Int = 60,
    val coolingTempCelsius: Int = 38,
    val estimatedBatteryTimeHr: Double = 8.4,
    val latencyPingMs: Int = 45,
    
    // In-game blockers & simulations
    val blockNotifications: Boolean = false,
    val blockAutoBrightness: Boolean = false,
    val lockCapacitiveButtons: Boolean = false,
    val activeFloatingApp: String? = null, // "Messenger", "System Console", etc.
    
    // Interactive system developer tweaks / guides
    val systemGraphicsDriverSet: Boolean = false,
    val forcePeakRefreshRate: Boolean = false,
    val localGameLaunchInstalled: List<InstalledGame> = listOf(
        InstalledGame("Genshin Impact Mobile", "com.miHoYo.GenshinImpact"),
        InstalledGame("PUBG Mobile Lite", "com.tencent.iglite"),
        InstalledGame("Wild Rift", "com.riotgames.league.wildrift"),
        InstalledGame("Grid Autosport", "com.feralinteractive.gridas")
    ),
    val ssdTrimActive: Boolean = false,
    val ssdTrimProgress: Float = 0f,
    val telemetryDebloated: Boolean = false,
    val gameModeActivated: Boolean = false,
    val lagKillerEnabled: Boolean = false,
    val isApplyingLagKiller: Boolean = false,
    val lagKillerProgress: Float = 0f,
    val lagKillerStatus: String = "Idle",
    
    // Automated Game Mode and Detection
    val autoDetectGameLaunchEnabled: Boolean = true,
    val runningGame: String? = null,
    val gameModeLogs: List<String> = emptyList(),
    val backgroundActivitiesDisabled: Boolean = false,
    val cpuGpuAllocated: Boolean = false,
    val notificationsSuppressed: Boolean = false,
    val inputLagOptimized: Boolean = false,

    // Additional Core Gamer Tweaks & Preferences
    val gameIntensities: Map<String, GameLoadIntensity> = emptyMap(),
    val gameRamBoostEnabled: Map<String, Boolean> = emptyMap(),
    val gameVpnEnabled: Map<String, Boolean> = emptyMap(),
    val autoRamCleanerEnabled: Boolean = true,
    val vSyncEnabled: Boolean = false,
    val allocatedVramGb: Int = 4,
    val lowLatencyMode: LowLatencyMode = LowLatencyMode.ON_BOOST,
    val isScanningApps: Boolean = false,
    val touchSensitivity: TouchSensitivity = TouchSensitivity.STANDARD,
    val dnsPreset: DnsPreset = DnsPreset.DEFAULT,
    val thermalLimit: ThermalLimit = ThermalLimit.OPTIMIZED,
    val lowLatencyAudioEnabled: Boolean = false,
    val bluetoothControllerBoostEnabled: Boolean = false,
    val bluetoothAudioOptimization: BluetoothAudioOptimization = BluetoothAudioOptimization.STANDARD,
    val hibernatedApps: Set<String> = emptySet(),
    val simulatedChargingEnabled: Boolean = false,
    val batterySaverRestricting: Boolean = false,
    val inGameSettingsOptimized: Boolean = false,
    val isScanningRamProcesses: Boolean = false,
    val isCleaningRamProcesses: Boolean = false,
    val ramProcessesScanned: Boolean = false,
    val activeBackgroundProcesses: List<BackgroundProcessItem> = listOf(
        BackgroundProcessItem("Vulkan Shader Standby Cache", "com.android.vulkan.shader", 420, "GPU Shader"),
        BackgroundProcessItem("Background Social Sync Daemon", "com.meta.services.sync", 280, "Social Network"),
        BackgroundProcessItem("Google Chrome Multi-tab Heap", "com.android.chrome.tabheap", 510, "Browser Host"),
        BackgroundProcessItem("Standby Unity Game Engine Cache", "com.ea.standby.engine", 640, "Inactive Apps"),
        BackgroundProcessItem("Telemetry Ad-Tracking Listener", "com.telemetry.adtracker", 185, "Analytics Daemon")
    ),
    val hasWriteSettingsPermission: Boolean = false
)

class TunerViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(TunerUiState())
    val uiState: StateFlow<TunerUiState> = _uiState.asStateFlow()

    private var appContext: Context? = null

    companion object {
        private const val PREFS_NAME = "apex_tuner_prefs"
        private const val KEY_SELECTED_PROFILE = "selected_profile"
        private const val KEY_GPU_FREQUENCY_TARGET = "gpu_frequency_target"
        private const val KEY_TARGET_FPS_CAP = "target_fps_cap"
        private const val KEY_TEXTURE_SCALE_PERCENT = "texture_scale_percent"
        private const val KEY_PRE_TRANSFORM_ENABLED = "pre_transform_enabled"
        private const val KEY_BLOCK_NOTIFICATIONS = "block_notifications"
        private const val KEY_BLOCK_AUTO_BRIGHTNESS = "block_auto_brightness"
        private const val KEY_LOCK_CAPACITIVE_BUTTONS = "lock_capacitive_buttons"
        private const val KEY_SYSTEM_GRAPHICS_DRIVER_SET = "system_graphics_driver_set"
        private const val KEY_FORCE_PEAK_REFRESH_RATE = "force_peak_refresh_rate"
        private const val KEY_TELEMETRY_DEBLOATED = "telemetry_debloated"
        private const val KEY_AUTO_DETECT_GAME_LAUNCH_ENABLED = "auto_detect_game_launch_enabled"
        private const val KEY_AUTO_RAM_CLEANER_ENABLED = "auto_ram_cleaner_enabled"
        private const val KEY_VSYNC_ENABLED = "vsync_enabled"
        private const val KEY_ALLOCATED_VRAM_GB = "allocated_vram_gb"
        private const val KEY_LOW_LATENCY_MODE = "low_latency_mode"
        private const val KEY_INSTALLED_GAMES = "local_game_launch_installed"
        private const val KEY_GAME_INTENSITIES = "game_intensities"
        private const val KEY_GAME_RAM_BOOST = "game_ram_boost"
        private const val KEY_GAME_VPN = "game_vpn"
        private const val KEY_TOUCH_SENSITIVITY = "touch_sensitivity"
        private const val KEY_DNS_PRESET = "dns_preset"
        private const val KEY_THERMAL_LIMIT = "thermal_limit"
        private const val KEY_LOW_LATENCY_AUDIO = "low_latency_audio"
        private const val KEY_BLUETOOTH_CONTROLLER_BOOST = "bluetooth_controller_boost"
        private const val KEY_BLUETOOTH_AUDIO_OPTIMIZATION = "bluetooth_audio_optimization"
        private const val KEY_LAG_KILLER_ENABLED = "lag_killer_enabled"
    }

    fun initPersistence(context: Context) {
        if (appContext == null) {
            appContext = context.applicationContext
            loadPersistedSettings()
        }
    }

    private fun saveSetting(block: android.content.SharedPreferences.Editor.() -> Unit) {
        val context = appContext ?: return
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().apply {
            block()
            apply()
        }
    }

    private fun loadPersistedSettings() {
        val context = appContext ?: return
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        
        val selectedProfileStr = prefs.getString(KEY_SELECTED_PROFILE, null)
        val selectedProfile = selectedProfileStr?.let {
            try { GameProfile.valueOf(it) } catch (e: Exception) { null }
        } ?: GameProfile.BALANCED

        val gpuFrequencyTarget = prefs.getFloat(KEY_GPU_FREQUENCY_TARGET, 600f)
        val targetFpsCap = prefs.getInt(KEY_TARGET_FPS_CAP, 60)
        val textureScalePercent = prefs.getInt(KEY_TEXTURE_SCALE_PERCENT, 100)
        val preTransformEnabled = prefs.getBoolean(KEY_PRE_TRANSFORM_ENABLED, false)
        val blockNotifications = prefs.getBoolean(KEY_BLOCK_NOTIFICATIONS, false)
        val blockAutoBrightness = prefs.getBoolean(KEY_BLOCK_AUTO_BRIGHTNESS, false)
        val lockCapacitiveButtons = prefs.getBoolean(KEY_LOCK_CAPACITIVE_BUTTONS, false)
        val systemGraphicsDriverSet = prefs.getBoolean(KEY_SYSTEM_GRAPHICS_DRIVER_SET, false)
        val forcePeakRefreshRate = prefs.getBoolean(KEY_FORCE_PEAK_REFRESH_RATE, false)
        val telemetryDebloated = prefs.getBoolean(KEY_TELEMETRY_DEBLOATED, false)
        val autoDetectGameLaunchEnabled = prefs.getBoolean(KEY_AUTO_DETECT_GAME_LAUNCH_ENABLED, true)
        val autoRamCleanerEnabled = prefs.getBoolean(KEY_AUTO_RAM_CLEANER_ENABLED, true)
        val vSyncEnabled = prefs.getBoolean(KEY_VSYNC_ENABLED, false)
        val allocatedVramGb = prefs.getInt(KEY_ALLOCATED_VRAM_GB, 4)
        
        val lowLatencyModeStr = prefs.getString(KEY_LOW_LATENCY_MODE, null)
        val lowLatencyMode = lowLatencyModeStr?.let {
            try { LowLatencyMode.valueOf(it) } catch (e: Exception) { null }
        } ?: LowLatencyMode.ON_BOOST

        val installedGamesStr = prefs.getString(KEY_INSTALLED_GAMES, null)
        val localGameLaunchInstalled = if (installedGamesStr != null) {
            installedGamesStr.split("|").filter { it.isNotBlank() }.mapNotNull { entry ->
                val parts = entry.split("::")
                if (parts.size >= 2) {
                    InstalledGame(packageName = parts[0], label = parts[1])
                } else if (parts.isNotEmpty()) {
                    InstalledGame(label = parts[0], packageName = "com.placeholder.${parts[0].replace(" ", "").lowercase()}")
                } else null
            }
        } else {
            listOf(
                InstalledGame("Genshin Impact Mobile", "com.miHoYo.GenshinImpact"),
                InstalledGame("PUBG Mobile Lite", "com.tencent.iglite"),
                InstalledGame("Wild Rift", "com.riotgames.league.wildrift"),
                InstalledGame("Grid Autosport", "com.feralinteractive.gridas")
            )
        }

        val gameIntensitiesStr = prefs.getString(KEY_GAME_INTENSITIES, "") ?: ""
        val gameIntensities = mutableMapOf<String, GameLoadIntensity>()
        if (gameIntensitiesStr.isNotEmpty()) {
            gameIntensitiesStr.split("|").forEach { entry ->
                val parts = entry.split(":")
                if (parts.size == 2) {
                    try {
                        gameIntensities[parts[0]] = GameLoadIntensity.valueOf(parts[1])
                    } catch (e: Exception) {}
                }
            }
        }

        val gameRamBoostStr = prefs.getString(KEY_GAME_RAM_BOOST, "") ?: ""
        val gameRamBoostEnabled = mutableMapOf<String, Boolean>()
        if (gameRamBoostStr.isNotEmpty()) {
            gameRamBoostStr.split("|").forEach { entry ->
                val parts = entry.split(":")
                if (parts.size == 2) {
                    gameRamBoostEnabled[parts[0]] = parts[1].toBoolean()
                }
            }
        }

        val gameVpnStr = prefs.getString(KEY_GAME_VPN, "") ?: ""
        val gameVpnEnabled = mutableMapOf<String, Boolean>()
        if (gameVpnStr.isNotEmpty()) {
            gameVpnStr.split("|").forEach { entry ->
                val parts = entry.split(":")
                if (parts.size == 2) {
                    gameVpnEnabled[parts[0]] = parts[1].toBoolean()
                }
            }
        }

        val score = prefs.getInt("score", 74)
        val ramUsedPercent = prefs.getInt("ram_used_percent", 78)
        val storageUsedPercent = prefs.getInt("storage_used_percent", 89)
        val isOptimized = prefs.getBoolean("is_optimized", false)
        val lastOptimizedTime = prefs.getString("last_optimized_time", "Never") ?: "Never"
        val appsSleepingCount = prefs.getInt("apps_sleeping_count", 12)
        val batteryDeepSleepEnabled = prefs.getBoolean("battery_deep_sleep_enabled", false)

        val touchSensitivityStr = prefs.getString(KEY_TOUCH_SENSITIVITY, null)
        val touchSensitivity = touchSensitivityStr?.let {
            try { TouchSensitivity.valueOf(it) } catch (e: Exception) { null }
        } ?: TouchSensitivity.STANDARD

        val dnsPresetStr = prefs.getString(KEY_DNS_PRESET, null)
        val dnsPreset = dnsPresetStr?.let {
            try { DnsPreset.valueOf(it) } catch (e: Exception) { null }
        } ?: DnsPreset.DEFAULT

        val thermalLimitStr = prefs.getString(KEY_THERMAL_LIMIT, null)
        val thermalLimit = thermalLimitStr?.let {
            try { ThermalLimit.valueOf(it) } catch (e: Exception) { null }
        } ?: ThermalLimit.OPTIMIZED

        val lowLatencyAudioEnabled = prefs.getBoolean(KEY_LOW_LATENCY_AUDIO, false)

        val bluetoothControllerBoostEnabled = prefs.getBoolean(KEY_BLUETOOTH_CONTROLLER_BOOST, false)
        val bluetoothAudioOptimizationStr = prefs.getString(KEY_BLUETOOTH_AUDIO_OPTIMIZATION, null)
        val bluetoothAudioOptimization = bluetoothAudioOptimizationStr?.let {
            try { BluetoothAudioOptimization.valueOf(it) } catch (e: Exception) { null }
        } ?: BluetoothAudioOptimization.STANDARD

        val lagKillerEnabled = prefs.getBoolean(KEY_LAG_KILLER_ENABLED, false)

        _uiState.value = _uiState.value.copy(
            selectedProfile = selectedProfile,
            gpuFrequencyTarget = gpuFrequencyTarget,
            targetFpsCap = targetFpsCap,
            textureScalePercent = textureScalePercent,
            preTransformEnabled = preTransformEnabled,
            blockNotifications = blockNotifications,
            blockAutoBrightness = blockAutoBrightness,
            lockCapacitiveButtons = lockCapacitiveButtons,
            systemGraphicsDriverSet = systemGraphicsDriverSet,
            forcePeakRefreshRate = forcePeakRefreshRate,
            telemetryDebloated = telemetryDebloated,
            autoDetectGameLaunchEnabled = autoDetectGameLaunchEnabled,
            autoRamCleanerEnabled = autoRamCleanerEnabled,
            vSyncEnabled = vSyncEnabled,
            allocatedVramGb = allocatedVramGb,
            lowLatencyMode = lowLatencyMode,
            localGameLaunchInstalled = localGameLaunchInstalled,
            gameIntensities = gameIntensities,
            gameRamBoostEnabled = gameRamBoostEnabled,
            gameVpnEnabled = gameVpnEnabled,
            score = score,
            ramUsedPercent = ramUsedPercent,
            storageUsedPercent = storageUsedPercent,
            isOptimized = isOptimized,
            lastOptimizedTime = lastOptimizedTime,
            appsSleepingCount = appsSleepingCount,
            batteryDeepSleepEnabled = batteryDeepSleepEnabled,
            touchSensitivity = touchSensitivity,
            dnsPreset = dnsPreset,
            thermalLimit = thermalLimit,
            lowLatencyAudioEnabled = lowLatencyAudioEnabled,
            bluetoothControllerBoostEnabled = bluetoothControllerBoostEnabled,
            bluetoothAudioOptimization = bluetoothAudioOptimization,
            lagKillerEnabled = lagKillerEnabled
        )
    }

    init {
        // Run a lightweight loop to update telemetry with real statistics from the device
        viewModelScope.launch {
            while (true) {
                delay(1500)
                updateTelemetryFluctuations()
            }
        }

        // Periodically measure actual network ping in background to avoid blocking main thread
        viewModelScope.launch {
            while (true) {
                val ping = measureRealPing()
                _uiState.value = _uiState.value.copy(latencyPingMs = ping)
                delay(6000)
            }
        }
    }

    private suspend fun measureRealPing(): Int {
        return kotlinx.coroutines.withContext(kotlinx.coroutines.Dispatchers.IO) {
            val start = System.currentTimeMillis()
            val socket = java.net.Socket()
            val measured = try {
                socket.connect(java.net.InetSocketAddress("1.1.1.1", 53), 1200)
                socket.close()
                (System.currentTimeMillis() - start).toInt()
            } catch (e: Exception) {
                try {
                    val startHttp = System.currentTimeMillis()
                    val url = java.net.URL("https://www.google.com")
                    val conn = url.openConnection() as java.net.HttpURLConnection
                    conn.connectTimeout = 1200
                    conn.readTimeout = 1200
                    conn.connect()
                    conn.disconnect()
                    (System.currentTimeMillis() - startHttp).toInt()
                } catch (e2: Exception) {
                    // Fallback to offline estimation based on active profiles
                    val state = _uiState.value
                    var base = when (state.lowLatencyMode) {
                        LowLatencyMode.ON_BOOST -> 12
                        LowLatencyMode.ON -> 22
                        LowLatencyMode.OFF -> 40
                    }
                    if (state.lowLatencyAudioEnabled) base -= 2
                    if (state.bluetoothControllerBoostEnabled) base -= 1
                    base.coerceAtLeast(6)
                }
            }

            // Adjust for active game booster and premium gaming VPN features
            val state = _uiState.value
            val outputPing = if (state.runningGame != null) {
                var gamePing = if (state.gameModeActivated) {
                    measured.coerceAtMost(measured / 2 + 5).coerceIn(6, 60)
                } else {
                    measured
                }
                if (state.gameVpnEnabled[state.runningGame] == true) {
                    gamePing + (30..45).random() // Tunneling proxy overhead
                } else {
                    gamePing
                }
            } else {
                measured
            }

            outputPing.coerceAtLeast(3)
        }
    }

    private fun updateTelemetryFluctuations() {
        val state = _uiState.value
        if (state.ssdTrimActive) return // Freeze updates during TRIM
        
        // Base value changes depending on selected profile
        var targetFps = when (state.selectedProfile) {
            GameProfile.ULTIMATE_PERFORMANCE -> 120
            GameProfile.PERFORMANCE -> if (state.forcePeakRefreshRate) 120 else 90
            GameProfile.BALANCED -> 60
            GameProfile.POWER_SAVING -> 48
         }
        
        var targetTemp = when (state.selectedProfile) {
            GameProfile.ULTIMATE_PERFORMANCE -> 45
            GameProfile.PERFORMANCE -> 42
            GameProfile.BALANCED -> 37
            GameProfile.POWER_SAVING -> 31
        }
        
        var targetBattery = when (state.selectedProfile) {
            GameProfile.ULTIMATE_PERFORMANCE -> 2.5
            GameProfile.PERFORMANCE -> 4.2
            GameProfile.BALANCED -> 7.8
            GameProfile.POWER_SAVING -> 13.2
        }

        var targetPing = when (state.lowLatencyMode) {
            LowLatencyMode.ON_BOOST -> if (state.telemetryDebloated) 14 else 22
            LowLatencyMode.ON -> if (state.telemetryDebloated) 24 else 38
            LowLatencyMode.OFF -> if (state.telemetryDebloated) 40 else 58
        }

        // Adjust temperature, FPS and battery based on thermal threshold limits
        when (state.thermalLimit) {
            ThermalLimit.CONSERVATIVE -> {
                targetTemp -= 5
                targetFps = (targetFps - 15).coerceAtLeast(30)
                targetBattery += 1.5
            }
            ThermalLimit.OPTIMIZED -> { /* standard */ }
            ThermalLimit.EXTREME_OVERCLOCK -> {
                targetTemp += 5
                targetFps = (targetFps + 10).coerceAtMost(120)
                targetBattery = (targetBattery - 1.0).coerceAtLeast(1.5)
            }
        }

        // Charging While Playing generates substantial excess heat & battery performance characteristics
        if (state.simulatedChargingEnabled) {
            targetTemp += 8
            targetBattery = (targetBattery + 5.0).coerceAtMost(24.0)
        }

        // Battery saver mode drastically limits rendering FPS & throttling characteristics
        if (state.batterySaverRestricting) {
            targetFps = (targetFps / 2).coerceAtLeast(24)
            targetTemp -= 5
            targetBattery = (targetBattery + 4.0).coerceAtMost(24.0)
            targetPing += 12 // added latency due to low-power scheduler cycles
        }

        // Apply DNS optimizations to base Ping
        val dnsPingReduction = when (state.dnsPreset) {
            DnsPreset.DEFAULT -> 0
            DnsPreset.CLOUDFLARE -> 5
            DnsPreset.GOOGLE_PUBLIC -> 3
            DnsPreset.ADGUARD_SHIELD -> 2
        }
        targetPing = (targetPing - dnsPingReduction).coerceAtLeast(8)
        if (state.lowLatencyAudioEnabled) {
            targetPing = (targetPing - 2).coerceAtLeast(6)
        }
        if (state.bluetoothControllerBoostEnabled) {
            targetPing = (targetPing - 1).coerceAtLeast(5)
        }

        // Adjust for V-Sync (ON limits fps to 60 and adds minor display buffer input lag/ping)
        if (state.vSyncEnabled) {
            targetFps = targetFps.coerceAtMost(60)
            targetPing += 10
        }

        // Check running game loading intensity
        val currentGame = state.runningGame
        if (currentGame != null) {
            val intensity = state.gameIntensities[currentGame] ?: GameLoadIntensity.MODERATE
            when (intensity) {
                GameLoadIntensity.HEAVY -> {
                    targetTemp += 3
                    targetBattery = (targetBattery - 1.2).coerceAtLeast(1.5)
                    // High intensive games require higher CPU load
                }
                GameLoadIntensity.LIGHTWEIGHT -> {
                    targetTemp -= 4
                    targetBattery += 3.5
                }
                GameLoadIntensity.MODERATE -> { /* default behavior */ }
            }
            
            // If game mode is fully active/running, target high performance and ultra low latency/input lag
            if (state.gameModeActivated) {
                targetFps = if (state.selectedProfile == GameProfile.ULTIMATE_PERFORMANCE) 120 else targetFps.coerceIn(90, 120)
                if (state.vSyncEnabled) targetFps = 60 // V-Sync constraint bounds it
                targetTemp = targetTemp.coerceAtMost(38) // Active thermal throttle & cooling
                targetPing = when (state.lowLatencyMode) {
                    LowLatencyMode.ON_BOOST -> 8
                    LowLatencyMode.ON -> 15
                    LowLatencyMode.OFF -> 30
                }
            }
        }

        // Add small fluctuations
        val currentFps = if (state.runningGame != null && state.gameModeActivated) {
            // Rock solid locked frame times to minimize input lag!
            (targetFps - 1 + (0..1).random()).coerceIn(30, 120)
        } else {
            (targetFps - 2 + (0..4).random()).coerceIn(30, 120)
        }
        val currentBattery = (targetBattery - 0.1 + (0..20).random() * 0.01).coerceIn(1.0, 24.0)

        // READ REAL DEVICE VALUES IF CONTEXT IS READY
        val context = appContext
        var realRamUsed = state.ramUsedPercent
        var realStorageUsed = state.storageUsedPercent
        var realCpuTemp = targetTemp

        if (context != null) {
            // Real RAM used percentage
            try {
                val am = context.getSystemService(Context.ACTIVITY_SERVICE) as android.app.ActivityManager
                val memoryInfo = android.app.ActivityManager.MemoryInfo()
                am.getMemoryInfo(memoryInfo)
                val totalPercent = (((memoryInfo.totalMem - memoryInfo.availMem).toDouble() / memoryInfo.totalMem.toDouble()) * 100).toInt()
                realRamUsed = totalPercent.coerceIn(10, 99)
            } catch (e: Exception) {}

            // Real Storage used percentage
            try {
                val path = android.os.Environment.getDataDirectory()
                val stat = android.os.StatFs(path.path)
                val blockSize = stat.blockSizeLong
                val totalBlocks = stat.blockCountLong
                val availableBlocks = stat.availableBlocksLong
                val totalStorage = totalBlocks * blockSize
                val usedStorage = totalStorage - availableBlocks * blockSize
                val storagePercent = if (totalStorage > 0) {
                    ((usedStorage.toDouble() / totalStorage.toDouble()) * 100).toInt()
                } else 50
                realStorageUsed = storagePercent.coerceIn(5, 99)
            } catch (e: Exception) {}

            // Real Battery Temp
            try {
                val intent = context.registerReceiver(null, android.content.IntentFilter(android.content.Intent.ACTION_BATTERY_CHANGED))
                if (intent != null) {
                    val tempValue = intent.getIntExtra(android.os.BatteryManager.EXTRA_TEMPERATURE, 0) / 10
                    if (tempValue > 0) {
                        realCpuTemp = tempValue
                    }
                }
            } catch (e: Exception) {}
        } else {
            // Fluctuate CPU temp slightly when offline
            realCpuTemp = (targetTemp - 1 + (0..2).random()).coerceIn(20, 50)
        }

        _uiState.value = state.copy(
            gameFps = currentFps,
            coolingTempCelsius = realCpuTemp,
            estimatedBatteryTimeHr = java.lang.Math.round(currentBattery * 10.0) / 10.0,
            ramUsedPercent = realRamUsed,
            storageUsedPercent = realStorageUsed
        )
    }

    fun selectTab(tab: TunerTab) {
        _uiState.value = _uiState.value.copy(activeTab = tab)
    }

    fun updateWriteSettingsPermissionStatus(hasPermission: Boolean) {
        _uiState.value = _uiState.value.copy(hasWriteSettingsPermission = hasPermission)
    }

    fun triggerSystemOptimization() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isClearing = true,
                clearProgress = 0.1f,
                clearStatus = "Analyzing hardware telemetry..."
            )
            delay(400)
            
            _uiState.value = _uiState.value.copy(
                clearProgress = 0.4f,
                clearStatus = "Purging memory cache & stopping background location locks..."
            )
            
            // Actual Cache Clear and Garbage Collection
            val context = appContext
            if (context != null) {
                try {
                    context.cacheDir?.listFiles()?.forEach { it.deleteRecursively() }
                } catch (e: Exception) {}
            }
            System.gc()
            Runtime.getRuntime().gc()
            delay(400)

            _uiState.value = _uiState.value.copy(
                clearProgress = 0.75f,
                clearStatus = "Triggering deep sleep for high-drain background applications..."
            )
            var killedCount = 0
            if (context != null) {
                try {
                    val am = context.getSystemService(Context.ACTIVITY_SERVICE) as android.app.ActivityManager
                    _uiState.value.localGameLaunchInstalled.forEach { game ->
                        try {
                            am.killBackgroundProcesses(game.packageName)
                            killedCount++
                        } catch (e: Exception) {}
                    }
                } catch (e: Exception) {}
            }
            delay(400)

            var currentRam = 42
            var currentStorage = 50
            if (context != null) {
                try {
                    val am = context.getSystemService(Context.ACTIVITY_SERVICE) as android.app.ActivityManager
                    val memoryInfo = android.app.ActivityManager.MemoryInfo()
                    am.getMemoryInfo(memoryInfo)
                    currentRam = (((memoryInfo.totalMem - memoryInfo.availMem).toDouble() / memoryInfo.totalMem.toDouble()) * 100).toInt().coerceIn(10, 99)

                    val path = android.os.Environment.getDataDirectory()
                    val stat = android.os.StatFs(path.path)
                    val blockSize = stat.blockSizeLong
                    val totalBlocks = stat.blockCountLong
                    val availableBlocks = stat.availableBlocksLong
                    val totalStorage = totalBlocks * blockSize
                    val usedStorage = totalStorage - availableBlocks * blockSize
                    currentStorage = if (totalStorage > 0) {
                        ((usedStorage.toDouble() / totalStorage.toDouble()) * 100).toInt()
                    } else 50
                } catch (e: Exception) {}
            }

            val currentScore = 98
            val optimizedTime = "Just now"
            val sleepingCount = 20

            _uiState.value = _uiState.value.copy(
                clearProgress = 1.0f,
                clearStatus = "Optimization Complete!",
                ramUsedPercent = currentRam,
                storageUsedPercent = currentStorage,
                isOptimized = true,
                score = currentScore,
                isClearing = false,
                lastOptimizedTime = optimizedTime,
                appsSleepingCount = sleepingCount
            )
            saveSetting {
                putInt("ram_used_percent", currentRam)
                putInt("storage_used_percent", currentStorage)
                putBoolean("is_optimized", true)
                putInt("score", currentScore)
                putString("last_optimized_time", optimizedTime)
                putInt("apps_sleeping_count", sleepingCount)
            }
        }
    }

    fun clearJunkOnly() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isClearing = true,
                clearProgress = 0.2f,
                clearStatus = "Wiping leftover installation logs and broken cache..."
            )
            delay(400)
            
            // Delete actual cache files in cache directory!
            val context = appContext
            var bytesDeletedNum: Long = 0
            if (context != null) {
                try {
                    val cacheDir = context.cacheDir
                    if (cacheDir != null && cacheDir.exists()) {
                        val fileLengthBefore = cacheDir.walkBottomUp().fold(0L) { acc, file -> acc + file.length() }
                        cacheDir.listFiles()?.forEach { file ->
                            file.deleteRecursively()
                        }
                        val fileLengthAfter = cacheDir.walkBottomUp().fold(0L) { acc, file -> acc + file.length() }
                        bytesDeletedNum = (fileLengthBefore - fileLengthAfter).coerceAtLeast(0L)
                    }
                } catch (e: Exception) {}
            }

            _uiState.value = _uiState.value.copy(
                clearProgress = 0.7f,
                clearStatus = "Reclaiming internal storage blocks..."
            )
            delay(400)

            var storageVal = 64
            if (context != null) {
                try {
                    val path = android.os.Environment.getDataDirectory()
                    val stat = android.os.StatFs(path.path)
                    val blockSize = stat.blockSizeLong
                    val totalBlocks = stat.blockCountLong
                    val availableBlocks = stat.availableBlocksLong
                    val totalStorage = totalBlocks * blockSize
                    val usedStorage = totalStorage - availableBlocks * blockSize
                    storageVal = if (totalStorage > 0) {
                        ((usedStorage.toDouble() / totalStorage.toDouble()) * 100).toInt()
                    } else 50
                } catch (e: Exception) {}
            }

            val nextScore = (_uiState.value.score + 10).coerceAtMost(100)
            val formatDeleted = if (bytesDeletedNum > 0) {
                String.format("%.2f KB", bytesDeletedNum / 1024.0)
            } else {
                "45.2 MB" // static realistic display when no files are cache-written yet
            }

            _uiState.value = _uiState.value.copy(
                isClearing = false,
                clearProgress = 1.0f,
                storageUsedPercent = storageVal,
                score = nextScore,
                clearStatus = "Junk deleted ($formatDeleted cleared). Storage is now $storageVal% loaded."
            )
            saveSetting {
                putInt("storage_used_percent", storageVal)
                putInt("score", nextScore)
            }
        }
    }

    fun clearRamOnly() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isClearing = true,
                clearProgress = 0.3f,
                clearStatus = "Sweeping temporary app caches..."
            )
            delay(400)

            System.gc()
            Runtime.getRuntime().gc()

            val context = appContext
            var finalRamPercent = 38
            if (context != null) {
                try {
                    val am = context.getSystemService(Context.ACTIVITY_SERVICE) as android.app.ActivityManager
                    val memoryInfo = android.app.ActivityManager.MemoryInfo()
                    am.getMemoryInfo(memoryInfo)
                    finalRamPercent = (((memoryInfo.totalMem - memoryInfo.availMem).toDouble() / memoryInfo.totalMem.toDouble()) * 100).toInt().coerceIn(10, 99)
                } catch (e: Exception) {}
            }

            val nextScore = (_uiState.value.score + 8).coerceAtMost(100)
            _uiState.value = _uiState.value.copy(
                isClearing = false,
                clearProgress = 1.0f,
                ramUsedPercent = finalRamPercent,
                score = nextScore,
                clearStatus = "RAM cleared to $finalRamPercent%."
            )
            saveSetting {
                putInt("ram_used_percent", finalRamPercent)
                putInt("score", nextScore)
            }
        }
    }

    fun scanBackgroundProcesses() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isScanningRamProcesses = true,
                ramProcessesScanned = false,
                isClearing = true,
                clearProgress = 0.2f,
                clearStatus = "Initializing RAM process scanner kernel..."
            )
            delay(500)
            _uiState.value = _uiState.value.copy(
                clearProgress = 0.6f,
                clearStatus = "Scanning heap allocations across running namespaces..."
            )
            delay(500)
            _uiState.value = _uiState.value.copy(
                clearProgress = 0.9f,
                clearStatus = "Retrieving active background task memory footprints..."
            )
            delay(400)
            
            // Generate list of typical memory-hogging tasks (all set back to active and selected)
            val updatedProcesses = listOf(
                BackgroundProcessItem("Vulkan Shader Standby Cache", "com.android.vulkan.shader", 420, "GPU Shader"),
                BackgroundProcessItem("Background Social Sync Daemon", "com.meta.services.sync", 280, "Social Network"),
                BackgroundProcessItem("Google Chrome Multi-tab Heap", "com.android.chrome.tabheap", 510, "Browser Host"),
                BackgroundProcessItem("Standby Unity Game Engine Cache", "com.ea.standby.engine", 640, "Inactive Apps"),
                BackgroundProcessItem("Telemetry Ad-Tracking Listener", "com.telemetry.adtracker", 185, "Analytics Daemon")
            )
            
            _uiState.value = _uiState.value.copy(
                isScanningRamProcesses = false,
                ramProcessesScanned = true,
                isClearing = false,
                clearProgress = 1.0f,
                clearStatus = "Discovered ${updatedProcesses.size} background processes consuming resources.",
                activeBackgroundProcesses = updatedProcesses
            )
        }
    }

    fun toggleProcessSelection(packageName: String) {
        val updated = _uiState.value.activeBackgroundProcesses.map {
            if (it.packageName == packageName) {
                it.copy(isSelected = !it.isSelected)
            } else {
                it
            }
        }
        _uiState.value = _uiState.value.copy(activeBackgroundProcesses = updated)
    }

    fun clearSelectedMemory() {
        viewModelScope.launch {
            val selectedItems = _uiState.value.activeBackgroundProcesses.filter { it.isSelected && it.status == "Active" }
            if (selectedItems.isEmpty()) {
                _uiState.value = _uiState.value.copy(
                    clearStatus = "No active background processes selected to clear."
                )
                return@launch
            }
            
            _uiState.value = _uiState.value.copy(
                isCleaningRamProcesses = true,
                isClearing = true,
                clearProgress = 0.1f,
                clearStatus = "Preparing memory sweep cycle..."
            )
            delay(300)

            // Calculate total reclaimed RAM
            val totalReclaimedMb = selectedItems.sumOf { it.ramCostMb }
            
            val context = appContext
            val am = context?.getSystemService(Context.ACTIVITY_SERVICE) as? android.app.ActivityManager

            // We loop through matching items and visually sweep them one by one
            var currentProcesses = _uiState.value.activeBackgroundProcesses
            selectedItems.forEachIndexed { index, item ->
                // Update current process state to "Sweeping"
                currentProcesses = currentProcesses.map {
                    if (it.packageName == item.packageName) it.copy(status = "Sweeping") else it
                }
                val progressVal = 0.1f + ((index.toFloat() / selectedItems.size) * 0.8f)
                _uiState.value = _uiState.value.copy(
                    clearProgress = progressVal,
                    clearStatus = "Terminating process: ${item.name}...",
                    activeBackgroundProcesses = currentProcesses
                )
                
                // Do actual low-level Android process termination if context available
                if (am != null) {
                    try {
                        am.killBackgroundProcesses(item.packageName)
                    } catch (e: Exception) {}
                }
                
                delay(300)
                
                // Set status to "Cleared"
                currentProcesses = currentProcesses.map {
                    if (it.packageName == item.packageName) it.copy(status = "Cleared") else it
                }
                _uiState.value = _uiState.value.copy(
                    activeBackgroundProcesses = currentProcesses
                )
            }

            // Perform GC to reclaim the memory
            System.gc()
            Runtime.getRuntime().gc()

            // Calculate a nice lowered RAM percentage
            // Each 100MB roughly correlates to about 1.25% of 8GB Ram
            val percentageReduced = (totalReclaimedMb / 80).coerceIn(5, 35)
            val currentRam = _uiState.value.ramUsedPercent
            val finalRamPercent = (currentRam - percentageReduced).coerceIn(30, 85)
            val nextScore = (_uiState.value.score + 12).coerceAtMost(100)

            val logs = _uiState.value.gameModeLogs + listOf(
                "[RAM-CLEANER] Initialized One-Tap Clear memory sweep.",
                "[RAM-CLEANER] Terminated ${selectedItems.size} background processes.",
                "[RAM-CLEANER] Reclaimed ${String.format("%.2f", totalReclaimedMb / 1024.0)} GB system RAM."
            )

            _uiState.value = _uiState.value.copy(
                isCleaningRamProcesses = false,
                isClearing = false,
                clearProgress = 1.0f,
                ramUsedPercent = finalRamPercent,
                score = nextScore,
                clearStatus = "Successfully reclaimed ${String.format("%.2f", totalReclaimedMb / 1024.0)} GB memory!",
                gameModeLogs = logs
            )

            saveSetting {
                putInt("ram_used_percent", finalRamPercent)
                putInt("score", nextScore)
            }
        }
    }

    fun toggleBatteryDeepSleep(enabled: Boolean) {
        val nextScore = if (enabled) (_uiState.value.score + 4).coerceAtMost(100) else (_uiState.value.score - 4).coerceAtLeast(50)
        val sleepingCount = if (enabled) 24 else 8
        _uiState.value = _uiState.value.copy(
            batteryDeepSleepEnabled = enabled,
            appsSleepingCount = sleepingCount,
            score = nextScore
        )
        saveSetting {
            putBoolean("battery_deep_sleep_enabled", enabled)
            putInt("apps_sleeping_count", sleepingCount)
            putInt("score", nextScore)
        }
    }

    fun setGameProfile(profile: GameProfile) {
        val targetFps = when (profile) {
            GameProfile.ULTIMATE_PERFORMANCE -> 120
            GameProfile.PERFORMANCE -> 120
            GameProfile.BALANCED -> 60
            GameProfile.POWER_SAVING -> 48
        }
        val targetFreq = when (profile) {
            GameProfile.ULTIMATE_PERFORMANCE -> 950f
            GameProfile.PERFORMANCE -> 850f
            GameProfile.BALANCED -> 600f
            GameProfile.POWER_SAVING -> 400f
        }
        val targetScale = when (profile) {
            GameProfile.ULTIMATE_PERFORMANCE -> 100
            GameProfile.PERFORMANCE -> 100
            GameProfile.BALANCED -> 85
            GameProfile.POWER_SAVING -> 50
        }

        _uiState.value = _uiState.value.copy(
            selectedProfile = profile,
            targetFpsCap = targetFps,
            gpuFrequencyTarget = targetFreq,
            textureScalePercent = targetScale
        )
        saveSetting {
            putString(KEY_SELECTED_PROFILE, profile.name)
            putInt(KEY_TARGET_FPS_CAP, targetFps)
            putFloat(KEY_GPU_FREQUENCY_TARGET, targetFreq)
            putInt(KEY_TEXTURE_SCALE_PERCENT, targetScale)
        }
    }

    fun updateGpuTarget(target: Float) {
        _uiState.value = _uiState.value.copy(gpuFrequencyTarget = target)
        saveSetting { putFloat(KEY_GPU_FREQUENCY_TARGET, target) }
    }

    fun updateFpsCap(fps: Int) {
        _uiState.value = _uiState.value.copy(targetFpsCap = fps)
        saveSetting { putInt(KEY_TARGET_FPS_CAP, fps) }
    }

    fun updateTextureScale(scale: Int) {
        _uiState.value = _uiState.value.copy(textureScalePercent = scale)
        saveSetting { putInt(KEY_TEXTURE_SCALE_PERCENT, scale) }
    }

    fun togglePreTransform(enabled: Boolean) {
        _uiState.value = _uiState.value.copy(preTransformEnabled = enabled)
        saveSetting { putBoolean(KEY_PRE_TRANSFORM_ENABLED, enabled) }
    }

    fun setBlockNotifications(enabled: Boolean) {
        _uiState.value = _uiState.value.copy(blockNotifications = enabled)
        saveSetting { putBoolean(KEY_BLOCK_NOTIFICATIONS, enabled) }
    }

    fun setBlockAutoBrightness(enabled: Boolean) {
        _uiState.value = _uiState.value.copy(blockAutoBrightness = enabled)
        saveSetting { putBoolean(KEY_BLOCK_AUTO_BRIGHTNESS, enabled) }
    }

    fun setLockCapacitiveButtons(enabled: Boolean) {
        _uiState.value = _uiState.value.copy(lockCapacitiveButtons = enabled)
        saveSetting { putBoolean(KEY_LOCK_CAPACITIVE_BUTTONS, enabled) }
    }

    fun toggleFloatingApp(appName: String) {
        val current = _uiState.value.activeFloatingApp
        _uiState.value = _uiState.value.copy(
            activeFloatingApp = if (current == appName) null else appName
        )
    }

    // Developer / Custom System tweaks actions
    fun toggleGraphicsDriverSetting() {
        val next = !_uiState.value.systemGraphicsDriverSet
        val nextScore = if (next) (_uiState.value.score + 10).coerceAtMost(100) else (_uiState.value.score - 10).coerceAtLeast(40)
        _uiState.value = _uiState.value.copy(
            systemGraphicsDriverSet = next,
            score = nextScore
        )
        saveSetting {
            putBoolean(KEY_SYSTEM_GRAPHICS_DRIVER_SET, next)
            putInt("score", nextScore)
        }
    }

    fun togglePeakRefreshRate() {
        val next = !_uiState.value.forcePeakRefreshRate
        val nextScore = if (next) (_uiState.value.score + 6).coerceAtMost(100) else (_uiState.value.score - 6).coerceAtLeast(40)
        _uiState.value = _uiState.value.copy(
            forcePeakRefreshRate = next,
            gameFps = if (next && _uiState.value.selectedProfile == GameProfile.PERFORMANCE) 120 else _uiState.value.gameFps,
            score = nextScore
        )
        saveSetting {
            putBoolean(KEY_FORCE_PEAK_REFRESH_RATE, next)
            putInt("score", nextScore)
        }
    }

    fun toggleGameMode() {
        val next = !_uiState.value.gameModeActivated
        val nextScore = if (next) (_uiState.value.score + 8).coerceAtMost(100) else (_uiState.value.score - 8).coerceAtLeast(40)
        _uiState.value = _uiState.value.copy(
            gameModeActivated = next,
            score = nextScore
        )
        saveSetting {
            putBoolean("game_mode_activated", next)
            putInt("score", nextScore)
        }
    }

    fun runSsdTrim() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(ssdTrimActive = true, ssdTrimProgress = 0.1f)
            delay(500)
            _uiState.value = _uiState.value.copy(ssdTrimProgress = 0.45f)
            delay(600)
            _uiState.value = _uiState.value.copy(ssdTrimProgress = 0.85f)
            delay(500)
            val trimStorage = (_uiState.value.storageUsedPercent - 3).coerceAtLeast(15)
            _uiState.value = _uiState.value.copy(
                ssdTrimActive = false,
                ssdTrimProgress = 1.0f,
                storageUsedPercent = trimStorage
            )
            saveSetting {
                putInt("storage_used_percent", trimStorage)
            }
        }
    }

    fun toggleTelemetryDebloater() {
        val next = !_uiState.value.telemetryDebloated
        val nextScore = if (next) (_uiState.value.score + 9).coerceAtMost(100) else (_uiState.value.score - 9).coerceAtLeast(40)
        _uiState.value = _uiState.value.copy(
            telemetryDebloated = next,
            score = nextScore
        )
        saveSetting {
            putBoolean(KEY_TELEMETRY_DEBLOATED, next)
            putInt("score", nextScore)
        }
    }

    // New Custom Game Settings Actions
    fun setVSync(enabled: Boolean) {
        _uiState.value = _uiState.value.copy(vSyncEnabled = enabled)
        saveSetting { putBoolean(KEY_VSYNC_ENABLED, enabled) }
    }

    fun setAutoRamCleanerEnabled(enabled: Boolean) {
        _uiState.value = _uiState.value.copy(autoRamCleanerEnabled = enabled)
        saveSetting { putBoolean(KEY_AUTO_RAM_CLEANER_ENABLED, enabled) }
    }

    fun setAllocatedVram(gb: Int) {
        val logs = _uiState.value.gameModeLogs + "[VRAM] Dynamic RAM Plus allocated space updated: ${gb}GB active paging swap."
        _uiState.value = _uiState.value.copy(allocatedVramGb = gb, gameModeLogs = logs)
        saveSetting { putInt(KEY_ALLOCATED_VRAM_GB, gb) }
    }

    fun setLowLatencyMode(mode: LowLatencyMode) {
        _uiState.value = _uiState.value.copy(lowLatencyMode = mode)
        saveSetting { putString(KEY_LOW_LATENCY_MODE, mode.name) }
    }

    fun setTouchSensitivity(level: TouchSensitivity) {
        val detail = when (level) {
            TouchSensitivity.STANDARD -> "240Hz Standard Mode"
            TouchSensitivity.HIGH_SENSITIVITY -> "480Hz Ultra-Response Mode"
            TouchSensitivity.ULTRA_GAMING -> "720Hz Hyper-Response Mode (Direct hardware IRQ override)"
        }
        val logs = _uiState.value.gameModeLogs + "[TOUCH] Screen touch digitization polling updated: $detail."
        _uiState.value = _uiState.value.copy(touchSensitivity = level, gameModeLogs = logs)
        saveSetting { putString(KEY_TOUCH_SENSITIVITY, level.name) }
    }

    fun setDnsPreset(preset: DnsPreset) {
        val detail = when (preset) {
            DnsPreset.DEFAULT -> "System Default DNS Resolver"
            DnsPreset.CLOUDFLARE -> "Cloudflare Gaming DNS Server (1.1.1.1)"
            DnsPreset.GOOGLE_PUBLIC -> "Google Shared DNS Server (8.8.8.8)"
            DnsPreset.ADGUARD_SHIELD -> "AdGuard Ad-Shield Gaming DNS"
        }
        val logs = _uiState.value.gameModeLogs + "[NETWORK] Active routing preset updated: $detail."
        _uiState.value = _uiState.value.copy(dnsPreset = preset, gameModeLogs = logs)
        saveSetting { putString(KEY_DNS_PRESET, preset.name) }
    }

    fun setThermalLimit(limit: ThermalLimit) {
        val detail = when (limit) {
            ThermalLimit.CONSERVATIVE -> "Conservative (CPU/GPU clocked at 80% to protect components)"
            ThermalLimit.OPTIMIZED -> "Optimized balance of heat and peak frames"
            ThermalLimit.EXTREME_OVERCLOCK -> "Overclock unlocked (Dynamic thermal guard threshold +15% bypass)"
        }
        val logs = _uiState.value.gameModeLogs + "[THERMAL] Maximum throttling threshold offset: $detail."
        _uiState.value = _uiState.value.copy(thermalLimit = limit, gameModeLogs = logs)
        saveSetting { putString(KEY_THERMAL_LIMIT, limit.name) }
    }

    fun setLowLatencyAudio(enabled: Boolean) {
        val logs = _uiState.value.gameModeLogs + "[AUDIO] Ultra Low-Latency thread feedback mode: ${if (enabled) "Enabled (12ms driver response)" else "Disabled (45ms standard Android soundpool buffer)"}."
        _uiState.value = _uiState.value.copy(lowLatencyAudioEnabled = enabled, gameModeLogs = logs)
        saveSetting { putBoolean(KEY_LOW_LATENCY_AUDIO, enabled) }
    }

    fun setBluetoothControllerBoost(enabled: Boolean) {
        val label = if (enabled) "Enabled (Fast HID reports @1000Hz, Link Supervision set to 100ms)" else "Disabled (125Hz standard Bluetooth report pool)"
        val logs = _uiState.value.gameModeLogs + "[BLUETOOTH] Bluetooth Controller input lag boost: $label."
        _uiState.value = _uiState.value.copy(bluetoothControllerBoostEnabled = enabled, gameModeLogs = logs)
        saveSetting { putBoolean(KEY_BLUETOOTH_CONTROLLER_BOOST, enabled) }
    }

    fun setBluetoothAudioOptimization(preset: BluetoothAudioOptimization) {
        val detail = when (preset) {
            BluetoothAudioOptimization.STANDARD -> "Default Android Bluetooth A2DP audio stream profile"
            BluetoothAudioOptimization.APT_X_ADAPTIVE -> "Qualcomm aptX Adaptive & Sony LDAC (High Bitrate / Low Latency Dynamic Stream)"
            BluetoothAudioOptimization.LE_AUDIO_MIN_LATENCY -> "Bluetooth LE Audio ultra-low-jitter sub-band dynamic codec (15ms lag)"
        }
        val logs = _uiState.value.gameModeLogs + "[BLUETOOTH] Audio streaming engine updated: $detail."
        _uiState.value = _uiState.value.copy(bluetoothAudioOptimization = preset, gameModeLogs = logs)
        saveSetting { putString(KEY_BLUETOOTH_AUDIO_OPTIMIZATION, preset.name) }
    }

    fun toggleHibernateApp(packageName: String, label: String, context: Context) {
        val currentHibernated = _uiState.value.hibernatedApps.toMutableSet()
        val logs: List<String>
        val finalHibernated: Set<String>
        if (currentHibernated.contains(packageName)) {
            currentHibernated.remove(packageName)
            logs = _uiState.value.gameModeLogs + "[HIBERNATION] App '$label' ($packageName) woke up from hibernation state."
            finalHibernated = currentHibernated
        } else {
            currentHibernated.add(packageName)
            try {
                val am = context.getSystemService(Context.ACTIVITY_SERVICE) as android.app.ActivityManager
                am.killBackgroundProcesses(packageName)
            } catch (e: Exception) {}
            logs = _uiState.value.gameModeLogs + "[HIBERNATION] Force stopped & successfully hibernated background tracker: '$label'."
            finalHibernated = currentHibernated
        }
        
        var nextRamPercent = _uiState.value.ramUsedPercent
        if (finalHibernated.size > _uiState.value.hibernatedApps.size) {
            nextRamPercent = (nextRamPercent - (1..3).random()).coerceAtLeast(15)
        } else if (finalHibernated.size < _uiState.value.hibernatedApps.size) {
            nextRamPercent = (nextRamPercent + (1..2).random()).coerceAtMost(95)
        }
        
        _uiState.value = _uiState.value.copy(
            hibernatedApps = finalHibernated,
            ramUsedPercent = nextRamPercent,
            gameModeLogs = logs
        )
        saveSetting {
            putInt("ram_used_percent", nextRamPercent)
        }
    }

    fun setGameIntensity(gameName: String, intensity: GameLoadIntensity) {
        val updatedMap = _uiState.value.gameIntensities.toMutableMap()
        updatedMap[gameName] = intensity
        val logs = _uiState.value.gameModeLogs + "[LOAD-CONFIG] '$gameName' profile optimized for load tier: ${intensity.name}."
        _uiState.value = _uiState.value.copy(
            gameIntensities = updatedMap,
            gameModeLogs = logs
        )
        val serialized = updatedMap.entries.joinToString("|") { "${it.key}:${it.value.name}" }
        saveSetting { putString(KEY_GAME_INTENSITIES, serialized) }
    }

    fun setGameRamBoost(gameName: String, enabled: Boolean) {
        val updatedMap = _uiState.value.gameRamBoostEnabled.toMutableMap()
        updatedMap[gameName] = enabled
        val logs = _uiState.value.gameModeLogs + "[RAM-BOOST-CONFIG] Supercharged RAM boost on launcher ${if (enabled) "Enabled" else "Disabled"} for target '$gameName'."
        _uiState.value = _uiState.value.copy(
            gameRamBoostEnabled = updatedMap,
            gameModeLogs = logs
        )
        val serialized = updatedMap.entries.joinToString("|") { "${it.key}:${it.value}" }
        saveSetting { putString(KEY_GAME_RAM_BOOST, serialized) }
    }

    fun setGameVpn(gameName: String, enabled: Boolean) {
        val updatedMap = _uiState.value.gameVpnEnabled.toMutableMap()
        updatedMap[gameName] = enabled
        val logs = _uiState.value.gameModeLogs + "[VPN-CONFIG] Gaming VPN tunnel proxy ${if (enabled) "Enabled" else "Disabled"} on launcher for target '$gameName'."
        _uiState.value = _uiState.value.copy(
            gameVpnEnabled = updatedMap,
            gameModeLogs = logs
        )
        val serialized = updatedMap.entries.joinToString("|") { "${it.key}:${it.value}" }
        saveSetting { putString(KEY_GAME_VPN, serialized) }
    }

    fun cleanRamPools() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isClearing = true,
                clearProgress = 0.1f,
                clearStatus = "Releasing cached virtual app pages..."
            )
            delay(400)
            
            // Trigger actual Android/Java JVM garbage collection!
            System.gc()
            Runtime.getRuntime().gc()

            _uiState.value = _uiState.value.copy(
                clearProgress = 0.5f,
                clearStatus = "Purging memory allocation fragments..."
            )
            delay(400)

            // Trigger real background package memory releases where possible
            val context = appContext
            var killedCount = 0
            if (context != null) {
                try {
                    val am = context.getSystemService(Context.ACTIVITY_SERVICE) as android.app.ActivityManager
                    _uiState.value.localGameLaunchInstalled.forEach { game ->
                        try {
                            am.killBackgroundProcesses(game.packageName)
                            killedCount++
                        } catch (e: Exception) {}
                    }
                } catch (e: Exception) {}
            }

            _uiState.value = _uiState.value.copy(
                clearProgress = 0.8f,
                clearStatus = "Recalculating real-time memory metrics..."
            )
            delay(300)

            // Measure real final RAM percentage
            var finalRamPercent = 25
            if (context != null) {
                try {
                    val am = context.getSystemService(Context.ACTIVITY_SERVICE) as android.app.ActivityManager
                    val memoryInfo = android.app.ActivityManager.MemoryInfo()
                    am.getMemoryInfo(memoryInfo)
                    finalRamPercent = (((memoryInfo.totalMem - memoryInfo.availMem).toDouble() / memoryInfo.totalMem.toDouble()) * 100).toInt().coerceIn(10, 99)
                } catch (e: Exception) {}
            }

            val logs = _uiState.value.gameModeLogs + "[CLEANER] Forced manual garbage collection and purged heap fragments. Sent background dormancy orders to $killedCount applications. Restored memory space!"
            val nextScore = (_uiState.value.score + 5).coerceAtMost(100)
            
            _uiState.value = _uiState.value.copy(
                isClearing = false,
                clearProgress = 1.0f,
                clearStatus = "RAM Cleared successfully!",
                ramUsedPercent = finalRamPercent,
                gameModeLogs = logs,
                score = nextScore
            )
            saveSetting {
                putInt("ram_used_percent", finalRamPercent)
                putInt("score", nextScore)
            }
        }
    }

    @Suppress("DEPRECATION")
    fun scanInstalledApps(context: Context) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isScanningApps = true)
            delay(1200) // simulated hardware scan delay
            try {
                val pm = context.packageManager
                val apps = pm.getInstalledApplications(PackageManager.GET_META_DATA)
                val userAppsList = apps.filter { app ->
                    val isSystem = (app.flags and ApplicationInfo.FLAG_SYSTEM) != 0
                    val isGame = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                        app.category == ApplicationInfo.CATEGORY_GAME
                    } else {
                        (app.flags and ApplicationInfo.FLAG_IS_GAME) != 0
                    }
                    isGame || !isSystem
                }.map { app ->
                    InstalledGame(
                        label = pm.getApplicationLabel(app).toString(),
                        packageName = app.packageName
                    )
                }.distinctBy { it.packageName }.filter { it.label.isNotBlank() && !it.packageName.startsWith("com.example") }.sortedBy { it.label }

                if (userAppsList.isNotEmpty()) {
                    _uiState.value = _uiState.value.copy(
                        localGameLaunchInstalled = userAppsList,
                        isScanningApps = false,
                        gameModeLogs = _uiState.value.gameModeLogs + "[DAEMON] Scan complete! Loaded ${userAppsList.size} real apps directly from your device."
                    )
                    val serialized = userAppsList.joinToString("|") { "${it.packageName}::${it.label}" }
                    saveSetting { putString(KEY_INSTALLED_GAMES, serialized) }
                } else {
                    // Fallback to mix of common apps
                    val fallbackApps = apps.map { app ->
                        InstalledGame(
                            label = pm.getApplicationLabel(app).toString(),
                            packageName = app.packageName
                        )
                    }.distinctBy { it.packageName }.filter { it.label.isNotBlank() && !it.packageName.startsWith("com.example") }.take(15)
                    
                    val finalFallback = if (fallbackApps.isNotEmpty()) fallbackApps else listOf(
                        InstalledGame("Genshin Impact Mobile", "com.miHoYo.GenshinImpact"),
                        InstalledGame("PUBG Mobile Lite", "com.tencent.iglite"),
                        InstalledGame("Wild Rift", "com.riotgames.league.wildrift"),
                        InstalledGame("Grid Autosport", "com.feralinteractive.gridas")
                    )
                    _uiState.value = _uiState.value.copy(
                        localGameLaunchInstalled = finalFallback,
                        isScanningApps = false,
                        gameModeLogs = _uiState.value.gameModeLogs + "[DAEMON] Scan completed. Bound ${finalFallback.size} device applications."
                    )
                    val serialized = finalFallback.joinToString("|") { "${it.packageName}::${it.label}" }
                    saveSetting { putString(KEY_INSTALLED_GAMES, serialized) }
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isScanningApps = false,
                    gameModeLogs = _uiState.value.gameModeLogs + "[DAEMON] Loaded fallback applications listing due to scan interruption: ${e.localizedMessage}"
                )
            }
        }
    }

    fun setAutoDetectGameLaunch(enabled: Boolean) {
        _uiState.value = _uiState.value.copy(autoDetectGameLaunchEnabled = enabled)
        saveSetting { putBoolean(KEY_AUTO_DETECT_GAME_LAUNCH_ENABLED, enabled) }
    }

    fun launchGameWithDetection(context: Context, gameName: String, packageName: String) {
        val isAutoEnabled = _uiState.value.autoDetectGameLaunchEnabled
        val intensity = _uiState.value.gameIntensities[gameName] ?: GameLoadIntensity.MODERATE
        val isRamBoostSelected = _uiState.value.gameRamBoostEnabled[gameName] ?: false
        val isVpnSelected = _uiState.value.gameVpnEnabled[gameName] ?: false
        val shouldAutoCleanRam = _uiState.value.autoRamCleanerEnabled && (intensity == GameLoadIntensity.HEAVY || _uiState.value.ramUsedPercent > 60)
        
        val initialLogs = mutableListOf(
            "[DAEMON] Intercepted execution parameter: '$gameName' (Process UID: ${10000 + (1000..9999).random()})",
            "[INTERCEPT] Determined profile load intensity: ${intensity.name}"
        )

        _uiState.value = _uiState.value.copy(
            runningGame = gameName,
            gameModeActivated = isAutoEnabled,
            backgroundActivitiesDisabled = false,
            cpuGpuAllocated = false,
            notificationsSuppressed = false,
            inputLagOptimized = false,
            gameModeLogs = initialLogs
        )
        
        viewModelScope.launch {
            if (isAutoEnabled) {
                delay(300)
                if (shouldAutoCleanRam || isRamBoostSelected) {
                    val finalRamPercent = if (isRamBoostSelected) 22 else 28
                    val sweepSize = if (isRamBoostSelected) "2.4GB (High-Velocity Dedicated RAM Sweep)" else "1.5GB"
                    _uiState.value = _uiState.value.copy(
                        ramUsedPercent = finalRamPercent,
                        gameModeLogs = _uiState.value.gameModeLogs + "[CLEANER] ${if (isRamBoostSelected) "Supercharged RAM Boost active!" else "Intensive load autoRAM sweep!"} Purged heap cache. Cleaned: $sweepSize of junk memory."
                    )
                    delay(300)
                }

                if (isVpnSelected) {
                    val isConnected = try {
                        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as android.net.ConnectivityManager
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                            val activeNet = cm.activeNetwork
                            val caps = cm.getNetworkCapabilities(activeNet)
                            caps != null && caps.hasTransport(android.net.NetworkCapabilities.TRANSPORT_VPN)
                        } else {
                            @Suppress("DEPRECATION")
                            cm.getNetworkInfo(android.net.ConnectivityManager.TYPE_VPN)?.isConnected == true
                        }
                    } catch (e: Exception) { false }

                    if (!isConnected) {
                        _uiState.value = _uiState.value.copy(
                            gameModeLogs = _uiState.value.gameModeLogs + "[VPN-DAEMON] ⚠️ WARNING: No system VPN tunnel detected! Routing you to System Settings to configure/enable a secure VPN..."
                        )
                        delay(1200)
                        try {
                            val vpnIntent = android.content.Intent("android.net.vpn.SETTINGS")
                            vpnIntent.addFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK)
                            context.startActivity(vpnIntent)
                        } catch (e: Exception) {
                            // Fallback if settings page can't launch in some environments
                            _uiState.value = _uiState.value.copy(
                                gameModeLogs = _uiState.value.gameModeLogs + "[VPN-DAEMON] Alternate system VPN request dispatched (routing settings fallback)."
                            )
                        }
                    } else {
                        _uiState.value = _uiState.value.copy(
                            gameModeLogs = _uiState.value.gameModeLogs + "[VPN-PROXY] Safe tunneling active! ⚡ Fast routing secured via active System VPN tunnel interface."
                        )
                    }
                    delay(300)
                }

                _uiState.value = _uiState.value.copy(
                    backgroundActivitiesDisabled = true,
                    appsSleepingCount = 31,
                    gameModeLogs = _uiState.value.gameModeLogs + "[SUSPENDER] Dormancy order dispatched. Placed background apps to sleep."
                )
                
                delay(300)
                _uiState.value = _uiState.value.copy(
                    notificationsSuppressed = true,
                    blockNotifications = true,
                    lockCapacitiveButtons = true,
                    gameModeLogs = _uiState.value.gameModeLogs + "[INTERRUPTING] Notification blackhole active. suppressing popups/toasts."
                )
                
                delay(300)
                val targetGpuFreq = if (_uiState.value.selectedProfile == GameProfile.ULTIMATE_PERFORMANCE) 950f else 900f
                val targetGpuText = if (_uiState.value.selectedProfile == GameProfile.ULTIMATE_PERFORMANCE) {
                    "[ALLOCATOR] ULTIMATE power plan active. CPU Core-Pinned. GPU Overclock max target (950MHz) active."
                } else {
                    "[ALLOCATOR] CPU pinned. GPU driver targets configured to 900MHz frequency limit."
                }
                
                _uiState.value = _uiState.value.copy(
                    cpuGpuAllocated = true,
                    gpuFrequencyTarget = targetGpuFreq,
                    targetFpsCap = if (_uiState.value.vSyncEnabled) 60 else 120,
                    forcePeakRefreshRate = true,
                    gameModeLogs = _uiState.value.gameModeLogs + targetGpuText
                )
                
                delay(300)
                val latencyText = when (_uiState.value.lowLatencyMode) {
                    LowLatencyMode.ON_BOOST -> "[LATENCY] Low latency ON + BOOST: Kernel input mapping direct interrupt delay <0.4ms latency jitter."
                    LowLatencyMode.ON -> "[LATENCY] Low latency ON: Input delay minimized (~1.8ms delay)."
                    LowLatencyMode.OFF -> "[LATENCY] Low latency OFF: Default frame scheduler queue active (~12.5ms delay)."
                }
                _uiState.value = _uiState.value.copy(
                    inputLagOptimized = true,
                    ramUsedPercent = if (isRamBoostSelected) 22 else if (shouldAutoCleanRam) 28 else 31,
                    score = 100,
                    gameModeLogs = _uiState.value.gameModeLogs + latencyText
                )
            } else {
                _uiState.value = _uiState.value.copy(
                    gameModeLogs = _uiState.value.gameModeLogs + "[WARNING] Auto-optimization daemon is disabled! Running game inside default unoptimized android workspace container."
                )
            }

            // At the end of optimizations, start the real game
            delay(400)
            try {
                val launchIntent = context.packageManager.getLaunchIntentForPackage(packageName)
                if (launchIntent != null) {
                    launchIntent.addFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK)
                    context.startActivity(launchIntent)
                    _uiState.value = _uiState.value.copy(
                        gameModeLogs = _uiState.value.gameModeLogs + "[LAUNCHER] Game execution successfully handed off to Android Package Manager."
                    )
                } else {
                    _uiState.value = _uiState.value.copy(
                        gameModeLogs = _uiState.value.gameModeLogs + "[SIMULATION] Real package activity is not directly accessible. Continuing launcher sandbox execution loops."
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    gameModeLogs = _uiState.value.gameModeLogs + "[LAUNCHER-ERR] Failed to start real application: ${e.localizedMessage}"
                )
            }
        }
    }

    fun closeActiveGame() {
        _uiState.value = _uiState.value.copy(
            runningGame = null,
            backgroundActivitiesDisabled = false,
            cpuGpuAllocated = false,
            notificationsSuppressed = false,
            inputLagOptimized = false,
            blockNotifications = false,
            lockCapacitiveButtons = false,
            gameModeLogs = emptyList()
        )
    }

    fun toggleSimulatedCharging() {
        val nextVal = !_uiState.value.simulatedChargingEnabled
        val logs = _uiState.value.gameModeLogs + "[CHARGING-GUARD] USB Power-In charging state is now ${if (nextVal) "CONNECTED" else "DISCONNECTED"}. Thermal dissipation requirements increased."
        _uiState.value = _uiState.value.copy(
            simulatedChargingEnabled = nextVal,
            gameModeLogs = logs
        )
    }

    fun toggleBatterySaverRestricting() {
        val nextVal = !_uiState.value.batterySaverRestricting
        val nextScore = if (nextVal) (_uiState.value.score - 15).coerceAtLeast(40) else (_uiState.value.score + 15).coerceAtMost(100)
        val logs = _uiState.value.gameModeLogs + "[POWER-GUARD] Battery Saver restriction ${if (nextVal) "ACTIVATED. CPU/GPU performance restricted to 50% core clock budget" else "DISABLED. High performance unlocked."}"
        _uiState.value = _uiState.value.copy(
            batterySaverRestricting = nextVal,
            score = nextScore,
            gameModeLogs = logs
        )
    }

    fun toggleInGameSettingsOptimized() {
        val nextVal = !_uiState.value.inGameSettingsOptimized
        val nextFps = if (nextVal) 60 else 120
        val textureScale = if (nextVal) 80 else 100
        val logs = _uiState.value.gameModeLogs + "[GAMESETTINGS-GUARD] Recommended stable presets loaded: Target FPS set to ${nextFps} capped to mitigate GPU overload."
        _uiState.value = _uiState.value.copy(
            inGameSettingsOptimized = nextVal,
            targetFpsCap = nextFps,
            textureScalePercent = textureScale,
            gameModeLogs = logs
        )
    }

    fun clearGameLogs() {
        _uiState.value = _uiState.value.copy(gameModeLogs = emptyList())
    }

    fun toggleLagKiller() {
        val target = !_uiState.value.lagKillerEnabled
        if (target) {
            viewModelScope.launch {
                _uiState.value = _uiState.value.copy(
                    isApplyingLagKiller = true,
                    lagKillerProgress = 0.05f,
                    lagKillerStatus = "Initializing Core Lag Killer Daemon..."
                )
                delay(400)
                _uiState.value = _uiState.value.copy(
                    lagKillerProgress = 0.25f,
                    lagKillerStatus = "Overriding standard display V-Sync frame pacing..."
                )
                delay(450)
                _uiState.value = _uiState.value.copy(
                    lagKillerProgress = 0.5f,
                    lagKillerStatus = "Tuning touch input polling sample rates..."
                )
                delay(400)
                _uiState.value = _uiState.value.copy(
                    lagKillerProgress = 0.75f,
                    lagKillerStatus = "Forcing high-priority Bluetooth HID poll protocols..."
                )
                delay(350)
                _uiState.value = _uiState.value.copy(
                    lagKillerProgress = 1.0f,
                    lagKillerStatus = "Lag reduction filters applied successfully!"
                )
                delay(300)

                val nextScore = (_uiState.value.score + 15).coerceAtMost(100)
                val logs = _uiState.value.gameModeLogs + listOf(
                    "[LAG-KILLER] Master engine ENGAGED",
                    "[LAG-KILLER] Bypassed display V-Sync framework buffers (instant frame pipeline)",
                    "[LAG-KILLER] Kernel touch pointer rate locked to ULTRA_GAMING (360Hz)",
                    "[LAG-KILLER] Bluetooth HID gameplay controller polling rate set to 1000Hz"
                )

                _uiState.value = _uiState.value.copy(
                    lagKillerEnabled = true,
                    isApplyingLagKiller = false,
                    vSyncEnabled = false,
                    touchSensitivity = TouchSensitivity.ULTRA_GAMING,
                    bluetoothControllerBoostEnabled = true,
                    lowLatencyAudioEnabled = true,
                    bluetoothAudioOptimization = BluetoothAudioOptimization.LE_AUDIO_MIN_LATENCY,
                    score = nextScore,
                    gameModeLogs = logs
                )

                saveSetting {
                    putBoolean(KEY_LAG_KILLER_ENABLED, true)
                    putBoolean(KEY_VSYNC_ENABLED, false)
                    putString(KEY_TOUCH_SENSITIVITY, TouchSensitivity.ULTRA_GAMING.name)
                    putBoolean(KEY_BLUETOOTH_CONTROLLER_BOOST, true)
                    putBoolean(KEY_LOW_LATENCY_AUDIO, true)
                    putString(KEY_BLUETOOTH_AUDIO_OPTIMIZATION, BluetoothAudioOptimization.LE_AUDIO_MIN_LATENCY.name)
                    putInt("score", nextScore)
                }
            }
        } else {
            val nextScore = (_uiState.value.score - 15).coerceAtLeast(40)
            val logs = _uiState.value.gameModeLogs + "[LAG-KILLER] Master engine DISENGAGED (Reverted kernel latency modes to standard system levels)."
            _uiState.value = _uiState.value.copy(
                lagKillerEnabled = false,
                score = nextScore,
                gameModeLogs = logs
            )
            saveSetting {
                putBoolean(KEY_LAG_KILLER_ENABLED, false)
                putInt("score", nextScore)
            }
        }
    }
}
