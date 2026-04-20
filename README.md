# Ghost Serialization Android Benchmark 👻🏎️

This is the official industrial benchmark application for **Ghost Serialization**. It serves as a performance laboratory to demonstrate the extreme efficiency of Ghost compared to industry standards like Moshi, Gson, and Kotlinx.Serialization.

## 🎯 Purpose

The goal of this application is to provide **irrefutable evidence** of Ghost's performance advantages in real-world scenarios:
- **Low Latency**: Sub-millisecond parsing even under heavy stress.
- **Zero Jank**: Native streaming prevents UI stutters by avoiding massive object allocations.
- **Memory Efficiency**: Minimal memory footprint, crucial for low-end devices.

## 🛠️ Performance Laboratory Features

- **6 Industrial Stacks**: Compare Ghost against Retrofit (Moshi/Gson) and Ktor (Kotlinx/Moshi).
- **Stress Testing**: Load up to 10 pages (~200 items) of the Rick and Morty API simultaneously.
- **Scientific Metrics**:
    - **Averaged Results**: 10-iteration averaging to eliminate execution noise.
    - **JIT Warm-up**: 50 pre-run iterations to ensure JIT optimization.
    - **Memory Tracking**: Precise allocation measurement via `VMDebug`.
    - **Jank Counter**: Real-time detection of UI frame drops during parsing.

## 🚀 Supported Stacks

1.  **GHOST + KTOR**: The pinnacle of performance using native streaming.
2.  **GHOST + RETROFIT**: Seamlessly integrate Ghost into existing Retrofit architectures.
3.  **RETROFIT + MOSHI**: The current Android industry standard.
4.  **RETROFIT + GSON**: Legacy comparison for older projects.
5.  **KTORFIT + KOTLINX**: Modern KMP alternative.
6.  **KTOR + MOSHI**: Modern asynchronous hybrid stack.

## 📱 Tech Stack

- **UI**: Jetpack Compose (Modern Material 3 Industrial Design).
- **Networking**: Ktor 3.x & Retrofit 2.11.x.
- **Serialization**: Ghost 1.1.x, Moshi, Gson, Kotlinx.Serialization.
- **Architecture**: Clean MVVM with `androidx.lifecycle.ViewModel`.

## 📈 Interpretation of Results

- **Performance Insight**: The app automatically calculates how many times faster Ghost is compared to your current selected stack.
- **JANK (J)**: Represents UI stutters. **J:0** is the goal (Perfect performance).
- **YOUR STACK**: Identifies the stack you chose in the comparison card for easy visualization.

---
*Developed with ❤️ by the Ghost Serialization team.* 👻
