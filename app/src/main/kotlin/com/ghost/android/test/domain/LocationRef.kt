package com.ghost.android.test.domain

import com.ghost.serialization.annotations.GhostSerialization
import com.google.gson.annotations.SerializedName
import com.squareup.moshi.Json
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@GhostSerialization
@Serializable
data class LocationRef(
    @SerializedName("name")
    @Json(name = "name")
    @SerialName("name")
    val name: String,
    @SerializedName("url")
    @Json(name = "url")
    @SerialName("url")
    val url: String
)