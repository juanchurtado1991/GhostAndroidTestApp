package com.ghost.android.test.ui.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.ghost.android.test.ui.AppDesign

@Composable
fun StatusIndicator(status: String) {
    val color = when (status.uppercase()) {
        "ALIVE" -> AppDesign.StatusAlive
        "DEAD" -> AppDesign.StatusDead
        else -> AppDesign.StatusUnknown
    }

    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .clip(CircleShape)
                .background(color)
        )
        Spacer(modifier = Modifier.width(6.dp))
        SampleText(text = status, isBold = true, fontSize = 12)
    }
}
