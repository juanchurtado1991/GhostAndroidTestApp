package com.ghost.android.test.domain

import com.ghost.serialization.annotations.GhostSerialization
import com.google.gson.annotations.SerializedName
import com.squareup.moshi.Json
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@GhostSerialization
@Serializable
data class PageInfo(
    @SerializedName("count")
    @Json(name = "count")
    @SerialName("count")
    val count: Int,
    @SerializedName("pages")
    @Json(name = "pages")
    @SerialName("pages")
    val pages: Int,
    @SerializedName("next")
    @Json(name = "next")
    @SerialName("next")
    val next: String? = null,
    @SerializedName("prev")
    @Json(name = "prev")
    @SerialName("prev")
    val prev: String? = null
)