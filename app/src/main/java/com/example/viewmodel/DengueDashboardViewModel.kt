package com.example.viewmodel

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import com.example.data.DemoDengueDataRepository
import com.example.data.DengueDataRepository
import com.example.model.AiChatMessage
import com.example.model.CityFilter
import com.example.model.CityResourceSummary
import com.example.model.CityRiskBarPoint
import com.example.model.DatasetConnector
import com.example.model.ForecastBandPoint
import com.example.model.KpiSummary
import com.example.model.MapLayerMode
import com.example.model.MlFeatureWeight
import com.example.model.NavSection
import com.example.model.ReportTemplate
import com.example.model.RiskFactorMetric
import com.example.model.RiskLevel
import com.example.model.WardHotspot
import com.example.model.WeeklyTrendPoint
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

@Immutable
data class DengueDashboardUiState(
  val selectedSection: NavSection = NavSection.OVERVIEW,
  val selectedCity: CityFilter = CityFilter.ALL,
  val selectedRiskFilter: RiskLevel? = null,
  val mapLayerMode: MapLayerMode = MapLayerMode.VECTOR_DENSITY,
  val selectedWardId: String? = null,
  val forecastHorizonDays: Int = 14,
  val foggingDispatchBoostPct: Float = 15f,
  val kpiSummary: KpiSummary,
  val weeklyTrend: List<WeeklyTrendPoint>,
  val riskBars: List<CityRiskBarPoint>,
  val wards: List<WardHotspot>,
  val forecastBand: List<ForecastBandPoint>,
  val mlFeatureWeights: List<MlFeatureWeight>,
  val riskFactors: List<RiskFactorMetric>,
  val resourceSummaries: List<CityResourceSummary>,
  val chatMessages: List<AiChatMessage>,
  val chatInput: String = "",
  val reports: List<ReportTemplate>,
  val previewingReport: ReportTemplate? = null,
  val datasetConnectors: List<DatasetConnector>,
  val snackbarMessage: String? = null,
) {
  val selectedWard: WardHotspot?
    get() = wards.firstOrNull { it.id == selectedWardId } ?: wards.firstOrNull()
}

class DengueDashboardViewModel(
  private val repository: DengueDataRepository = DemoDengueDataRepository()
) : ViewModel() {

  private val _uiState = MutableStateFlow(buildState(CityFilter.ALL))
  val uiState: StateFlow<DengueDashboardUiState> = _uiState.asStateFlow()

  private fun buildState(
    city: CityFilter,
    existing: DengueDashboardUiState? = null,
  ): DengueDashboardUiState {
    val wards = repository.getWardHotspots(city)
    val horizon = existing?.forecastHorizonDays ?: 14
    return DengueDashboardUiState(
      selectedSection = existing?.selectedSection ?: NavSection.OVERVIEW,
      selectedCity = city,
      selectedRiskFilter = existing?.selectedRiskFilter,
      mapLayerMode = existing?.mapLayerMode ?: MapLayerMode.VECTOR_DENSITY,
      selectedWardId = wards.firstOrNull()?.id,
      forecastHorizonDays = horizon,
      foggingDispatchBoostPct = existing?.foggingDispatchBoostPct ?: 15f,
      kpiSummary = repository.getKpiSummary(city),
      weeklyTrend = repository.getWeeklyTrend(city),
      riskBars = repository.getRiskBarSeries(city),
      wards = wards,
      forecastBand = repository.getForecastBand(city, horizon),
      mlFeatureWeights = repository.getMlFeatureWeights(city),
      riskFactors = repository.getRiskFactors(city),
      resourceSummaries = repository.getResourceSummaries(city),
      chatMessages = existing?.chatMessages ?: repository.getInitialAssistantMessages(),
      chatInput = existing?.chatInput ?: "",
      reports = repository.getReportTemplates(city),
      previewingReport = null,
      datasetConnectors = existing?.datasetConnectors ?: repository.getDatasetConnectors(),
      snackbarMessage = null,
    )
  }

  fun selectSection(section: NavSection) {
    _uiState.update { it.copy(selectedSection = section) }
  }

  fun selectCity(city: CityFilter) {
    _uiState.update { current -> buildState(city, current) }
  }

  fun selectRiskFilter(riskLevel: RiskLevel?) {
    _uiState.update { current ->
      val next = if (current.selectedRiskFilter == riskLevel) null else riskLevel
      current.copy(selectedRiskFilter = next)
    }
  }

  fun selectMapLayer(mode: MapLayerMode) {
    _uiState.update { it.copy(mapLayerMode = mode) }
  }

  fun selectWard(wardId: String) {
    _uiState.update { it.copy(selectedWardId = wardId) }
  }

  fun setForecastHorizon(days: Int) {
    _uiState.update { current ->
      current.copy(
        forecastHorizonDays = days,
        forecastBand = repository.getForecastBand(current.selectedCity, days),
      )
    }
  }

  fun setFoggingBoost(pct: Float) {
    _uiState.update { it.copy(foggingDispatchBoostPct = pct) }
  }

  fun updateChatInput(text: String) {
    _uiState.update { it.copy(chatInput = text) }
  }

  fun sendDemoChatMessage(overridePrompt: String? = null) {
    val current = _uiState.value
    val prompt = (overridePrompt ?: current.chatInput).trim()
    if (prompt.isEmpty()) return

    val cityLabel = current.selectedCity.displayName
    val topWard = current.wards.maxByOrNull { it.demoActiveCases }
    val reply = buildString {
      append("[DEMO UI PREVIEW — No Live AI/ML Connected]\n")
      append("Scope: $cityLabel • High-Risk Wards: ${current.kpiSummary.highRiskWardsCount}. ")
      if (topWard != null) {
        append(
          "Top placeholder hotspot is ${topWard.wardName} (${topWard.riskLevel.label}, " +
            "${topWard.demoActiveCases} demo cases, Breteau Index ${topWard.breteauIndex}). "
        )
      }
      append(
        "In Stage 2, this assistant will query live epidemiological feeds and predictive models for \"$prompt\"."
      )
    }

    val nextIndex = current.chatMessages.size + 1
    val userMsg = AiChatMessage(
      id = "u-$nextIndex",
      isUser = true,
      content = prompt,
      timestampLabel = "NOW",
      isDemoSimulated = false,
    )
    val aiMsg = AiChatMessage(
      id = "a-$nextIndex",
      isUser = false,
      content = reply,
      timestampLabel = "DEMO SHELL",
      isDemoSimulated = true,
    )
    _uiState.update {
      it.copy(
        chatMessages = it.chatMessages + userMsg + aiMsg,
        chatInput = if (overridePrompt == null) "" else it.chatInput,
      )
    }
  }

  fun openReportPreview(report: ReportTemplate) {
    _uiState.update { it.copy(previewingReport = report) }
  }

  fun closeReportPreview() {
    _uiState.update { it.copy(previewingReport = null) }
  }

  fun triggerDemoActionFeedback(message: String) {
    _uiState.update { it.copy(snackbarMessage = message) }
  }

  fun clearSnackbar() {
    _uiState.update { it.copy(snackbarMessage = null) }
  }

  fun resetDemoWorkspace() {
    _uiState.value = buildState(CityFilter.ALL).copy(
      snackbarMessage = "Demo workspace reset to All Cities baseline."
    )
  }
}
