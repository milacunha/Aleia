package br.com.camilacunha.aleia.data

import br.com.camilacunha.aleia.data.remote.model.GoogleBookResponse
import br.com.camilacunha.aleia.data.remote.model.ImageLinks
import br.com.camilacunha.aleia.data.remote.model.Volume
import br.com.camilacunha.aleia.data.remote.model.VolumeInfo
import io.mockk.every
import io.mockk.mockk

object RemoteTestHelpers {

    fun mockVolume(
        title: String = "O Hobbit",
        authors: List<String>? = listOf("Tolkien"),
        genres: List<String>? = listOf("Fantasia"),
        thumbnail: String? = "https://example.com/cover.jpg"
    ): Volume {
        val volume = mockk<Volume>(relaxed = true)
        val info = mockk<VolumeInfo>(relaxed = true)
        val imageLinks = mockk<ImageLinks>(relaxed = true)

        every { volume.volumeInfo } returns info
        every { info.title } returns title
        every { info.authors } returns authors
        every { info.genres } returns genres
        every { info.imageLinks } returns imageLinks
        every { imageLinks.thumbnail } returns thumbnail

        return volume
    }

    fun responseWith(items: List<Volume>?): GoogleBookResponse =
        mockk(relaxed = true) { every { this@mockk.items } returns items }
}