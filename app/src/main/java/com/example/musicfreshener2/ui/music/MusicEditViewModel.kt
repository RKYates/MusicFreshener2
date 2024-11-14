package com.example.musicfreshener2.ui.music

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicfreshener2.data.MusicEntry
import com.example.musicfreshener2.data.MusicRepository
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.todayIn

class MusicEditViewModel(
    savedStateHandle: SavedStateHandle,
    private val musicRepository: MusicRepository
) : ViewModel() {

    /**
     * Holds current music entry ui state
     */
    var musicUiState by mutableStateOf(MusicUiState())
        private set

    private val musicId: Int = checkNotNull(savedStateHandle[MusicEditDestination.musicIdArg])

    init {
        viewModelScope.launch {
            musicUiState = musicRepository.getMusicStream(musicId)
                .filterNotNull()
                .first()
                .toMusicUiState(true)
        }
    }

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

    suspend fun updateMusic() {
        if (validateInput()) {
            musicRepository.updateMusic(musicUiState.musicDetails.toMusicEntry())
        }
    }

    suspend fun addNewListen() {
        if (validateInput()) {
            musicRepository.insertMusic(musicUiState.musicDetails.toNewMusicListen())
        }
    }
}

/**
 * Extension function to convert [MusicDetails] to [MusicEntry] but with a new ID and today's date.
 */
fun MusicDetails.toNewMusicListen(): MusicEntry = MusicEntry(
    artist = artist.trim(),
    album = album.trim(),
    rating = rating.trim().toIntOrNull() ?: 0,
    date = Clock.System.todayIn(TimeZone.currentSystemDefault()).format(dateFormat),
    genre = genre.trim()
)