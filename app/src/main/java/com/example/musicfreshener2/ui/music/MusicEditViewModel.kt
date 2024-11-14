package com.example.musicfreshener2.ui.music

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicfreshener2.data.MusicRepository
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

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
}