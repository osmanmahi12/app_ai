package com.example

import android.content.Context
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.core.app.ApplicationProvider
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @get:Rule
  val composeTestRule = createAndroidComposeRule<MainActivity>()

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("DengueGuard AI", appName)
  }

  @Test
  fun `main activity launches and renders dashboard`() {
    composeTestRule.waitForIdle()
    composeTestRule.onNodeWithTag("kpi_total_cases").assertIsDisplayed()
    composeTestRule.onNodeWithTag("city_filter_dhaka").performClick()
    composeTestRule.waitForIdle()
    composeTestRule.onNodeWithText("Map").performClick()
    composeTestRule.waitForIdle()
    composeTestRule.onNodeWithTag("topbar_section_title").assertIsDisplayed()
    composeTestRule.onNodeWithTag("outbreak_radar_map").assertIsDisplayed()
  }
}
