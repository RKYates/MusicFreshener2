package com.example.musicfreshener2.ui.music

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.musicfreshener2.data.MusicEntry
import com.example.musicfreshener2.data.MusicRepository
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.format.char
import kotlinx.datetime.todayIn


class MusicAddViewModel(private val musicRepository: MusicRepository) : ViewModel() {

    /**
     * Holds current music entry ui state
     */
    var musicUiState by mutableStateOf(MusicUiState())
        private set

    /**
     * Updates [musicUiState] with the value provided. Also triggers input validation.
     */
    fun updateUiState(musicDetails: MusicDetails) {
        musicUiState = MusicUiState(
                musicDetails = musicDetails,
                isEntryValid = validateInput(musicDetails)
        )
    }

    private fun validateInput(uiState: MusicDetails = musicUiState.musicDetails): Boolean {
        return with(uiState) {
            artist.isNotBlank() && album.isNotBlank() && rating.isNotBlank() && date.isNotBlank() &&
                    genre.isNotBlank()
        }
    }

    suspend fun addEntry() {
        if (validateInput()) {
            musicRepository.insertMusic(musicUiState.musicDetails.toMusicEntry())
        }
    }
}

data class MusicUiState(
    val musicDetails: MusicDetails = MusicDetails(),
    val isEntryValid: Boolean = false
)

data class MusicDetails (
    val id: Int = 0,
    val artist: String = "",
    val album: String = "",
    val rating: String = "",
    val date: String = Clock.System.todayIn(TimeZone.currentSystemDefault()).toString(),
    val genre: String = ""
)

/**
 * Extension function to convert [MusicDetails] to [MusicEntry]
 */
fun MusicDetails.toMusicEntry(): MusicEntry = MusicEntry(
    id = id,
    artist = artist.trim(),
    album = album.trim(),
    rating = rating.trim().toIntOrNull() ?: 0,
    date = date.trim(),
    genre = genre.trim()
)

/**
 * Extension function to convert [MusicEntry] to [MusicUiState]
 */
fun MusicEntry.toMusicUiState(isEntryValid: Boolean = false): MusicUiState = MusicUiState(
    musicDetails = this.toMusicDetails(),
    isEntryValid = isEntryValid
)

/**
 * Extension function to convert [MusicEntry] to [MusicDetails]
 */
fun MusicEntry.toMusicDetails(): MusicDetails = MusicDetails(
    id = id,
    artist = artist,
    album = album,
    rating = rating.toString(),
    date = date,
    genre = genre
)