package com.example.musicfreshener2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.musicfreshener2.ui.theme.MusicFreshener2Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
        setContent {
            MusicFreshener2Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MusicList()
                }
            }
        }
    }
}

@Composable
fun MusicList() {
    val musicList = listOf<MusicEntry>(
        MusicEntry("Dance with the Dead", "Loved to Death", 5, "10/05/2024", "Synthwave"),
        MusicEntry("Wind Rose", "Trollslayer", 5, "10/04/2024", "Power Metal")
    )

    LazyColumn {
        items(musicList) { row ->
            MusicRow(row)
        }

    }
}

@Composable
fun MusicRow(entry: MusicEntry) {
    Row (
        modifier = Modifier
            .padding(8.dp)
    ) {
        Column (
            modifier = Modifier.weight(1f)
        ) {
            Text(text = entry.artist)
            Text(text = entry.album)
        }
        Text(
            text = entry.rating.toString(),
            fontSize = 40.sp,
            modifier = Modifier
                .weight(.2f)
        )
        Column (
            modifier = Modifier.weight(1f)
        ) {
            Text(text = entry.date)
            Text(text = entry.genre)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun Preview() {
    MusicFreshener2Theme {
        MusicRow(MusicEntry("BAND", "ALBUM", 5, "01/01/2001", "GENRE"))
    }
}