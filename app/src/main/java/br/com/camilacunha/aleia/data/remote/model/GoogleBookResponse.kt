package br.com.camilacunha.aleia.data.remote.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GoogleBookResponse(
    val items: List<Volume>? = null
)

@JsonClass(generateAdapter = true)
data class Volume(
    val id: String? = null,
    val volumeInfo: VolumeInfo? = null
)

@JsonClass(generateAdapter = true)
data class VolumeInfo(
    val title: String? = null,
    val authors: List<String>? = null,

    @Json(name = "categories")
    val genres: List<String>? = null,

    @Json(name = "imageLinks")
    val imageLinks: ImageLinks? = null
)

@JsonClass(generateAdapter = true)
data class ImageLinks(
    val thumbnail: String? = null,
    val smallThumbnail: String? = null
)