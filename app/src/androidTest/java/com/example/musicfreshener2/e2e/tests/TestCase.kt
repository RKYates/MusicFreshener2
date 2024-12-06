package com.example.musicfreshener2.e2e.tests

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.ext.junit.rules.activityScenarioRule
import androidx.test.filters.LargeTest
import com.example.musicfreshener2.MainActivity
import com.example.musicfreshener2.data.MusicEntry
import com.example.musicfreshener2.e2e.pages.HomePage
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.todayIn
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith
import java.util.UUID
import kotlin.random.Random

@RunWith(AndroidJUnit4::class)
open class TestCase {
    lateinit var home: HomePage

    @get:Rule(order = 0) var composeTestRule = createComposeRule()
    @get:Rule(order = 1) var activityScenarioRule = activityScenarioRule<MainActivity>()

    @Before
    fun setup() {
        home = HomePage(composeTestRule)
    }

    /**
     * Helper function to make a randomized [MusicEntry].
     * @param dateType The type of date to use for the entry.
     */
    fun makeRandomEntry(dateType: DateType = DateType.TODAY_BLANK): MusicEntry {
        val today = Clock.System.todayIn(TimeZone.currentSystemDefault())

        return MusicEntry(
            artist = UUID.randomUUID().toString(),
            album = UUID.randomUUID().toString(),
            rating = Random.nextInt(1, 6),
            genre = UUID.randomUUID().toString(),
            date = when (dateType) {
                DateType.TODAY_BLANK -> ""
                DateType.TODAY_EXPLICIT -> today.toString()
                DateType.LESS_THAN_A_WEEK -> today.minus(Random.nextInt(1, 7), DateTimeUnit.DAY).toString()
                DateType.LESS_THAN_A_MONTH -> today.minus(Random.nextInt(1, 30), DateTimeUnit.DAY).toString()
                DateType.MONTH_OR_MORE -> today.minus(Random.nextInt(31, 365), DateTimeUnit.DAY).toString()
            }
        )
    }
}

enum class DateType {TODAY_BLANK, TODAY_EXPLICIT, LESS_THAN_A_WEEK, LESS_THAN_A_MONTH, MONTH_OR_MORE}