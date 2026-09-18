package com.example.cyberpunkmanager.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface SavedItemDao {
    @Query("SELECT * FROM saved_items")
    fun getAllSavedItems(): Flow<List<SavedItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSavedItem(item: SavedItem)

    @Query("DELETE FROM saved_items WHERE id = :id")
    suspend fun deleteSavedItemById(id: String)

    @Query("SELECT EXISTS(SELECT 1 FROM saved_items WHERE id = :id)")
    suspend fun isItemSaved(id: String): Boolean
}
