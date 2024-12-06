package com.example.musicfreshener2.e2e.pages

import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import com.example.musicfreshener2.R
import com.example.musicfreshener2.data.MusicEntry
import com.example.musicfreshener2.ui.home.TEST_TAG_ADD_MUSIC_FAB
import com.example.musicfreshener2.ui.home.TEST_TAG_BOTTOM_SHEET_BUTTON
import com.example.musicfreshener2.ui.home.TEST_TAG_HISTORY_TOGGLE
import com.example.musicfreshener2.ui.home.TEST_TAG_HOME_SCREEN_BODY
import com.example.musicfreshener2.ui.home.makeEntryTestTag

/**
 * Represents the home page of the app, with the list of music entries.
 */
class HomePage(composeTestRule: ComposeTestRule) : Page(composeTestRule) {
    init {
        composeTestRule.onNodeWithTag(TEST_TAG_HOME_SCREEN_BODY).assertExists()
    }

    /**
     * Goes to the add music page.
     */
    fun goToAddMusic(): AddMusicPage {
        composeTestRule.onNodeWithTag(TEST_TAG_ADD_MUSIC_FAB).performClick()
        return AddMusicPage(composeTestRule)
    }

    /**
     * Toggles whether the list should show history or to listen to.
     */
    fun toggleHistory() {
        composeTestRule.apply {
            onNodeWithTag(TEST_TAG_BOTTOM_SHEET_BUTTON).performClick()
            onNodeWithTag(TEST_TAG_HISTORY_TOGGLE).performClick()
            onNodeWithText(R.string.done).performClick()
        }
    }

    fun doesEntryShow(entry: MusicEntry): Boolean {
        val tag = makeEntryTestTag(entry)
        val tagFromInspector = ""
        return exists(hasTestTag(makeEntryTestTag(entry)))
    }
}