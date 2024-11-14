package com.example.musicfreshener2.data

import kotlinx.coroutines.flow.Flow

class OfflineMusicRepository(private val musicDao: MusicDao) : MusicRepository {
    override fun getAllMusicStream(): Flow<List<MusicEntry>> = musicDao.getAllMusic()

    override fun getMusicStream(id: Int): Flow<MusicEntry> = musicDao.getMusicEntry(id)

    override suspend fun insertMusic(musicEntry: MusicEntry) = musicDao.insertMusic(musicEntry)

    override suspend fun updateMusic(musicEntry: MusicEntry) = musicDao.updateMusic(musicEntry)
}