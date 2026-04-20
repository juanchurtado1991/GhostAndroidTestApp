package com.ghost.android.test.domain

import com.ghost.serialization.annotations.GhostName
import com.ghost.serialization.annotations.GhostSerialization
import com.google.gson.annotations.SerializedName
import com.squareup.moshi.Json
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@GhostSerialization
@Serializable
enum class CharacterStatus {
    @SerializedName("Alive")
    @Json(name = "Alive")
    @SerialName("Alive")
    @GhostName("Alive")
    Alive,
    @SerializedName("Dead")
    @Json(name = "Dead")
    @SerialName("Dead")
    @GhostName("Dead")
    Dead,
    @SerializedName("unknown")
    @Json(name = "unknown")
    @SerialName("unknown")
    @GhostName("unknown")
    unknown
}