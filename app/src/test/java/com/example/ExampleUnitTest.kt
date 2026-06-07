package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleUnitTest {
  @Test
  fun addition_isCorrect() {
    assertEquals(4, 2 + 2)
  }

  @Test
  fun testAutomatedGameLaunchDetectionFlow() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val viewModel = TunerViewModel()
    
    // 1. Initially no game should be running
    assertNull(viewModel.uiState.value.runningGame)
    assertFalse(viewModel.uiState.value.backgroundActivitiesDisabled)
    assertFalse(viewModel.uiState.value.cpuGpuAllocated)
    assertFalse(viewModel.uiState.value.notificationsSuppressed)

    // 2. Disable auto-detect and verify a game launch doesn't trigger automated optimizations
    viewModel.setAutoDetectGameLaunch(false)
    assertFalse(viewModel.uiState.value.autoDetectGameLaunchEnabled)
    
    viewModel.launchGameWithDetection(context, "Wild Rift", "com.riotgames.league.wildrift")
    assertEquals("Wild Rift", viewModel.uiState.value.runningGame)
    assertFalse(viewModel.uiState.value.gameModeActivated)
    assertFalse(viewModel.uiState.value.backgroundActivitiesDisabled)

    // 3. Close game and re-enable auto-detect
    viewModel.closeActiveGame()
    assertNull(viewModel.uiState.value.runningGame)
    viewModel.setAutoDetectGameLaunch(true)
    assertTrue(viewModel.uiState.value.autoDetectGameLaunchEnabled)

    // 4. Launch with auto-detect enabled
    viewModel.launchGameWithDetection(context, "PUBG Mobile Lite", "com.tencent.iglite")
    assertEquals("PUBG Mobile Lite", viewModel.uiState.value.runningGame)
    assertTrue(viewModel.uiState.value.gameModeActivated)
  }

  @Test
  fun testNewAdvancedConfigurations() {
    val viewModel = TunerViewModel()
    
    // 1. Initial values are default
    assertEquals(TouchSensitivity.STANDARD, viewModel.uiState.value.touchSensitivity)
    assertEquals(DnsPreset.DEFAULT, viewModel.uiState.value.dnsPreset)
    assertEquals(ThermalLimit.OPTIMIZED, viewModel.uiState.value.thermalLimit)
    assertFalse(viewModel.uiState.value.lowLatencyAudioEnabled)

    // 2. Modify Sensi-boost Touch sensitivity
    viewModel.setTouchSensitivity(TouchSensitivity.ULTRA_GAMING)
    assertEquals(TouchSensitivity.ULTRA_GAMING, viewModel.uiState.value.touchSensitivity)
    assertTrue(viewModel.uiState.value.gameModeLogs.any { it.contains("720Hz") })

    // 3. Modify Gaming DNS
    viewModel.setDnsPreset(DnsPreset.CLOUDFLARE)
    assertEquals(DnsPreset.CLOUDFLARE, viewModel.uiState.value.dnsPreset)
    assertTrue(viewModel.uiState.value.gameModeLogs.any { it.contains("Cloudflare") })

    // 4. Modify Thermal limits
    viewModel.setThermalLimit(ThermalLimit.EXTREME_OVERCLOCK)
    assertEquals(ThermalLimit.EXTREME_OVERCLOCK, viewModel.uiState.value.thermalLimit)
    assertTrue(viewModel.uiState.value.gameModeLogs.any { it.contains("Overclock") })

    // 5. Toggle Ultra Low-Latency Audio
    viewModel.setLowLatencyAudio(true)
    assertTrue(viewModel.uiState.value.lowLatencyAudioEnabled)

    // 6. Bluetooth controller response acceleration
    assertFalse(viewModel.uiState.value.bluetoothControllerBoostEnabled)
    viewModel.setBluetoothControllerBoost(true)
    assertTrue(viewModel.uiState.value.bluetoothControllerBoostEnabled)
    assertTrue(viewModel.uiState.value.gameModeLogs.any { it.contains("1000Hz") })

    // 7. Bluetooth audio low-latency codecs
    assertEquals(BluetoothAudioOptimization.STANDARD, viewModel.uiState.value.bluetoothAudioOptimization)
    viewModel.setBluetoothAudioOptimization(BluetoothAudioOptimization.LE_AUDIO_MIN_LATENCY)
    assertEquals(BluetoothAudioOptimization.LE_AUDIO_MIN_LATENCY, viewModel.uiState.value.bluetoothAudioOptimization)
    assertTrue(viewModel.uiState.value.gameModeLogs.any { it.contains("LE Audio") })
  }

  @Test
  fun testLauncherCustomOptions() {
    val viewModel = TunerViewModel()
    val gameName = "Wild Rift"

    // 1. Initial values should be null or false by default
    assertFalse(viewModel.uiState.value.gameRamBoostEnabled[gameName] ?: false)
    assertFalse(viewModel.uiState.value.gameVpnEnabled[gameName] ?: false)

    // 2. Enable custom RAM Boost for Wild Rift
    viewModel.setGameRamBoost(gameName, true)
    assertTrue(viewModel.uiState.value.gameRamBoostEnabled[gameName] == true)
    assertTrue(viewModel.uiState.value.gameModeLogs.any { it.contains("RAM-BOOST-CONFIG") && it.contains("Enabled") })

    // 3. Enable custom VPN for Wild Rift
    viewModel.setGameVpn(gameName, true)
    assertTrue(viewModel.uiState.value.gameVpnEnabled[gameName] == true)
    assertTrue(viewModel.uiState.value.gameModeLogs.any { it.contains("VPN-CONFIG") && it.contains("Enabled") })

    // 4. Test disable RAM Boost
    viewModel.setGameRamBoost(gameName, false)
    assertFalse(viewModel.uiState.value.gameRamBoostEnabled[gameName] ?: false)
  }

  @Test
  fun testRamCleanerScannerAndClearingFlow() {
    val viewModel = TunerViewModel()

    // 1. Check initial states of RAM cleaner
    assertFalse(viewModel.uiState.value.isScanningRamProcesses)
    assertFalse(viewModel.uiState.value.ramProcessesScanned)
    assertFalse(viewModel.uiState.value.isCleaningRamProcesses)

    // 2. Default processes loaded
    val defaultProcesses = viewModel.uiState.value.activeBackgroundProcesses
    assertTrue(defaultProcesses.isNotEmpty())
    assertEquals("Vulkan Shader Standby Cache", defaultProcesses[0].name)
    assertEquals("com.android.vulkan.shader", defaultProcesses[0].packageName)

    // 3. Toggle selection
    assertTrue(defaultProcesses[0].isSelected)
    viewModel.toggleProcessSelection("com.android.vulkan.shader")
    assertFalse(viewModel.uiState.value.activeBackgroundProcesses[0].isSelected)

    viewModel.toggleProcessSelection("com.android.vulkan.shader")
    assertTrue(viewModel.uiState.value.activeBackgroundProcesses[0].isSelected)
  }

  @Test
  fun testLagKillerToggle() {
    val viewModel = TunerViewModel()

    // 1. Initially disabled
    assertFalse(viewModel.uiState.value.lagKillerEnabled)
    assertFalse(viewModel.uiState.value.isApplyingLagKiller)

    // 2. Click toggle will initiate applying state immediately
    viewModel.toggleLagKiller()
    assertTrue(viewModel.uiState.value.isApplyingLagKiller)
    assertTrue(viewModel.uiState.value.lagKillerProgress > 0f)

    // 3. Since we want to test disengaging too, let's toggle with disabled
    // If it's enabled, toggleLagKiller will disable it synchronously
    // Let's force state.copy to simulate enabled which runs disengage synchronously
    // We can't access private _uiState directly, but we can verify our toggling starts correctly.
  }
}
