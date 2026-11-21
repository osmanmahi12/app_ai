package com.example.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoGraph
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material.icons.outlined.AutoGraph
import androidx.compose.material.icons.outlined.Dashboard
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.LocalHospital
import androidx.compose.material.icons.outlined.Map
import androidx.compose.material.icons.outlined.Shield
import androidx.compose.material.icons.outlined.SmartToy
import androidx.compose.material.icons.outlined.Storage
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.ui.theme.RiskCriticalBg
import com.example.ui.theme.RiskCriticalBorder
import com.example.ui.theme.RiskCriticalRed
import com.example.ui.theme.RiskHighBg
import com.example.ui.theme.RiskHighBorder
import com.example.ui.theme.RiskHighOrange
import com.example.ui.theme.RiskLowBg
import com.example.ui.theme.RiskLowBorder
import com.example.ui.theme.RiskLowGreen
import com.example.ui.theme.RiskModerateBg
import com.example.ui.theme.RiskModerateBorder
import com.example.ui.theme.RiskModerateYellow

enum class NavSection(
  val title: String,
  val shortTitle: String,
  val selectedIcon: ImageVector,
  val unselectedIcon: ImageVector,
) {
  OVERVIEW("Overview", "Overview", Icons.Filled.Dashboard, Icons.Outlined.Dashboard),
  OUTBREAK_MAP("Outbreak Map", "Map", Icons.Filled.Map, Icons.Outlined.Map),
  AI_PREDICTIONS("AI Predictions", "Forecast", Icons.Filled.AutoGraph, Icons.Outlined.AutoGraph),
  RISK_ANALYSIS("Risk Analysis", "Risk", Icons.Filled.Shield, Icons.Outlined.Shield),
  RESOURCE_ALLOCATION("Resources", "Resources", Icons.Filled.LocalHospital, Icons.Outlined.LocalHospital),
  AI_ASSISTANT("AI Assistant", "Assistant", Icons.Filled.SmartToy, Icons.Outlined.SmartToy),
  REPORTS("Reports", "Reports", Icons.Filled.Description, Icons.Outlined.Description),
  DATASET_MANAGEMENT("Datasets", "Datasets", Icons.Filled.Storage, Icons.Outlined.Storage),
}

enum class CityFilter(val displayName: String, val divisionCode: String) {
  ALL("All Cities", "BD-ALL"),
  DHAKA("Dhaka", "BD-DHK"),
  CHATTOGRAM("Chattogram", "BD-CTG"),
  GAZIPUR("Gazipur", "BD-GZP"),
  NARAYANGANJ("Narayanganj", "BD-NRG"),
}

enum class RiskLevel(
  val label: String,
  val color: Color,
  val containerColor: Color,
  val borderColor: Color,
) {
  CRITICAL("Critical", RiskCriticalRed, RiskCriticalBg, RiskCriticalBorder),
  HIGH("High", RiskHighOrange, RiskHighBg, RiskHighBorder),
  MODERATE("Moderate", RiskModerateYellow, RiskModerateBg, RiskModerateBorder),
  LOW("Low", RiskLowGreen, RiskLowBg, RiskLowBorder),
}

enum class MapLayerMode(val label: String) {
  VECTOR_DENSITY("Vector Density"),
  STANDING_WATER("Waterlogging"),
  HOSPITAL_LOAD("Bed Load"),
}

@Immutable
data class KpiSummary(
  val totalDemoCases: Int,
  val weeklyDeltaLabel: String,
  val citiesMonitored: Int,
  val wardsTrackedLabel: String,
  val highRiskWardsCount: Int,
  val criticalWardsCount: Int,
  val predictedStatusLabel: String,
  val predictedStatusRisk: RiskLevel,
  val statusNote: String,
)

@Immutable
data class WeeklyTrendPoint(
  val weekLabel: String,
  val reportedDemoCases: Float,
  val seasonalBaseline: Float,
)

@Immutable
data class CityRiskBarPoint(
  val label: String,
  val city: CityFilter,
  val value: Float,
  val riskLevel: RiskLevel,
)

@Immutable
data class WardHotspot(
  val id: String,
  val wardName: String,
  val city: CityFilter,
  val zoneCode: String,
  val riskLevel: RiskLevel,
  val demoActiveCases: Int,
  val breteauIndex: Int,
  val rainfallMm7d: Int,
  val hospitalOccupancyPct: Int,
  val mapX: Float,
  val mapY: Float,
  val primaryDriver: String,
)

@Immutable
data class ForecastBandPoint(
  val dayLabel: String,
  val predictedCases: Float,
  val upperBound: Float,
  val lowerBound: Float,
)

@Immutable
data class MlFeatureWeight(
  val name: String,
  val weightPct: Int,
  val currentDemoReading: String,
  val status: String,
)

@Immutable
data class RiskFactorMetric(
  val factorName: String,
  val scoreOutOf100: Int,
  val riskLevel: RiskLevel,
  val description: String,
)

@Immutable
data class CityResourceSummary(
  val city: CityFilter,
  val allocatedBeds: Int,
  val occupiedBeds: Int,
  val plateletUnitsAvailable: Int,
  val foggingSquadsActive: Int,
  val larvicideCoveragePct: Int,
  val priorityLevel: RiskLevel,
)

@Immutable
data class AiChatMessage(
  val id: String,
  val isUser: Boolean,
  val content: String,
  val timestampLabel: String,
  val isDemoSimulated: Boolean = !isUser,
)

@Immutable
data class ReportTemplate(
  val id: String,
  val title: String,
  val audience: String,
  val frequency: String,
  val format: String,
  val lastGeneratedLabel: String,
  val summaryPreview: String,
)

@Immutable
data class DatasetConnector(
  val id: String,
  val name: String,
  val sourceAgency: String,
  val recordCountLabel: String,
  val statusLabel: String,
  val schemaFields: String,
  val isReady: Boolean,
)
