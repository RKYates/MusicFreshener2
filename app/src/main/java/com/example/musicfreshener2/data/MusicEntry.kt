package com.example.musicfreshener2.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "music_entries")
data class MusicEntry (
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val artist: String,
    val album: String,
    val rating: Int,
    val date: String,
    val genre: String
)