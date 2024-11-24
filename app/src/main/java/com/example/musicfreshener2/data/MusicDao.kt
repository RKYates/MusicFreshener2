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

    @Query("select * " +
            "from music_entries m " +
            "group by artist, album " +
            "having " +
            "  date = max(date) " +
            "  and" +
            "  artist not in (" +
            "    select distinct artist " +
            "    from music_entries " +
            "    where julianday('now') - julianday(date) < 7" +
            "  ) " +
            "  and " +
            "  album not in ( " +
            "    select distinct album " +
            "    from music_entries mAlbum " +
            "    where julianday('now') - julianday(date) < 30 " +
            "    and mAlbum.artist = m.artist " +
            "  ) " +
            "order by rating desc, date(date) desc")
    fun getAllMusicToListenTo(): Flow<List<MusicEntry>>

    @Query("SELECT * FROM music_entries order by date desc")
    fun getAllMusicHistory(): Flow<List<MusicEntry>>

    @Query("SELECT * FROM music_entries WHERE id = :id")
    fun getMusicEntry(id: Int): Flow<MusicEntry>
}