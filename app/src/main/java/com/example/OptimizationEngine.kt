package com.example

import android.content.Context
import android.os.Build
import android.os.Process
import android.os.PerformanceHintManager
import android.view.WindowManager
import java.io.DataOutputStream

/**
 * Core Optimization Engine for low-level system properties, refresh rate enforcement,
 * RAM Plus swap toggling, background logging overhead stripping, and native ADPF hint sessions.
 */
class OptimizationEngine(private val context: Context) {

    /**
     * Executes shell commands to apply low-level system properties.
     * Gracefully falls back if root access is unavailable.
     */
    fun executeShellCommands(commands: List<String>): Boolean {
        return try {
            val process = Runtime.getRuntime().exec("su")
            val outputStream = DataOutputStream(process.outputStream)
            
            for (command in commands) {
                outputStream.writeBytes("$command\n")
            }
            outputStream.writeBytes("exit\n")
            outputStream.flush()
            process.waitFor() == 0
        } catch (e: Exception) {
            false
        }
    }

    /**
     * Forces the hardware display to stay locked at its highest refresh rate (e.g., 120Hz).
     */
    fun forceDisplayFreshness(windowManager: WindowManager? = null) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val display = context.display
            val maxRefreshRate = display?.supportedModes?.maxByOrNull { it.refreshRate }?.refreshRate ?: 120f
            
            val commands = listOf(
                "settings put system peak_refresh_rate $maxRefreshRate",
                "settings put system min_refresh_rate $maxRefreshRate"
            )
            executeShellCommands(commands)
        }
    }

    /**
     * Disables dynamic Android RAM Plus swapping to prioritize pure high-speed physical DRAM.
     */
    fun toggleRamPlus(enable: Boolean): Boolean {
        val size = if (enable) "4096" else "0" // 0 disables virtual swap
        return executeShellCommands(listOf(
            "setprop persist.sys.zram_enabled ${if (enable) "1" else "0"}",
            "settings put global ram_expand_size_list $size"
        ))
    }

    /**
     * Strips system-wide I/O background loops by killing non-essential tracing.
     */
    fun stripLoggingOverhead(): Boolean {
        val commands = listOf(
            "setprop log.tag.all s", // Set all log visibility to Silent
            "stop logd",            // Kill the primary logging daemon
            "setprop persist.logd.size 0"
        )
        return executeShellCommands(commands)
    }

    /**
     * Deploys native Android Dynamic Performance Framework (ADPF) hints for thread priority pinning.
     */
    fun targetAdpfSession(tids: IntArray = intArrayOf(Process.myPid(), Process.myTid()), targetFrameDurationNanos: Long = 8333333L) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val hintManager = context.getSystemService(Context.PERFORMANCE_HINT_SERVICE) as? PerformanceHintManager
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && hintManager != null) {
                try {
                    val session = hintManager.createHintSession(tids, targetFrameDurationNanos)
                    session?.reportActualWorkDuration(targetFrameDurationNanos - 1000000L)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }
}
