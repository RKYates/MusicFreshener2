package com.example.musicfreshener2.ui.music

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.musicfreshener2.AppTopBar
import com.example.musicfreshener2.R
import com.example.musicfreshener2.ui.AppViewModelProvider
import com.example.musicfreshener2.ui.navigation.NavigationDestination
import kotlinx.coroutines.launch

object MusicEditDestination : NavigationDestination {
    override val route = "music_edit"
    override val titleRes = R.string.title_music_edit
    const val musicIdArg = "musicId"
    val routeWithArgs = "$route/{$musicIdArg}"
}

@Composable
fun MusicEditScreen(
    navigateBack: () -> Unit,
    onNavigateUp: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: MusicEditViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            AppTopBar(
                title = stringResource(MusicEditDestination.titleRes),
                canNavigateBack = true,
                navigateUp = onNavigateUp
            )
        },
        modifier = modifier
    ) { innerPadding ->
        Column (
            modifier = modifier
//                .fillMaxWidth()
                .padding(16.dp)
        ) {
            MusicAddBody(
                musicUiState = viewModel.musicUiState,
                onMusicValueChange = viewModel::updateUiState,
                onSaveClick = {
                    coroutineScope.launch {
                        viewModel.updateMusic()
                        navigateBack()
                    }
                },
                R.string.update,
                modifier = Modifier.padding(innerPadding)
            )
            Button(
                onClick = {
                    coroutineScope.launch {
                        viewModel.addNewListen()
                        navigateBack()
                    }
                },
                enabled = viewModel.musicUiState.isEntryValid
            ) {
                Text(stringResource(R.string.add_listen_now))
            }
        }
    }
}

@Preview
@Composable
fun MusicEditScreenPreview() {
    MusicEditScreen(navigateBack = { /*Do nothing*/ }, onNavigateUp = { /*Do nothing*/ })
}