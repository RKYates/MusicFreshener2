package com.example.musicfreshener2.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicfreshener2.data.MusicEntry
import com.example.musicfreshener2.data.MusicRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

/**
 * ViewModel to retrieve all music from the Room DB.
 */
class HomeViewModel(private val musicRepository: MusicRepository) : ViewModel() {
    var homeUiState: StateFlow<HomeUiState> = getSortedMusicStream()

    private fun getSortedMusicStream(
        sortingType: SortingType = SortingType.TO_LISTEN_TO
    ): StateFlow<HomeUiState> {
        return musicRepository.getAllMusicStream(sortingType).map { HomeUiState(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = HomeUiState()
            )
    }

    fun sortMusic(sortingType: SortingType) {
        homeUiState = getSortedMusicStream(sortingType)
    }

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}

enum class SortingType { HISTORY, TO_LISTEN_TO }

data class HomeUiState(val musicList: List<MusicEntry> = listOf())