package com.example.musicfreshener2.e2e.tests

import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Tests for adding music entries to the list.
 */
class AddMusicTests : TestCase() {
    /**
     * Ensures that an entry set on today is added, doesn't show on To Listen To, and does show
     * on history.
     */
    @Test
    fun addEntryToday() = runBlocking {
        val entry = makeRandomEntry()

        val addPage = home.goToAddMusic()
        home = addPage.addEntry(entry)

        assertFalse("Today's entry shows on To Listen To after adding.",
            home.doesEntryShow(entry))

        home.toggleHistory()
        assertTrue("Today's entry doesn't show on history after adding.",
            home.doesEntryShow(entry))
    }

    /**
     * Ensures that an entry set on the past is added and does show on To Listen To.
     */
    @Test
    fun addPastEntry() {
        val entry = makeRandomEntry(DateType.MONTH_OR_MORE)

        val addPage = home.goToAddMusic()
        home = addPage.addEntry(entry)
        assertTrue("Past entry doesn't show on To Listen To after adding.",
            home.doesEntryShow(entry))
    }
}