package com.example.musicfreshener2.ui.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.musicfreshener2.AppTopBar
import com.example.musicfreshener2.MusicApplication
import com.example.musicfreshener2.R
import com.example.musicfreshener2.data.MusicEntry
import com.example.musicfreshener2.ui.AppViewModelProvider
import com.example.musicfreshener2.ui.navigation.NavigationDestination
import com.example.musicfreshener2.ui.home.SortingType.TO_LISTEN_TO
import com.example.musicfreshener2.ui.home.SortingType.HISTORY
import kotlinx.coroutines.launch

object HomeDestination : NavigationDestination {
    override val route = "home"
    override val titleRes = R.string.title_to_listen_to
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navigateToAddMusic: () -> Unit,
    navigateToEditMusic: (Int) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val homeUiState by viewModel.homeUiState.collectAsState()
    val scope = rememberCoroutineScope()
    val optionsSheetState = rememberModalBottomSheetState()
    var showOptionsSheet by remember { mutableStateOf(false) }

    Scaffold(
        bottomBar = { BottomBar(
            navigateToAddMusic = navigateToAddMusic,
            showOptionsSheet = { showOptionsSheet = true }
        )},
        topBar = { AppTopBar(
            title = stringResource(HomeDestination.titleRes),
            canNavigateBack = false
        )},
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->

        MusicBody(
            entries = homeUiState.musicList,
            onEntryClick = navigateToEditMusic,
            modifier = modifier,
            contentPadding = innerPadding
        )

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
                                //TODO make this work realtime
//                                viewModel.sortMusic(if (checked) TO_LISTEN_TO else HISTORY)
                                viewModel.viewModelScope.launch {
                                    viewModel.sortMusic(if (checked) TO_LISTEN_TO else HISTORY)
                                }
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
fun BottomBar(
    navigateToAddMusic: () -> Unit,
    showOptionsSheet: () -> Unit,
    modifier: Modifier = Modifier
) {
    BottomAppBar(
        actions = {
            IconButton(
                onClick = showOptionsSheet
            ) {
                Icon(
                    imageVector = Icons.Filled.Menu,
                    contentDescription = MusicApplication.appContext.getString(R.string.menu_title))
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = navigateToAddMusic,
                containerColor = BottomAppBarDefaults.bottomAppBarFabColor,
                elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation()
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = MusicApplication.appContext.getString(R.string.add_entry))
            }
        },
        modifier = modifier
    )
}

/**
 * Displays either the list of music entries or an empty message.
 */
@Composable
fun MusicBody(
    entries: List<MusicEntry>,
    onEntryClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    Column (
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .padding(contentPadding)
    ) {
        if (entries.isEmpty()) {
            Text(
                text = stringResource(R.string.no_music_description),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary
            )
        } else {
            MusicList(
                entries = entries,
                onEntryClick = onEntryClick,
                innerPadding = contentPadding
            )
        }
    }
}

@Composable
fun MusicList(
    entries: List<MusicEntry>,
    onEntryClick: (Int) -> Unit,
    innerPadding: PaddingValues
) {
    LazyColumn (
        contentPadding = innerPadding
    ) {
        items(entries) { row ->
            MusicRow(
                row,
                Modifier.clickable {
                    onEntryClick(row.id)
                }
            )
        }
    }
}

@Composable
fun MusicRow(
    entry: MusicEntry,
    modifier: Modifier = Modifier
) {
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        modifier = modifier
            .padding(8.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(8.dp)
        ) {
            Column(
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
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(text = entry.date)
                Text(text = entry.genre)
            }
        }
    }
}

////////////////// PREVIEWS //////////////////

private val testMusicList = listOf(
    MusicEntry(
        artist = "The Dance with the Dead",
        album = "The Loved to Death",
        rating = 5,
        date = "10/05/2024",
        genre = "Synthwave"
    ),
    MusicEntry(
        artist = "Wind Rose",
        album = "Trollslayer",
        rating = 5,
        date = "10/04/2024",
        genre = "Power Metal"
    )
)

@Preview
@Composable
fun EmptyListPreview() {
    MusicBody(
        entries = listOf(),
        onEntryClick = {},
        contentPadding = PaddingValues(0.dp)
    )
}

@Preview(showBackground = true)
@Composable
fun MusicListPreview() {
    MusicList(
        entries = testMusicList,
        onEntryClick = {},
        innerPadding = PaddingValues(0.dp)
    )
}

@Preview(showBackground = true)
@Composable
fun MusicRowPreview() {
    MusicRow(
        entry = testMusicList[0]
    )
}
