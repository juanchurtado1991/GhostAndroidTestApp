package com.ghost.android.test.ui.model

import com.ghost.android.test.domain.GhostCharacter

enum class NetworkStack(val title: String, val description: String, val engineName: String) {
    GHOST_KTOR(
        "GHOST + KTOR",
        "High-performance streaming serialization. Best for low-latency and large datasets.",
        "GHOST"
    ),
    GHOST_RETROFIT(
        "GHOST + RETROFIT",
        "The fastest Retrofit adapter. Replaces standard converters with Ghost's pre-compiled engine.",
        "GHOST"
    ),
    RETROFIT_MOSHI(
        "RETROFIT + MOSHI",
        "The Android industry standard. Uses reflection-based mapping and robust Retrofit interfaces.",
        "MOSHI"
    ),
    RETROFIT_GSON(
        "RETROFIT + GSON",
        "The classic legacy stack. Uses Google's Gson engine. Slower and more memory-intensive.",
        "GSON"
    ),
    KTORFIT_KOTLINX(
        "KTORFIT + KOTLINX",
        "The modern KMP alternative to Retrofit. Uses KSP for code generation and Kotlinx.Serialization.",
        "KSER"
    ),
    KTOR_MOSHI(
        "KTOR + MOSHI",
        "Combines Ktor's modern asynchronous client with Moshi's reliable serialization engine.",
        "MOSHI"
    )
}

data class UiState(
    val characters: List<GhostCharacter> = emptyList(),
    val isLoading: Boolean = false,
    val loadingStatus: String = "PROFILING...",
    val errorMessage: String? = null,
    val results: List<EngineResult> = emptyList(),
    val sessionHistory: List<String> = emptyList(),
    val pageCount: Float = 20f,
    val selectedStack: NetworkStack = NetworkStack.GHOST_KTOR,
    val isStackDialogVisible: Boolean = false
)