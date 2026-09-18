package br.com.camilacunha.aleia.data.local.seed

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SeedBookDto(
    val title: String,
    val author: String,
    val genre: String,
    val coverUrl: String
)