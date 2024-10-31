package com.example.musicfreshener2

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.musicfreshener2.ui.theme.MusicFreshener2Theme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MusicFreshener2Theme {
                ShowScreen()
            }
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun ShowScreen() {

    val scope = rememberCoroutineScope()
    val addSheetState = rememberModalBottomSheetState()
    var showAddBottomSheet by remember { mutableStateOf(false) }
    val optionsSheetState = rememberModalBottomSheetState()
    var showOptionsSheet by remember { mutableStateOf(false) }

    Scaffold(
        bottomBar = {
            BottomAppBar(
                actions = {
                    IconButton(
                        onClick = { showOptionsSheet = true }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Menu,
                            contentDescription = App.appContext.getString(R.string.menu_title))
                    }
                },
                floatingActionButton = {
                    FloatingActionButton(
                        onClick = { showAddBottomSheet = true },
                        containerColor = BottomAppBarDefaults.bottomAppBarFabColor,
                        elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation()
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Add,
                            contentDescription = App.appContext.getString(R.string.add_entry))
                    }
                }
            )
        },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->

        MusicList(innerPadding)

        if (showAddBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = {
                    showAddBottomSheet = false
                },
                sheetState = addSheetState
            ) {
                // add Sheet content
                var artist by remember { mutableStateOf("") }
                var album by remember { mutableStateOf("") }
                var rating by remember { mutableStateOf("") }
                var date by remember { mutableStateOf("") }
                var genre by remember { mutableStateOf("") }

                Column (
                    modifier = Modifier.padding(8.dp)
                ) {
                    TextField(
                        value = artist,
                        onValueChange = { artist = it },
                        label = { Text(stringResource(R.string.artist)) },
                        modifier = Modifier.padding(8.dp),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(imeAction = androidx.compose.ui.text.input.ImeAction.Next)
                    )
                    TextField(
                        value = album,
                        onValueChange = { album = it },
                        label = { Text(stringResource(R.string.album)) },
                        modifier = Modifier.padding(8.dp),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(imeAction = androidx.compose.ui.text.input.ImeAction.Next)
                    )
                    TextField(
                        value = genre,
                        onValueChange = { genre = it },
                        label = { Text(stringResource(R.string.genre)) },
                        modifier = Modifier.padding(8.dp),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(imeAction = androidx.compose.ui.text.input.ImeAction.Next)
                    )
                    TextField( // TODO make app scroll to this when focus is set to it
                        value = date,
                        onValueChange = { date = it },
                        label = { Text(stringResource(R.string.date)) },
                        modifier = Modifier.padding(8.dp),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(imeAction = androidx.compose.ui.text.input.ImeAction.Next)
                    )
                    TextField( // TODO make app scroll to this when focus is set to it
                        value = rating,
                        onValueChange = { rating = it },
                        label = { Text(stringResource(R.string.rating)) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        modifier = Modifier.padding(8.dp)
                    )
                    Button(
                        onClick = {
                            addEntry(
                                MusicEntry(
                                    artist.trim(),
                                    album.trim(),
                                    rating.toInt(),
                                    date,
                                    genre.trim()
                                )
                            )
                            scope.launch { addSheetState.hide() }.invokeOnCompletion {
                                if (!addSheetState.isVisible) {
                                    showAddBottomSheet = false
                                }
                            }
                            // TODO re-display list with updates for new row .. another mutable var?
                        },
                        modifier = Modifier
                            .padding(bottom = 100.dp) // TODO make this go higher in a better way
                            .align(Alignment.End)
                    ) {
                        Text(stringResource(R.string.add_album))
                    }
                }
            }
        }

        if (showOptionsSheet) {
            ModalBottomSheet(
                onDismissRequest = {
                    showOptionsSheet = false
                },
                sheetState = optionsSheetState
            ) {
                // options Sheet content
                var filterText by remember { mutableStateOf("") }
                val toListenText = stringResource(R.string.title_to_listen_to)
                val historyText = stringResource(R.string.history)
                var toggleText by remember { mutableStateOf(toListenText) }
                var checked by remember { mutableStateOf(true) }

                Column (
                    modifier = Modifier.padding(8.dp)
                ) {
                    Row (
                        modifier = Modifier.padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(toggleText)
                        Switch(
                            checked = checked,
                            onCheckedChange = {
                                checked = it
                                toggleText = if (checked) toListenText else historyText
                            },
                            modifier = Modifier.padding(start = 16.dp)
                        )
                    }
                    Row (
                        modifier = Modifier.padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(stringResource(R.string.filter_by_text))
                        TextField(
                            value = filterText,
                            onValueChange = { filterText = it },
                            modifier = Modifier.padding(start = 16.dp),
                            singleLine = true
                        )
                    }
                    Button(
                        onClick = {
                            scope.launch { optionsSheetState.hide() }.invokeOnCompletion {
                                if (!optionsSheetState.isVisible) {
                                    showOptionsSheet = false
                                }
                            }
                            // TODO re-display list with filter applied .. another mutable var?
                        },
                        modifier = Modifier
                            .padding(bottom = 100.dp) // TODO make this go higher in a better way
                            .align(Alignment.End)
                    ) {
                        Text(stringResource(R.string.done))
                    }
                }
            }
        }
    }
}

@Composable
fun BottomBar() {
    BottomAppBar(
        actions = {
            IconButton(onClick = { /* do something */ }) {
                Icon(
                    imageVector = Icons.Filled.Menu,
                    contentDescription = App.appContext.getString(R.string.menu_title))
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { /* do something */ },
                containerColor = BottomAppBarDefaults.bottomAppBarFabColor,
                elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation()
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = App.appContext.getString(R.string.add_entry))
            }
        }
    )
}

@Composable
fun MusicList(innerPadding: PaddingValues) {
    val musicList = listOf(
        MusicEntry("Dance with the Dead", "Loved to Death", 5, "10/05/2024", "Synthwave"),
        MusicEntry("Wind Rose", "Trollslayer", 5, "10/04/2024", "Power Metal")
    )

    LazyColumn (
        contentPadding = innerPadding
    ) {
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

fun addEntry(entry: MusicEntry) {
    //TODO: Add entry to list and refresh
    Log.d("MusicFreshener2", "addEntry: $entry")
}

@Preview(showBackground = true)
@Composable
fun Preview() {
    MusicFreshener2Theme {
        MusicRow(MusicEntry("BAND", "ALBUM", 5, "01/01/2001", "GENRE"))
    }
}