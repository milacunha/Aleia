package br.com.camilacunha.aleia.data.local.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import br.com.camilacunha.aleia.data.local.model.BookEntity

@Dao
interface BooksDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertBook(book: BookEntity): Long //-1 se ignorado

    @Query("SELECT * FROM books WHERE isRead is 0")
    suspend fun getAllUnreadBooks(): List<BookEntity>

    @Query("UPDATE books SET isRead = 1 WHERE id = :bookId")
    suspend fun markAsRead(bookId: Int)

    @Query("SELECT DISTINCT genre FROM books WHERE isRead = 0 AND genre IS NOT NULL AND genre != '' ORDER BY genre")
    suspend fun getDistinctGenres(): List<String>

    @Query("SELECT * FROM books WHERE isRead = 0 AND genre = :genre")
    suspend fun getUnreadBooksByGenre(genre: String): List<BookEntity>

    @Query(
        """
        SELECT * FROM books 
        WHERE isRead = 0 
          AND (coverUrl IS NULL OR coverUrl = '' OR genre IS NULL OR genre = '')
        ORDER BY id ASC
    """
    )
    suspend fun getBooksMissingMetadata(): List<BookEntity>

    @Query(
        """
        UPDATE books 
        SET coverUrl = :coverUrl, genre = :genre 
        WHERE id = :id
    """
    )
    suspend fun updateBookCoverAndGenre(
        id: Int,
        coverUrl: String?,
        genre: String?
    ): Int //0 se não encontrado
}