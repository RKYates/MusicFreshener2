package com.example.musicfreshener2.data

import kotlinx.coroutines.flow.Flow

interface MusicRepository {
    fun getAllMusicStream(): Flow<List<MusicEntry>>

    fun getMusicStream(id: Int): Flow<MusicEntry>

    suspend fun insertMusic(musicEntry: MusicEntry)

    suspend fun updateMusic(musicEntry: MusicEntry)
}