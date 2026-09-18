package com.example.cyberpunkmanager.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "saved_items")
data class SavedItem(
    @PrimaryKey val id: String,
    val name: String,
    val category: String,
    val jsonData: String
)
