package com.example.musicfreshener2.ui


import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.musicfreshener2.MusicApplication
import com.example.musicfreshener2.ui.home.HomeViewModel
import com.example.musicfreshener2.ui.music.MusicAddViewModel

object AppViewModelProvider {
    val Factory = viewModelFactory {

        initializer {
            HomeViewModel(musicApplication().container.musicRepository)
        }

        initializer {
            MusicAddViewModel(musicApplication().container.musicRepository)
        }
    }
}

fun CreationExtras.musicApplication(): MusicApplication =
    (this[AndroidViewModelFactory.APPLICATION_KEY] as MusicApplication)