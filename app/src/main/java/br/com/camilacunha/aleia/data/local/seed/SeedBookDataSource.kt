package br.com.camilacunha.aleia.data.local.seed

import android.content.Context
import android.util.Log
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types

class SeedBookDataSource(
    private val context: Context,
    private val moshi: Moshi
) {
    private val tag = this::class.java.simpleName

    fun readSeedBooks(): List<SeedBookDto> {
        return try {
            val json = context.assets
                .open(FILE_NAME)
                .bufferedReader()
                .use { it.readText() }

            val type = Types.newParameterizedType(List::class.java, SeedBookDto::class.java)
            val adapter = moshi.adapter<List<SeedBookDto>>(type)
            val books = adapter.fromJson(json).orEmpty()

            Log.d(tag, "JSON lido com sucesso: ${books.size} livros")
            books
        } catch (e: Exception) {
            Log.e(tag, "Erro ao ler/parsear $FILE_NAME", e)
            emptyList()
        }
    }

    private companion object {
        const val FILE_NAME = "books.json"
    }
}