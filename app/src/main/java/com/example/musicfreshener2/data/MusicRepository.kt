package com.example.musicfreshener2.data

import com.example.musicfreshener2.ui.home.SortingType
import kotlinx.coroutines.flow.Flow

interface MusicRepository {
    fun getAllMusicStream(sortingType: SortingType): Flow<List<MusicEntry>>

    fun getMusicStream(id: Int): Flow<MusicEntry>

    suspend fun insertMusic(musicEntry: MusicEntry)

    suspend fun updateMusic(musicEntry: MusicEntry)
}