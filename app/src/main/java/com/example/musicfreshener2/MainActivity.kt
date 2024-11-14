package com.example.musicfreshener2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.musicfreshener2.ui.theme.MusicFreshener2Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MusicFreshener2Theme {
                MusicApp()
            }
        }
    }
}
