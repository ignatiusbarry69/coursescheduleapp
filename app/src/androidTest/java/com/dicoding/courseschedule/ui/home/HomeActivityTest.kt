package com.dicoding.courseschedule.ui.home

import com.dicoding.courseschedule.R
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class HomeActivityTest {

    @Rule
    @JvmField
    val activityRule = ActivityTestRule(HomeActivity::class.java)

    @Test
    fun testAddCourseMenu() {
        // click add menu
        onView(withId(R.id.action_add)).perform(click())

        // cek addactivity opened
        onView(withId(R.id.spinner)).check(matches(isDisplayed()))
    }
}