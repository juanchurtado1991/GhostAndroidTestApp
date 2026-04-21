# 👻 Ghost Android Benchmark Dashboard

This is the official testing laboratory for **Ghost Serialization** in Android environments. This application serves as both a performance validation tool and a blueprint for production-grade integrations on Android.

## 🛠️ How to Use Ghost in your Android Project

Ghost is optimized for the Android runtime, providing near-native performance with zero reflection. Follow these steps to integrate it:

### 1. Define your Models
Annotate your data classes with `@Ghost`. This triggers the KSP compiler to generate the specialized serialization code during your build.

```kotlin
import com.ghost.serialization.Ghost

@Ghost
data class User(
    val id: Int,
    val username: String,
    val email: String
)
```

### 2. Gradle Configuration
Apply the Ghost plugin in your app-level `build.gradle.kts`. This plugin handles KSP setup and automatically adds the necessary runtime dependencies.

```kotlin
plugins {
    id("com.ghostserializer.ghost") version "1.1.14"
}

// Optionally customize the version
ghost {
    version.set("1.1.14")
}
```

### 3. Networking Integration

#### Retrofit (Recommended)
Ghost provides a specialized converter for Retrofit. It avoids all reflection when parsing response bodies.

```kotlin
import com.ghost.serialization.retrofit.GhostConverterFactory

val retrofit = Retrofit.Builder()
    .baseUrl("https://api.ghost.com/")
    .addConverterFactory(GhostConverterFactory.create())
    .build()
```

#### Ktor
If you use Ktor (KMP), install the Ghost content negotiator:

```kotlin
import com.ghost.serialization.ktor.ghost

val client = HttpClient(OkHttp) {
    install(ContentNegotiation) {
        ghost()
    }
}
```

### 4. Manual Serialization
For local storage (e.g., saving to DataStore or SharedPreferences), you can use the `Ghost` entry point directly:

```kotlin
val user = User(1, "Ghosty", "ghost@example.com")

// Encode to String or Bytes
val jsonString = Ghost.encodeToString(user)
val jsonBytes = Ghost.encodeToBytes(user)

// Decode
val decoded = Ghost.deserialize<User>(jsonString)
```

> **Note on Initialization**: On Android, Ghost uses `ServiceLoader` for automatic registry discovery. If you use ProGuard/R8, ensure you keep the `META-INF/services` entries (the Ghost plugin handles this automatically).

## 📊 Surgical Audit Dashboard & Benchmark Results

The Android application includes a robust benchmarking tool running directly on the device. To eliminate OS noise and JIT compilation variance, the test suite executes an aggressive warmup and averages results over many iterations.

### Benchmark Methodology
- **Workload:** 100 iterations per configuration (`BENCHMARK_ITERATIONS = 100`) after an aggressive 1000-iteration JIT warmup.
- **Payload:** Real-world API payloads from the Rick and Morty API (Stress Load: 20 Pages).
- **Network Stack:** Evaluated via a local playback mechanism (OkHttp FakeResponseInterceptor & Ktor MockEngine) to accurately measure converter overhead without network variability.
- **Surgical Metrics**: Memory measurements are captured using thread-local allocation tracking, bypassing external heap pollution.

### Benchmark Results (Native Android, 20 Pages, x100)

| Engine | Operation | Mode | Avg Latency | Avg Memory (Waste) |
|--------|-----------|------|-------------|--------------------|
| **Gson** | Network | Retrofit | 7.52 ms | 883 KB |
| **Moshi** | Network | Retrofit | 7.72 ms | 695 KB |
| **KSer** | Network | Ktorfit | 16.10 ms | 2438 KB |
| **Ghost** | Network | Retrofit | **5.70 ms** | **683 KB** |
| | | | | |
| **Gson** | Read | String | 5.98 ms | 693 KB |
| **Moshi** | Read | String | 7.79 ms | 687 KB |
| **KSer** | Read | String | 9.71 ms | 628 KB |
| **Ghost** | Read | String | **4.70 ms** | **506 KB** |
| | | | | |
| **Gson** | Read | Bytes | 6.42 ms | 1084 KB |
| **Moshi** | Read | Bytes | 8.19 ms | 1077 KB |
| **KSer** | Read | Bytes | 10.18 ms | 1019 KB |
| **Ghost** | Read | Bytes | **4.18 ms** | **303 KB** |
| | | | | |
| **Gson** | Read | Stream | 6.45 ms | 714 KB |
| **Moshi** | Read | Stream | 6.12 ms | 685 KB |
| **KSer** | Read | Stream | 10.15 ms | 1020 KB |
| **Ghost** | Read | Stream | **4.42 ms** | **675 KB** |
| | | | | |
| **Gson** | Write | String | 10.93 ms | 1694 KB |
| **Moshi** | Write | String | 9.61 ms | 1130 KB |
| **KSer** | Write | String | 4.92 ms | 447 KB |
| **Ghost** | Write | String | **4.13 ms** | **428 KB** |
| | | | | |
| **Gson** | Write | Bytes | 9.13 ms | 1886 KB |
| **Moshi** | Write | Bytes | 8.61 ms | 1314 KB |
| **KSer** | Write | Bytes | 3.43 ms | 634 KB |
| **Ghost** | Write | Bytes | **2.22 ms** | **220 KB** |
| | | | | |
| **Gson** | Write | Buffer | 10.35 ms | 1856 KB |
| **Moshi** | Write | Buffer | 8.59 ms | 544 KB |
| **Ghost** | Write | Buffer | **2.22 ms** | **191 KB** |

**Conclusion:** Ghost consistently outperforms standard libraries (Gson, Moshi, Kotlinx.Serialization) across all Android operations, especially when dealing with high-throughput streams or byte parsing. 

It cuts down memory allocations by **up to 80%** (e.g., 220 KB vs. 1886 KB for Gson Byte Writes), which drastically reduces garbage collection pauses and prevents UI jank on Android devices.

## 🚀 How to Run the Benchmark Locally

1. Open the project in Android Studio.
2. Ensure you have an Android device or emulator running.
3. Select the `app` module and run `assembleDebug` or click the Play button to deploy.
4. Launch the app, select the desired stress load (e.g., 20 Pages), and press **Run Benchmark**.
5. Wait for the `JIT Warmup (200x)` to complete.
6. The test will run through parsing, writing, and networking stages, displaying real-time metrics on the dashboard.

---
*Developed with ❤️ by the Ghost Serialization team.* 👻
