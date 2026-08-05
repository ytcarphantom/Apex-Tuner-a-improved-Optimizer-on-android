package com.example

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

@Composable
fun RamOptimizerScreen() {
    val context = LocalContext.current
    val ramOptimizer = remember { RamOptimizer(context) }
    val scope = rememberCoroutineScope()

    var ramStats by remember { mutableStateOf(ramOptimizer.getRamStats()) }
    var isOptimizing by remember { mutableStateOf(false) }
    var statusMessage by remember { mutableStateOf("System Ready") }

    val animatedProgress by animateFloatAsState(
        targetValue = ramStats.usedPercentage / 100f,
        animationSpec = tween(durationMillis = 800),
        label = "RamProgress"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .testTag("card_ram_optimizer_module"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "RAM OPTIMIZER",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Usage Percentage
            Text(
                text = "${ramStats.usedPercentage}%",
                fontSize = 48.sp,
                fontWeight = FontWeight.ExtraBold,
                color = if (ramStats.usedPercentage > 80) Color.Red else MaterialTheme.colorScheme.onSurface
            )

            Text(
                text = "${ramStats.usedRamMb} MB / ${ramStats.totalRamMb} MB Used",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Progress Bar
            LinearProgressIndicator(
                progress = animatedProgress,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(10.dp),
                color = if (ramStats.usedPercentage > 80) Color.Red else MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.surface
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = statusMessage,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.secondary
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Action Button
            Button(
                onClick = {
                    scope.launch {
                        isOptimizing = true
                        statusMessage = "Clearing background tasks..."
                        val freedMb = ramOptimizer.optimizeRam()
                        ramStats = ramOptimizer.getRamStats()
                        statusMessage = "Freed ~$freedMb MB RAM successfully!"
                        isOptimizing = false
                    }
                },
                enabled = !isOptimizing,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .testTag("btn_boost_ram_now"),
                shape = RoundedCornerShape(12.dp)
            ) {
                if (isOptimizing) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(text = "BOOST RAM NOW", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
