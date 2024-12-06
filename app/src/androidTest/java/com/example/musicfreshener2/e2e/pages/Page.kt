package com.example.musicfreshener2.e2e.pages

import androidx.annotation.IdRes
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.onNodeWithText
import com.example.musicfreshener2.MusicApplication

open class Page(val composeTestRule: ComposeTestRule) {
    /**
     * Determines if a specified Compose matcher currently exists in the UI.
     */
    fun exists(
        matcher: SemanticsMatcher,
        useUnmergedTree: Boolean = true
    ): Boolean {
        try {
            composeTestRule.onNode(matcher, useUnmergedTree).assertExists()
            return true
        } catch (e: AssertionError) {
            return false
        }
    }
}

/**
 * Extension function to [ComposeTestRule.onNodeWithText] that uses the string resource for
 * convenience instead of a hardcoded string.
 */
fun ComposeTestRule.onNodeWithText(
    @IdRes id: Int,
    substring: Boolean = false,
    ignoreCase: Boolean = false,
    useUnmergedTree: Boolean = false
): SemanticsNodeInteraction {
    val text = MusicApplication.appContext.getString(id)
    return onNodeWithText(
        text = text,
        substring = substring,
        ignoreCase = ignoreCase,
        useUnmergedTree = useUnmergedTree
    )
}