package com.ghost.android.test.ui.model

data class EngineResult(
    val name: String,
    val timeMs: Double,
    val memoryBytes: Long,
    val jankCount: Int = 0
)