package com.ghost.android.test.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ghost.android.test.data.GhostRetrofitClient
import com.ghost.android.test.data.GsonRetrofitClient
import com.ghost.android.test.data.KtorMoshiClient
import com.ghost.android.test.data.KtorfitClient
import com.ghost.android.test.data.RetrofitClient
import com.ghost.android.test.data.RickAndMortyRepository
import com.ghost.android.test.ui.model.NetworkStack
import com.ghost.android.test.ui.model.UiState
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    private val repository = RickAndMortyRepository()

    var uiState by mutableStateOf(UiState())
        private set

    private var currentBenchmarkingEngine: String? = null
    private var currentJankAccumulator = 0

    fun showStackDialog(show: Boolean) {
        uiState = uiState.copy(isStackDialogVisible = show)
    }

    fun selectStack(stack: NetworkStack) {
        uiState = uiState.copy(selectedStack = stack, isStackDialogVisible = false)
    }

    fun updatePageCount(count: Float) {
        uiState = uiState.copy(pageCount = count)
    }

    fun onJankDetected() {
        if (currentBenchmarkingEngine != null) {
            currentJankAccumulator++
        }
    }

    fun runBenchmark() {
        if (uiState.isLoading) return

        uiState =
            uiState.copy(isLoading = true, errorMessage = null, loadingStatus = "Initiating...")

        viewModelScope.launch {
            try {
                // Stack selection for the main list fetch
                when (uiState.selectedStack) {
                    NetworkStack.GHOST_KTOR -> repository.getCharacters(uiState.pageCount.toInt())
                    NetworkStack.GHOST_RETROFIT -> GhostRetrofitClient.service.getCharacters(uiState.pageCount.toInt())
                    NetworkStack.RETROFIT_MOSHI -> RetrofitClient.service.getCharacters(uiState.pageCount.toInt())
                    NetworkStack.RETROFIT_GSON -> GsonRetrofitClient.service.getCharacters(uiState.pageCount.toInt())
                    NetworkStack.KTORFIT_KOTLINX -> KtorfitClient.service.getCharacters(uiState.pageCount.toInt())
                    NetworkStack.KTOR_MOSHI -> KtorMoshiClient.getCharacters(uiState.pageCount.toInt())
                }

                val (benchResponse, engineResults) = repository
                    .runBenchmark(uiState.pageCount.toInt()) { status ->
                        when (status) {
                            "RESET_JANK" -> {
                                currentJankAccumulator = 0
                                0
                            }

                            "" -> {
                                currentJankAccumulator
                            }

                            else -> {
                                uiState = uiState.copy(loadingStatus = status)
                                if (status.startsWith("Benchmarking")) {
                                    currentBenchmarkingEngine = status
                                        .substringAfter("Benchmarking ")
                                        .substringBefore(" ")
                                }
                                currentJankAccumulator = 0
                                0
                            }
                        }
                    }

                uiState = uiState.copy(
                    isLoading = false,
                    characters = benchResponse.results,
                    results = engineResults,
                    sessionHistory = uiState.sessionHistory +
                            "Run with ${uiState.pageCount.toInt()} " +
                            "pages (Stack: ${uiState.selectedStack.title})"
                )
            } catch (e: Exception) {
                uiState =
                    uiState.copy(isLoading = false, errorMessage = e.message ?: "Unknown Error")
            }
        }
    }

    fun getExportLogs(): String {
        return uiState.sessionHistory.joinToString("\n")
    }
}
