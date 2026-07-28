package com.example.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface SavedContentDao {
    @Query("SELECT * FROM saved_content ORDER BY createdAt DESC")
    fun getAllSavedContent(): Flow<List<SavedContentEntity>>

    @Query("SELECT * FROM saved_content WHERE toolType = :toolType ORDER BY createdAt DESC")
    fun getContentByTool(toolType: String): Flow<List<SavedContentEntity>>

    @Query("SELECT * FROM saved_content WHERE title LIKE '%' || :query || '%' OR generatedOutput LIKE '%' || :query || '%' ORDER BY createdAt DESC")
    fun searchContent(query: String): Flow<List<SavedContentEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertContent(content: SavedContentEntity): Long

    @Query("DELETE FROM saved_content WHERE id = :id")
    suspend fun deleteContentById(id: Long)

    @Query("UPDATE saved_content SET isFavorite = :isFavorite WHERE id = :id")
    suspend fun updateFavorite(id: Long, isFavorite: Boolean)
}
