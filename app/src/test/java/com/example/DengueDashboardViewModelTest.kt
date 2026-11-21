package com.example

import com.example.model.CityFilter
import com.example.model.NavSection
import com.example.model.RiskLevel
import com.example.viewmodel.DengueDashboardViewModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class DengueDashboardViewModelTest {

  @Test
  fun allNavigationSections_canBeSelected() {
    val vm = DengueDashboardViewModel()
    NavSection.entries.forEach { section ->
      vm.selectSection(section)
      assertEquals(section, vm.uiState.value.selectedSection)
    }
  }

  @Test
  fun cityFilters_updateKpisAndWardScope() {
    val vm = DengueDashboardViewModel()
    val allCases = vm.uiState.value.kpiSummary.totalDemoCases
    assertEquals(4, vm.uiState.value.kpiSummary.citiesMonitored)

    listOf(
      CityFilter.DHAKA,
      CityFilter.CHATTOGRAM,
      CityFilter.GAZIPUR,
      CityFilter.NARAYANGANJ,
    ).forEach { city ->
      vm.selectCity(city)
      val state = vm.uiState.value
      assertEquals(city, state.selectedCity)
      assertEquals(1, state.kpiSummary.citiesMonitored)
      assertTrue(state.kpiSummary.totalDemoCases < allCases)
      assertTrue(state.wards.isNotEmpty())
      assertTrue(state.wards.all { it.city == city })
    }
  }

  @Test
  fun riskFilterAndForecastHorizon_updateState() {
    val vm = DengueDashboardViewModel()
    vm.selectRiskFilter(RiskLevel.CRITICAL)
    assertEquals(RiskLevel.CRITICAL, vm.uiState.value.selectedRiskFilter)
    // Tapping again clears filter
    vm.selectRiskFilter(RiskLevel.CRITICAL)
    assertEquals(null, vm.uiState.value.selectedRiskFilter)

    vm.setForecastHorizon(7)
    assertEquals(7, vm.uiState.value.forecastBand.size)
    vm.setForecastHorizon(14)
    assertEquals(14, vm.uiState.value.forecastBand.size)
  }

  @Test
  fun demoAssistantShell_appendsUserAndDemoReply() {
    val vm = DengueDashboardViewModel()
    val initialCount = vm.uiState.value.chatMessages.size
    vm.sendDemoChatMessage("Summarize Dhaka hotspots")
    val updated = vm.uiState.value.chatMessages
    assertEquals(initialCount + 2, updated.size)
    assertTrue(updated.last().isDemoSimulated)
    assertTrue(updated.last().content.contains("DEMO UI PREVIEW"))
  }
}
