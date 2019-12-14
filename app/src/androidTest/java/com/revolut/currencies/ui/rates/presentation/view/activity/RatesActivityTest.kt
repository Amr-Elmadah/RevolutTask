package com.revolut.currencies.ui.rates.presentation.view.activity

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.revolut.currencies.R
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class RatesActivityTest {
    @get : Rule
    val activityScenario = ActivityScenario.launch(RatesActivity::class.java)

    @Test
    fun testIsRatesActivityInView() {
        onView(withId(R.id.llMainContent)).check(matches(isDisplayed()))
    }

    @Test
    fun testIsRatesRecyclerViewVisible() {
        onView(withId(R.id.rvRates)).check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
    }

    @Test
    fun testIsNoDataLayoutNotVisible() {
        onView(withId(R.id.llNoData)).check(matches(withEffectiveVisibility(Visibility.GONE)))
    }

    @Test
    fun testIsToolbarWithRatesText() {
        onView(withId(R.id.tvRates)).check(matches(withText(
            R.string.rates
        )))
    }

    @Test
    fun testSrlIsWorking() {
        onView(withId(R.id.srlRates)).perform(ViewActions.swipeUp())
    }
}