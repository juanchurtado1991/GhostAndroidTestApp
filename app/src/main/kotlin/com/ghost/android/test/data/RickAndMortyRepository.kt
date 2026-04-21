package com.ghost.android.test.data

import android.annotation.SuppressLint
import com.ghost.android.test.domain.CharacterResponse
import com.ghost.android.test.ui.model.EngineResult
import com.ghost.android.test.util.forceGC
import com.ghost.android.test.util.getCurrentThreadAllocatedBytes
import com.ghost.serialization.Ghost
import com.ghost.serialization.benchmark.GhostModuleRegistry_app
import com.ghost.serialization.ktor.ghost
import com.google.gson.Gson
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.Request

class RickAndMortyRepository {

    init {
        Ghost.addRegistry(GhostModuleRegistry_app.INSTANCE)
    }

    private val moshiAdapter = Moshi.Builder()
        .addLast(KotlinJsonAdapterFactory())
        .build()
        .adapter(CharacterResponse::class.java)
    private val kser = Json { ignoreUnknownKeys = true }
    private val client = OkHttpClient()
    private val gson = Gson()

    private val ktorClient = HttpClient {
        install(ContentNegotiation) { ghost() }
    }

    suspend fun getCharacters(page: Int): CharacterResponse = withContext(Dispatchers.IO) {
        ktorClient.get("https://rickandmortyapi.com/api/character") {
            parameter("page", page)
        }.body()
    }

    @SuppressLint("CheckResult")
    suspend fun runBenchmark(
        pageCount: Int,
        onEngineMeasure: suspend (String) -> Int
    ): Pair<CharacterResponse, List<EngineResult>> = withContext(Dispatchers.IO) {
        val allBytes = mutableListOf<ByteArray>()
        
        onEngineMeasure("Downloading Stress Data ($pageCount pages)...")
        for (i in 1..pageCount) {
            onEngineMeasure("Downloading Page $i/$pageCount...")
            val request = Request.Builder()
                .url("https://rickandmortyapi.com/api/character/?page=$i")
                .build()

            val responseBytes = client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) throw Exception("API Error on page $i: ${response.code}")
                response.body?.bytes() ?: throw RuntimeException("Empty response Body")
            }
            allBytes.add(responseBytes)
        }

        // We simulate a large contiguous payload by joining results
        val jsonString = if (pageCount == 1) {
            allBytes[0].decodeToString()
        } else {
            val firstPage = allBytes[0].decodeToString()
            val infoPart = firstPage.substringBefore("\"results\":")
            val resultsList = allBytes.joinToString(",") { 
                val s = it.decodeToString()
                s.substringAfter("\"results\":")
                 .substringBeforeLast("}")
                 .trim()
                 .removePrefix("[")
                 .removeSuffix("]")
            }
            "$infoPart\"results\": [$resultsList]}"
        }
        val responseBytes = jsonString.encodeToByteArray()

        // 2. INTENSIVE WARM-UP (50 iterations to match KMP)
        // Ensures JIT is hot and Ghost's internal buffers are ready
        onEngineMeasure("Warming up engines (50x)...")
        repeat(50) {
            Ghost.deserialize<CharacterResponse>(responseBytes)
            moshiAdapter.fromJson(jsonString)
            gson.fromJson(jsonString, CharacterResponse::class.java)
            kser.decodeFromString<CharacterResponse>(jsonString)
        }
        forceGC()

        val results = listOf(
            measureEngine(GHOST, onEngineMeasure) {
                Ghost.deserialize<CharacterResponse>(responseBytes)
            },
            measureEngine(MOSHI, onEngineMeasure) {
                moshiAdapter.fromJson(jsonString)
            },
            measureEngine(GSON, onEngineMeasure) {
                gson.fromJson(jsonString, CharacterResponse::class.java)
            },
            measureEngine(KSER, onEngineMeasure) {
                kser.decodeFromString<CharacterResponse>(jsonString)
            },
            measureEngine("BLANK", onEngineMeasure) {
                // Empty
            }
        )

        val serializer = Ghost.getSerializer(CharacterResponse::class)
        val serializerName = serializer?.let { it::class.simpleName } ?: "None"
        onEngineMeasure("Finalizing... Serializer: $serializerName")

        val decoded = Ghost.deserialize<CharacterResponse>(responseBytes)
        return@withContext decoded to results
    }

    private suspend inline fun <T> measureEngine(
        engineName: String,
        onEngineMeasure: suspend (String) -> Int,
        crossinline block: () -> T
    ): EngineResult {
        val iterations = 100
        var totalTimeNs = 0L
        var totalMemBytes = 0L
        var totalJank = 0
        
        // 1. STABILIZATION before the batch
        System.gc()
        delay(500)

        onEngineMeasure("Benchmarking $engineName (100 iterations)...")
        repeat(iterations) {
            // Precise Memory Start
            val memStart = getCurrentThreadAllocatedBytes()
            val timeStart = System.nanoTime()

            block()

            val timeEnd = System.nanoTime()
            val memEnd = getCurrentThreadAllocatedBytes()
            
            totalTimeNs += (timeEnd - timeStart)
            totalMemBytes += if (memEnd >= memStart) memEnd - memStart else 0L
            
            // Short delay to let JankStats catch frame drops per iteration
            delay(50)
            
            totalJank += onEngineMeasure("") 
            onEngineMeasure("RESET_JANK") 
        }

        return EngineResult(
            name = engineName,
            timeMs = (totalTimeNs / iterations.toDouble()) / 1_000_000.0,
            memoryBytes = totalMemBytes / iterations,
            jankCount = totalJank
        )
    }

    companion object {
        private const val GSON = "GSON"
        private const val GHOST = "GHOST"
        private const val MOSHI = "MOSHI"
        private const val KSER = "KSER"
    }
}
