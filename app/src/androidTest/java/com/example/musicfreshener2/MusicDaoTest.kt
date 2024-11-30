package com.example.musicfreshener2

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.musicfreshener2.data.MusicDao
import com.example.musicfreshener2.data.MusicDatabase
import com.example.musicfreshener2.data.MusicEntry
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.todayIn
import org.junit.After

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Before

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class MusicDaoTest {

    ////////////////////// DATA //////////////////////

    // vars used in tests
    private lateinit var musicDao: MusicDao
    private lateinit var db: MusicDatabase

    // dates and rows for testing
    private val today = Clock.System.todayIn(TimeZone.currentSystemDefault()).toString()
    private val eightDaysAgo = Clock.System.todayIn(TimeZone.currentSystemDefault()).minus(8, kotlinx.datetime.DateTimeUnit.DAY).toString()
    private val fiveDaysAgo = Clock.System.todayIn(TimeZone.currentSystemDefault()).minus(5, kotlinx.datetime.DateTimeUnit.DAY).toString()
    private val thirtyTwoDaysAgo = Clock.System.todayIn(TimeZone.currentSystemDefault()).minus(32, kotlinx.datetime.DateTimeUnit.DAY).toString()
    private val fiftyDaysAgo = Clock.System.todayIn(TimeZone.currentSystemDefault()).minus(50, kotlinx.datetime.DateTimeUnit.DAY).toString()
    private val oneHundredDaysAgo = Clock.System.todayIn(TimeZone.currentSystemDefault()).minus(100, kotlinx.datetime.DateTimeUnit.DAY).toString()

    private val band1Album1 = MusicEntry(1, "Band 1", "Album 1", 5, today, "Genre 1")
    private val band1Album2 = MusicEntry(2, "Band 1", "Album 2", 3, eightDaysAgo,"Genre 1")
    private val band2Album1 = MusicEntry(3, "Band 2", "Album 1", 4, fiveDaysAgo, "Genre 2")
    private val band2Album2 = MusicEntry(4, "Band 2", "Album 2", 2, thirtyTwoDaysAgo, "Genre 2")
    private val band1Album2Past = MusicEntry(5, "Band 1", "Album 2", 3, fiftyDaysAgo, "Genre 1")
    private val band1Album3 = MusicEntry(6, "Band 1", "Album 3", 5, thirtyTwoDaysAgo, "Genre 1")
    private val band1Album3Past = MusicEntry(7, "Band 1", "Album 3", 5, oneHundredDaysAgo, "Genre 1")

    ////////////////////// SETUP & TEARDOWN //////////////////////

    @Before
    fun createDb() {
        val context: Context = ApplicationProvider.getApplicationContext()

        db = Room.inMemoryDatabaseBuilder(context, MusicDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        musicDao = db.musicDao()
    }

    @After
    fun closeDb() {
        db.close()
    }

    ////////////////////// TESTS //////////////////////

    @Test
    fun daoInsertMusic_insertsItemIntoDB() = runBlocking {
        musicDao.insertMusic(band1Album1)
        val allMusic = musicDao.getAllMusic().first()
        assertEquals(allMusic[0], band1Album1)
    }

    @Test
    fun daoGetMusicEntry_returnsCorrectItem() = runBlocking {
        musicDao.insertMusic(band1Album1)
        val musicEntry = musicDao.getMusicEntry(1).first()
        assertEquals(musicEntry, band1Album1)
    }

    @Test
    fun daoGetAllMusic_returnsAllItemsFromDB() = runBlocking {
        musicDao.insertMusic(band1Album1)
        musicDao.insertMusic(band1Album2)
        musicDao.insertMusic(band2Album1)
        musicDao.insertMusic(band2Album2)
        musicDao.insertMusic(band1Album3)
        musicDao.insertMusic(band1Album2Past)

        val allMusic = musicDao.getAllMusic().first()
        assertEquals(allMusic[0], band1Album1)
        assertEquals(allMusic[1], band1Album2)
        assertEquals(allMusic[2], band2Album1)
        assertEquals(allMusic[3], band2Album2)
        assertEquals(allMusic[4], band1Album3)
        assertEquals(allMusic[5], band1Album2Past)
    }

    @Test
    fun daoUpdateMusic_updatesEntryInDB() = runBlocking {
        musicDao.insertMusic(band1Album1)
        val band1Album1Updated = MusicEntry(1, "Band 1", "Album 1", 4, "2024-11-29", "Genre 1 updated!")
        musicDao.updateMusic(band1Album1Updated)
        val allMusic = musicDao.getAllMusic().first()
        assertEquals(allMusic[0], band1Album1Updated)
    }

    @Test
    fun daoGetAllMusicHistory_getsAlbumsInOrder() = runBlocking {
        musicDao.insertMusic(band1Album1)
        musicDao.insertMusic(band1Album2)
        musicDao.insertMusic(band2Album1)
        musicDao.insertMusic(band2Album2)
        musicDao.insertMusic(band1Album3)
        musicDao.insertMusic(band1Album2Past)

        val allMusicHistory = musicDao.getAllMusicHistory().first()
        assertEquals(allMusicHistory[0], band1Album1)
        assertEquals(allMusicHistory[1], band2Album1)
        assertEquals(allMusicHistory[2], band1Album2)
        assertEquals(allMusicHistory[3], band2Album2)
        assertEquals(allMusicHistory[4], band1Album3)
        assertEquals(allMusicHistory[5], band1Album2Past)
    }

    @Test
    fun daoGetAllMusicToListenTo_hasAlbumFromOverAMonthAgo() = runBlocking {
        musicDao.insertMusic(band1Album2Past)
        val music = musicDao.getAllMusicToListenTo().first()
        assertTrue(music.contains(band1Album2Past))
    }

    @Test
    fun daoGetAllMusicToListenTo_noAlbumFromToday() = runBlocking {
        musicDao.insertMusic(band1Album1)
        val music = musicDao.getAllMusicToListenTo().first()
        assertFalse(music.contains(band1Album1))
    }

    @Test
    fun daoGetAllMusicToListenTo_noAlbumFromOverAWeekAgo() = runBlocking {
        musicDao.insertMusic(band1Album2)
        val music = musicDao.getAllMusicToListenTo().first()
        assertFalse(music.contains(band1Album2))
    }

    @Test
    fun daoGetAllMusicToListenTo_noAlbumFromOverAMonthAgoWhenArtistLessThanAWeek() = runBlocking {
        musicDao.insertMusic(band2Album1)
        musicDao.insertMusic(band2Album2)
        val music = musicDao.getAllMusicToListenTo().first()
        assertFalse(music.contains(band2Album1))
        assertFalse(music.contains(band2Album2))
    }

    @Test
    fun daoGetAllMusicToListenTo_noAlbumFromOverAMonthAgoWhenAlbumAlsoLessThanAMonth() = runBlocking {
        musicDao.insertMusic(band1Album2)
        musicDao.insertMusic(band1Album2Past)
        val music = musicDao.getAllMusicToListenTo().first()
        assertFalse(music.contains(band1Album2))
        assertFalse(music.contains(band1Album2Past))
    }

    @Test
    fun daoGetAllMusicToListenTo_hasAlbumFromArtistOverAWeekAgoAndAlbumOverAMonth() = runBlocking {
        musicDao.insertMusic(band1Album3)
        musicDao.insertMusic(band1Album2)
        val music = musicDao.getAllMusicToListenTo().first()
        assertTrue(music.contains(band1Album3))
        assertFalse(music.contains(band1Album2))
    }

    @Test
    fun daoGetAllMusicToListenTo_hasMostRecentAlbumWhenListenedMultipleTimesOverAMonthAgo() = runBlocking {
        musicDao.insertMusic(band1Album3)
        musicDao.insertMusic(band1Album3Past)
        val music = musicDao.getAllMusicToListenTo().first()
        assertTrue(music.contains(band1Album3))
        assertFalse(music.contains(band1Album3Past))
    }
}