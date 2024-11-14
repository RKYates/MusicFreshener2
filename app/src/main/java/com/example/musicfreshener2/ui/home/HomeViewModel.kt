package com.example.musicfreshener2.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicfreshener2.data.MusicEntry
import com.example.musicfreshener2.data.MusicRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

/**
 * ViewModel to retrieve all music from the Room DB.
 */
class HomeViewModel(musicRepository: MusicRepository) : ViewModel() {
    val homeUiState: StateFlow<HomeUiState> =
        musicRepository.getAllMusicStream().map { HomeUiState(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = HomeUiState()
            )

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}

data class HomeUiState(val musicList: List<MusicEntry> = listOf())