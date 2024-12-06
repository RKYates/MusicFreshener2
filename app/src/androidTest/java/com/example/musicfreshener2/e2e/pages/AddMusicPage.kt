package com.example.musicfreshener2.e2e.pages

import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.performTextReplacement
import com.example.musicfreshener2.R
import com.example.musicfreshener2.data.MusicEntry
import com.example.musicfreshener2.ui.music.TEST_TAG_ALBUM_INPUT
import com.example.musicfreshener2.ui.music.TEST_TAG_ARTIST_INPUT
import com.example.musicfreshener2.ui.music.TEST_TAG_DATE_INPUT
import com.example.musicfreshener2.ui.music.TEST_TAG_GENRE_INPUT
import com.example.musicfreshener2.ui.music.TEST_TAG_RATING_INPUT

/**
 * Represents the page where a new music entry can be added.
 */
class AddMusicPage(composeTestRule: ComposeTestRule) : Page(composeTestRule) {
    init {
        composeTestRule.onNodeWithText(R.string.add_entry).assertExists()
    }

    /**
     * Adds a new music entry and takes the user back to the home page.
     */
    fun addEntry(entry: MusicEntry): HomePage {
        return addEntry(entry.album, entry.artist, entry.genre, entry.rating, entry.date)
    }

    /**
     * Adds a new music entry and takes the user back to the home page.
     */
    fun addEntry(
        album: String,
        artist: String,
        genre: String,
        rating: Int,
        date: String = ""
    ): HomePage {
        if (date.isNotBlank()) composeTestRule.onNodeWithTag(TEST_TAG_DATE_INPUT).performTextReplacement(date)
        composeTestRule.apply {
            onNodeWithTag(TEST_TAG_ARTIST_INPUT).performTextInput(artist)
            onNodeWithTag(TEST_TAG_ALBUM_INPUT).performTextInput(album)
            onNodeWithTag(TEST_TAG_GENRE_INPUT).performTextInput(genre)
            onNodeWithTag(TEST_TAG_RATING_INPUT).performTextInput(rating.toString())
            onNodeWithText(R.string.add_album).performClick()
        }
        return HomePage(composeTestRule)
    }
}