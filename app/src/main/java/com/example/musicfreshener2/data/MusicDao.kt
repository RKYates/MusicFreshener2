package com.example.musicfreshener2.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface MusicDao {
    @Insert
    suspend fun insertMusic(entry: MusicEntry)

    @Update
    suspend fun updateMusic(entry: MusicEntry)

    @Query("SELECT * FROM music_entries")
    fun getAllMusic(): Flow<List<MusicEntry>>

    @Query("SELECT * FROM music_entries WHERE id = :id")
    fun getMusicEntry(id: Int): Flow<MusicEntry>
}