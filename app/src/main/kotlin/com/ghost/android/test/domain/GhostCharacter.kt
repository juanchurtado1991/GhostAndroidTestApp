package com.ghost.android.test.domain

import com.ghost.serialization.annotations.GhostSerialization
import com.google.gson.annotations.SerializedName
import com.squareup.moshi.Json
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@GhostSerialization
@Serializable
data class GhostCharacter(
    @SerializedName("id")
    @Json(name = "id")
    @SerialName("id")
    val id: Int,
    @SerializedName("name")
    @Json(name = "name")
    @SerialName("name")
    val name: String,
    @SerializedName("status")
    @Json(name = "status")
    @SerialName("status")
    val status: CharacterStatus = CharacterStatus.unknown,
    @SerializedName("species")
    @Json(name = "species")
    @SerialName("species")
    val species: String = "",
    @SerializedName("type")
    @Json(name = "type")
    @SerialName("type")
    val type: String = "",
    @SerializedName("gender")
    @Json(name = "gender")
    @SerialName("gender")
    val gender: String = "",
    @SerializedName("origin")
    @Json(name = "origin")
    @SerialName("origin")
    val origin: LocationRef,
    @SerializedName("location")
    @Json(name = "location")
    @SerialName("location")
    val location: LocationRef,
    @SerializedName("image")
    @Json(name = "image")
    @SerialName("image")
    val image: String = "",
    @SerializedName("episode")
    @Json(name = "episode")
    @SerialName("episode")
    val episode: List<String> = emptyList(),
    @SerializedName("url")
    @Json(name = "url")
    @SerialName("url")
    val url: String = "",
    @SerializedName("created")
    @Json(name = "created")
    @SerialName("created")
    val created: String = ""
)