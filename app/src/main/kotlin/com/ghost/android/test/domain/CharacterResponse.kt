package com.ghost.android.test.domain

import com.ghost.serialization.annotations.GhostSerialization
import com.google.gson.annotations.SerializedName
import com.squareup.moshi.Json
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@GhostSerialization
@Serializable
data class CharacterResponse(
    @SerializedName("info")
    @Json(name = "info")
    @SerialName("info")
    val info: PageInfo,
    @SerializedName("results")
    @Json(name = "results")
    @SerialName("results")
    val results: List<GhostCharacter>
)