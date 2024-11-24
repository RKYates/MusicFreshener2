package com.example.musicfreshener2.data

import com.example.musicfreshener2.ui.home.SortingType
import com.example.musicfreshener2.ui.home.SortingType.TO_LISTEN_TO
import com.example.musicfreshener2.ui.home.SortingType.HISTORY
import kotlinx.coroutines.flow.Flow

class OfflineMusicRepository(private val musicDao: MusicDao) : MusicRepository {
    override fun getAllMusicStream(sortingType: SortingType): Flow<List<MusicEntry>> {
        return when (sortingType) {
            TO_LISTEN_TO -> musicDao.getAllMusicToListenTo()
            HISTORY -> musicDao.getAllMusicHistory()
        }
    }

    override fun getMusicStream(id: Int): Flow<MusicEntry> = musicDao.getMusicEntry(id)

    override suspend fun insertMusic(musicEntry: MusicEntry) = musicDao.insertMusic(musicEntry)

    override suspend fun updateMusic(musicEntry: MusicEntry) = musicDao.updateMusic(musicEntry)
}