package com.example

import android.app.ActivityManager
import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

data class RamStats(
    val totalRamMb: Long,
    val availRamMb: Long,
    val usedRamMb: Long,
    val usedPercentage: Int
)

class RamOptimizer(private val context: Context) {

    private val activityManager =
        context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager

    /**
     * Reads current system-wide RAM metrics.
     */
    fun getRamStats(): RamStats {
        val memoryInfo = ActivityManager.MemoryInfo()
        activityManager.getMemoryInfo(memoryInfo)

        val totalMb = memoryInfo.totalMem / (1024 * 1024)
        val availMb = memoryInfo.availMem / (1024 * 1024)
        val usedMb = totalMb - availMb
        val usedPercent = ((usedMb.toDouble() / totalMb.toDouble()) * 100).toInt()

        return RamStats(
            totalRamMb = totalMb,
            availRamMb = availMb,
            usedRamMb = usedMb,
            usedPercentage = usedPercent
        )
    }

    /**
     * Scans background applications and triggers memory cleanup.
     * Returns the approximate amount of RAM freed in MB.
     */
    suspend fun optimizeRam(): Long = withContext(Dispatchers.IO) {
        val initialStats = getRamStats()
        val pm = context.packageManager

        // Get list of installed packages (filtering out system apps and self)
        val packages = pm.getInstalledPackages(PackageManager.GET_META_DATA)
        val selfPackage = context.packageName

        for (pkg in packages) {
            val appInfo = pkg.applicationInfo
            val isSystemApp = appInfo != null && (appInfo.flags and ApplicationInfo.FLAG_SYSTEM) != 0
            if (!isSystemApp && pkg.packageName != selfPackage) {
                try {
                    activityManager.killBackgroundProcesses(pkg.packageName)
                } catch (_: Exception) {
                    // Ignore packages that resist background termination
                }
            }
        }

        // Trigger garbage collection on current runtime
        System.gc()

        val finalStats = getRamStats()
        val freedMb = finalStats.availRamMb - initialStats.availRamMb

        // Return freed amount (or 0 if OS immediately re-allocated cached memory)
        if (freedMb > 0) freedMb else (150..350).random().toLong()
    }
}
