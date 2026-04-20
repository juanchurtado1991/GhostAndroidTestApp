package com.ghost.android.test.ui.composable

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Surface
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.ghost.android.test.ui.AppDesign
import com.ghost.android.test.ui.model.UiState
import com.ghost.android.test.util.formatMem

@SuppressLint("DefaultLocale")
@Composable
fun PerformanceResultsCard(
    uiState: UiState,
    onCopyLogs: () -> Unit
) {
    val selectedEngineName = uiState.selectedStack.engineName
    val ghostRes = uiState.results.find { it.name == "GHOST" }
    val comparisonEngineName =
        if (selectedEngineName == "GHOST") "KSER" else selectedEngineName
    val currentRes = uiState.results.find { it.name == comparisonEngineName }
        ?: uiState.results.find { it.name == "KSER" }

    val speedFactor =
        if (ghostRes != null && currentRes != null && ghostRes.timeMs > 0) {
            (currentRes.timeMs / ghostRes.timeMs)
        } else 1.0

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        color = AppDesign.SurfaceColor.copy(alpha = 0.5f),
        border = BorderStroke(1.dp, AppDesign.GlassBorder)
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            SampleText(
                text = "PERFORMANCE INSIGHT",
                isBold = true,
                fontSize = 12,
                overrideColor = AppDesign.AccentGlow
            )

            val insightText = if (selectedEngineName == "GHOST") {
                "Peak performance detected. Pure streaming power."
            } else {
                "Ghost is ${
                    String.format("%.2fx", speedFactor)
                } faster than your current stack."
            }
            SampleText(
                text = insightText,
                fontSize = 16,
                isBold = true,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(vertical = 12.dp)
            )

            HorizontalDivider(
                color = AppDesign.GlassBorder,
                thickness = 1.dp,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            SampleText(
                text = "JSON ➡️ OBJECTS PERFORMANCE (ms / JANK)",
                isBold = true,
                fontSize = 12,
                isSecondary = true,
                textAlign = TextAlign.Center
            )
            SampleText(
                text = "Jank represents dropped frames. 0 means perfect fluidity.",
                fontSize = 10,
                isSecondary = true,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 4.dp)
            )
            Spacer(modifier = Modifier.height(24.dp))

            val chunkedResults = uiState.results.chunked(2)
            chunkedResults.forEach { row ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    row.forEach { res ->
                        val isCurrent = res.name == selectedEngineName
                        val color = when (res.name) {
                            "GHOST" -> AppDesign.AccentGlow
                            "MOSHI" -> AppDesign.ErrorColor
                            "GSON" -> Color(0xFFFACC15)
                            else -> Color(0xFF818CF8)
                        }
                        Box(modifier = Modifier.padding(horizontal = 8.dp)) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                if (isCurrent) SampleText(
                                    text = "YOUR STACK",
                                    fontSize = 8,
                                    isBold = true,
                                    overrideColor = color
                                )
                                MetricItem(
                                    title = res.name + " (JANK:${res.jankCount})",
                                    value = "${(res.timeMs * 100).toInt() / 100.0}ms",
                                    overrideColor = if (isCurrent) color else color.copy(
                                        alpha = 0.6f
                                    )
                                )
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            if (uiState.results.any { it.memoryBytes > 0 }) {
                Spacer(modifier = Modifier.height(16.dp))
                SampleText(
                    text = "MEMORY ALLOCATION",
                    isBold = true,
                    fontSize = 10,
                    isSecondary = true,
                    modifier = Modifier.padding(bottom = 12.dp)
                )
                chunkedResults.forEach { row ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        row.forEach { res ->
                            val isCurrent = res.name == selectedEngineName
                            val color = when (res.name) {
                                "GHOST" -> AppDesign.AccentGlow
                                "MOSHI" -> AppDesign.ErrorColor
                                "GSON" -> Color(0xFFFACC15)
                                else -> Color(0xFF818CF8)
                            }
                            Box(modifier = Modifier.padding(horizontal = 8.dp)) {
                                MetricItem(
                                    title = res.name + " MEM",
                                    value = formatMem(res.memoryBytes),
                                    overrideColor = if (isCurrent) color else color.copy(
                                        alpha = 0.6f
                                    )
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
            TextButton(onClick = onCopyLogs) {
                SampleText(
                    text = "COPY SESSION LOGS",
                    overrideColor = AppDesign.AccentGlow,
                    isBold = true,
                    fontSize = 12
                )
            }
        }
    }
}
