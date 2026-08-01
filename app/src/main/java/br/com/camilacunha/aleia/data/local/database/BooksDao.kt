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
}