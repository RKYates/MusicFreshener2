package com.example.musicfreshener2.ui.music

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.musicfreshener2.AppTopBar
import com.example.musicfreshener2.R
import com.example.musicfreshener2.ui.AppViewModelProvider
import com.example.musicfreshener2.ui.navigation.NavigationDestination
import kotlinx.coroutines.launch

const val TEST_TAG_ARTIST_INPUT = "artist input"
const val TEST_TAG_ALBUM_INPUT = "album input"
const val TEST_TAG_GENRE_INPUT = "genre input"
const val TEST_TAG_DATE_INPUT = "date input"
const val TEST_TAG_RATING_INPUT = "rating input"

object MusicAddDestination : NavigationDestination {
    override val route = "add_music"
    override val titleRes = R.string.add_entry
}

@Composable
fun MusicAddScreen(
    navigateBack: () -> Unit,
    onNavigateUp: () -> Unit,
    viewModel: MusicAddViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            AppTopBar(
                title = stringResource(MusicAddDestination.titleRes),
                canNavigateBack = true,
                navigateUp = onNavigateUp
            )
        }
    ) { innerPadding ->
        MusicAddBody(
            viewModel.musicUiState,
            onMusicValueChange = viewModel::updateUiState,
            onSaveClick = {
                coroutineScope.launch {
                    viewModel.addEntry()
                }
                navigateBack()
            } ,
            R.string.add_album,
            modifier = Modifier.padding(innerPadding)
        )
    }
}

@Composable
fun MusicAddBody(
    musicUiState: MusicUiState,
    onMusicValueChange: (MusicDetails) -> Unit,
    onSaveClick: () -> Unit,
    buttonTextRes: Int,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(32.dp)
    ) {
        Column {
            MusicAddForm(
                musicDetails = musicUiState.musicDetails,
                onValueChange = onMusicValueChange
            )
            Button(
                onClick = onSaveClick,
                enabled = musicUiState.isEntryValid
            ) {
                Text(stringResource(buttonTextRes))
            }
        }
    }
}

@Composable
fun MusicAddForm(
    musicDetails: MusicDetails,
    modifier: Modifier = Modifier,
    onValueChange: (MusicDetails) -> Unit = {}
) {
    Column (
        modifier = modifier.padding(8.dp)
    ) {
        TextField(
            value = musicDetails.artist,
            onValueChange = { onValueChange(musicDetails.copy(artist = it)) },
            label = { Text(stringResource(R.string.artist)) },
            modifier = Modifier
                .padding(8.dp)
                .testTag(TEST_TAG_ARTIST_INPUT),
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = androidx.compose.ui.text.input.ImeAction.Next)
        )
        TextField(
            value = musicDetails.album,
            onValueChange = { onValueChange(musicDetails.copy(album = it)) },
            label = { Text(stringResource(R.string.album)) },
            modifier = Modifier
                .padding(8.dp)
                .testTag(TEST_TAG_ALBUM_INPUT),
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = androidx.compose.ui.text.input.ImeAction.Next)
        )
        TextField(
            value = musicDetails.genre,
            onValueChange = { onValueChange(musicDetails.copy(genre = it)) },
            label = { Text(stringResource(R.string.genre)) },
            modifier = Modifier
                .padding(8.dp)
                .testTag(TEST_TAG_GENRE_INPUT),
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = androidx.compose.ui.text.input.ImeAction.Next)
        )
        TextField(
            value = musicDetails.date,
            onValueChange = { onValueChange(musicDetails.copy(date = it)) },
            label = { Text(stringResource(R.string.date)) },
            modifier = Modifier
                .padding(8.dp)
                .testTag(TEST_TAG_DATE_INPUT),
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = androidx.compose.ui.text.input.ImeAction.Next
            )
        )
        TextField(
            value = musicDetails.rating,
            onValueChange = { onValueChange(musicDetails.copy(rating = it)) },
            label = { Text(stringResource(R.string.rating)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true,
            modifier = Modifier
                .padding(8.dp)
                .testTag(TEST_TAG_RATING_INPUT)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun MusicAddBodyPreview() {
    MusicAddBody(
        musicUiState = MusicUiState(
            musicDetails = MusicDetails(
                artist = "BAND",
                album = "ALBUM",
                genre = "GENRE",
                date = "1/1/2001",
                rating = "5"
            ),
            isEntryValid = true
        ),
        onMusicValueChange = {},
        onSaveClick = {},
        buttonTextRes = R.string.add_album
    )
}