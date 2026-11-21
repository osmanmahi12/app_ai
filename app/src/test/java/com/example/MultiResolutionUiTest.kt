package com.example

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
class MultiResolutionUiTest {

  @get:Rule
  val composeTestRule = createAndroidComposeRule<MainActivity>()

  @Test
  @Config(sdk = [36], qualifiers = "w360dp-h640dp-xxhdpi")
  fun `compact high-dpi phone renders KPIs and city filters cleanly`() {
    composeTestRule.waitForIdle()
    composeTestRule.onNodeWithTag("open_sidebar_button").assertIsDisplayed()
    composeTestRule.onNodeWithTag("kpi_total_cases").assertIsDisplayed()
    composeTestRule.onNodeWithTag("city_filter_chattogram").performClick()
    composeTestRule.waitForIdle()
    composeTestRule.onNodeWithTag("kpi_outbreak_status").assertIsDisplayed()
  }

  @Test
  @Config(sdk = [36], qualifiers = "w700dp-h1000dp-xhdpi")
  fun `medium tablet resolution renders rail and wide charts`() {
    composeTestRule.waitForIdle()
    composeTestRule.onNodeWithTag("kpi_total_cases").assertIsDisplayed()
    composeTestRule.onNodeWithTag("incidence_line_chart_card").assertIsDisplayed()
    composeTestRule.onNodeWithTag("risk_bar_chart_card").assertIsDisplayed()
  }

  @Test
  @Config(sdk = [36], qualifiers = "w1024dp-h768dp-xhdpi")
  fun `expanded desktop resolution renders persistent sidebar and full dashboard`() {
    composeTestRule.waitForIdle()
    composeTestRule.onNodeWithTag("sidebar_nav_overview").assertIsDisplayed()
    composeTestRule.onNodeWithTag("sidebar_nav_ai_assistant").performClick()
    composeTestRule.waitForIdle()
    composeTestRule.onNodeWithTag("assistant_input_field").assertIsDisplayed()
  }
}
