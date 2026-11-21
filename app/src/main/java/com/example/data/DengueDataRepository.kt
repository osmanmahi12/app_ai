package com.example.data

import com.example.model.AiChatMessage
import com.example.model.CityFilter
import com.example.model.CityResourceSummary
import com.example.model.CityRiskBarPoint
import com.example.model.DatasetConnector
import com.example.model.ForecastBandPoint
import com.example.model.KpiSummary
import com.example.model.MlFeatureWeight
import com.example.model.ReportTemplate
import com.example.model.RiskFactorMetric
import com.example.model.RiskLevel
import com.example.model.WardHotspot
import com.example.model.WeeklyTrendPoint

/**
 * Repository abstraction designed for Stage-2 backend/ML replacement (e.g., Retrofit/Firebase/DGHS feed).
 */
interface DengueDataRepository {
  fun getKpiSummary(city: CityFilter): KpiSummary
  fun getWeeklyTrend(city: CityFilter): List<WeeklyTrendPoint>
  fun getRiskBarSeries(city: CityFilter): List<CityRiskBarPoint>
  fun getWardHotspots(city: CityFilter): List<WardHotspot>
  fun getForecastBand(city: CityFilter, horizonDays: Int): List<ForecastBandPoint>
  fun getMlFeatureWeights(city: CityFilter): List<MlFeatureWeight>
  fun getRiskFactors(city: CityFilter): List<RiskFactorMetric>
  fun getResourceSummaries(city: CityFilter): List<CityResourceSummary>
  fun getInitialAssistantMessages(): List<AiChatMessage>
  fun getReportTemplates(city: CityFilter): List<ReportTemplate>
  fun getDatasetConnectors(): List<DatasetConnector>
}

/**
 * Deterministic synthetic DEMO DATA provider.
 * All figures are illustrative UI placeholders and NOT real epidemiological statistics.
 */
class DemoDengueDataRepository : DengueDataRepository {

  private val allWards = listOf(
    WardHotspot(
      id = "dhk-n19",
      wardName = "DNCC Ward 19 (Banani-Gulshan)",
      city = CityFilter.DHAKA,
      zoneCode = "DHK-N19",
      riskLevel = RiskLevel.CRITICAL,
      demoActiveCases = 412,
      breteauIndex = 44,
      rainfallMm7d = 128,
      hospitalOccupancyPct = 86,
      mapX = 0.44f,
      mapY = 0.34f,
      primaryDriver = "Construction waterlogging & high container index (Demo)",
    ),
    WardHotspot(
      id = "dhk-s14",
      wardName = "DSCC Ward 14 (Dhanmondi-Jigatola)",
      city = CityFilter.DHAKA,
      zoneCode = "DHK-S14",
      riskLevel = RiskLevel.CRITICAL,
      demoActiveCases = 368,
      breteauIndex = 39,
      rainfallMm7d = 122,
      hospitalOccupancyPct = 82,
      mapX = 0.37f,
      mapY = 0.47f,
      primaryDriver = "Dense residential rooftop tanks (Demo)",
    ),
    WardHotspot(
      id = "dhk-n02",
      wardName = "DNCC Ward 02 (Mirpur Section 12)",
      city = CityFilter.DHAKA,
      zoneCode = "DHK-N02",
      riskLevel = RiskLevel.HIGH,
      demoActiveCases = 285,
      breteauIndex = 31,
      rainfallMm7d = 114,
      hospitalOccupancyPct = 74,
      mapX = 0.28f,
      mapY = 0.27f,
      primaryDriver = "Drainage blockage & post-rain stagnation (Demo)",
    ),
    WardHotspot(
      id = "ctg-26",
      wardName = "CCC Ward 26 (North Halishahar)",
      city = CityFilter.CHATTOGRAM,
      zoneCode = "CTG-W26",
      riskLevel = RiskLevel.CRITICAL,
      demoActiveCases = 334,
      breteauIndex = 41,
      rainfallMm7d = 165,
      hospitalOccupancyPct = 79,
      mapX = 0.78f,
      mapY = 0.74f,
      primaryDriver = "Coastal humidity & tidal storm drains (Demo)",
    ),
    WardHotspot(
      id = "ctg-15",
      wardName = "CCC Ward 15 (Bagmaniram-Chawkbazar)",
      city = CityFilter.CHATTOGRAM,
      zoneCode = "CTG-W15",
      riskLevel = RiskLevel.HIGH,
      demoActiveCases = 219,
      breteauIndex = 29,
      rainfallMm7d = 152,
      hospitalOccupancyPct = 71,
      mapX = 0.84f,
      mapY = 0.63f,
      primaryDriver = "High population density & market clusters (Demo)",
    ),
    WardHotspot(
      id = "gzp-47",
      wardName = "GCC Ward 47 (Tongi East Industrial)",
      city = CityFilter.GAZIPUR,
      zoneCode = "GZP-W47",
      riskLevel = RiskLevel.HIGH,
      demoActiveCases = 248,
      breteauIndex = 33,
      rainfallMm7d = 109,
      hospitalOccupancyPct = 68,
      mapX = 0.42f,
      mapY = 0.16f,
      primaryDriver = "Industrial yard tire & drum storage (Demo)",
    ),
    WardHotspot(
      id = "gzp-25",
      wardName = "GCC Ward 25 (Joydebpur Central)",
      city = CityFilter.GAZIPUR,
      zoneCode = "GZP-W25",
      riskLevel = RiskLevel.MODERATE,
      demoActiveCases = 142,
      breteauIndex = 22,
      rainfallMm7d = 98,
      hospitalOccupancyPct = 54,
      mapX = 0.34f,
      mapY = 0.12f,
      primaryDriver = "Seasonal peri-urban transit corridor (Demo)",
    ),
    WardHotspot(
      id = "nrg-11",
      wardName = "NCC Ward 11 (Fatullah Riverfront)",
      city = CityFilter.NARAYANGANJ,
      zoneCode = "NRG-W11",
      riskLevel = RiskLevel.HIGH,
      demoActiveCases = 196,
      breteauIndex = 28,
      rainfallMm7d = 118,
      hospitalOccupancyPct = 66,
      mapX = 0.56f,
      mapY = 0.58f,
      primaryDriver = "Shitalakshya dockside water containers (Demo)",
    ),
    WardHotspot(
      id = "nrg-04",
      wardName = "NCC Ward 04 (Siddhirganj West)",
      city = CityFilter.NARAYANGANJ,
      zoneCode = "NRG-W04",
      riskLevel = RiskLevel.MODERATE,
      demoActiveCases = 118,
      breteauIndex = 19,
      rainfallMm7d = 104,
      hospitalOccupancyPct = 49,
      mapX = 0.62f,
      mapY = 0.49f,
      primaryDriver = "Moderate larval counts in residential blocks (Demo)",
    ),
    WardHotspot(
      id = "dhk-s33",
      wardName = "DSCC Ward 33 (Uttara Sector 7)",
      city = CityFilter.DHAKA,
      zoneCode = "DHK-N01",
      riskLevel = RiskLevel.LOW,
      demoActiveCases = 76,
      breteauIndex = 12,
      rainfallMm7d = 92,
      hospitalOccupancyPct = 38,
      mapX = 0.25f,
      mapY = 0.38f,
      primaryDriver = "Active municipal larvicide clearance (Demo)",
    ),
  )

  private val baseWeeks = listOf("W1", "W2", "W3", "W4", "W5", "W6", "W7", "W8", "W9", "W10", "W11", "W12")

  private val cityScaleFactor = mapOf(
    CityFilter.ALL to 1.0f,
    CityFilter.DHAKA to 0.46f,
    CityFilter.CHATTOGRAM to 0.24f,
    CityFilter.GAZIPUR to 0.17f,
    CityFilter.NARAYANGANJ to 0.13f,
  )

  override fun getKpiSummary(city: CityFilter): KpiSummary {
    val wards = getWardHotspots(city)
    val highOrCritical = wards.count { it.riskLevel == RiskLevel.CRITICAL || it.riskLevel == RiskLevel.HIGH }
    val criticalOnly = wards.count { it.riskLevel == RiskLevel.CRITICAL }
    return when (city) {
      CityFilter.ALL -> KpiSummary(
        totalDemoCases = 2398,
        weeklyDeltaLabel = "+12.4% (Demo)",
        citiesMonitored = 4,
        wardsTrackedLabel = "42 Demo Zones",
        highRiskWardsCount = highOrCritical,
        criticalWardsCount = criticalOnly,
        predictedStatusLabel = "ELEVATED WATCH",
        predictedStatusRisk = RiskLevel.HIGH,
        statusNote = "Stage 1 Placeholder • ML Offline",
      )
      CityFilter.DHAKA -> KpiSummary(
        totalDemoCases = 1141,
        weeklyDeltaLabel = "+14.8% (Demo)",
        citiesMonitored = 1,
        wardsTrackedLabel = "18 DNCC/DSCC Demo Zones",
        highRiskWardsCount = highOrCritical,
        criticalWardsCount = criticalOnly,
        predictedStatusLabel = "CRITICAL ALERT",
        predictedStatusRisk = RiskLevel.CRITICAL,
        statusNote = "Stage 1 Placeholder • ML Offline",
      )
      CityFilter.CHATTOGRAM -> KpiSummary(
        totalDemoCases = 553,
        weeklyDeltaLabel = "+11.1% (Demo)",
        citiesMonitored = 1,
        wardsTrackedLabel = "10 CCC Demo Zones",
        highRiskWardsCount = highOrCritical,
        criticalWardsCount = criticalOnly,
        predictedStatusLabel = "HIGH SURGE RISK",
        predictedStatusRisk = RiskLevel.HIGH,
        statusNote = "Stage 1 Placeholder • ML Offline",
      )
      CityFilter.GAZIPUR -> KpiSummary(
        totalDemoCases = 390,
        weeklyDeltaLabel = "+7.2% (Demo)",
        citiesMonitored = 1,
        wardsTrackedLabel = "8 GCC Demo Zones",
        highRiskWardsCount = highOrCritical,
        criticalWardsCount = criticalOnly,
        predictedStatusLabel = "MODERATE WATCH",
        predictedStatusRisk = RiskLevel.MODERATE,
        statusNote = "Stage 1 Placeholder • ML Offline",
      )
      CityFilter.NARAYANGANJ -> KpiSummary(
        totalDemoCases = 314,
        weeklyDeltaLabel = "+5.6% (Demo)",
        citiesMonitored = 1,
        wardsTrackedLabel = "6 NCC Demo Zones",
        highRiskWardsCount = highOrCritical,
        criticalWardsCount = criticalOnly,
        predictedStatusLabel = "CONTROLLED WATCH",
        predictedStatusRisk = RiskLevel.LOW,
        statusNote = "Stage 1 Placeholder • ML Offline",
      )
    }
  }

  override fun getWeeklyTrend(city: CityFilter): List<WeeklyTrendPoint> {
    val factor = cityScaleFactor[city] ?: 1f
    val rawDemo = listOf(120f, 138f, 154f, 175f, 198f, 224f, 240f, 268f, 295f, 312f, 348f, 382f)
    val rawBaseline = listOf(110f, 118f, 126f, 136f, 148f, 160f, 174f, 188f, 202f, 214f, 226f, 238f)
    return baseWeeks.mapIndexed { index, week ->
      WeeklyTrendPoint(
        weekLabel = week,
        reportedDemoCases = (rawDemo[index] * factor).toInt().toFloat(),
        seasonalBaseline = (rawBaseline[index] * factor).toInt().toFloat(),
      )
    }
  }

  override fun getRiskBarSeries(city: CityFilter): List<CityRiskBarPoint> {
    return if (city == CityFilter.ALL) {
      listOf(
        CityRiskBarPoint("Dhaka", CityFilter.DHAKA, 1141f, RiskLevel.CRITICAL),
        CityRiskBarPoint("Chattogram", CityFilter.CHATTOGRAM, 553f, RiskLevel.HIGH),
        CityRiskBarPoint("Gazipur", CityFilter.GAZIPUR, 390f, RiskLevel.MODERATE),
        CityRiskBarPoint("Narayanganj", CityFilter.NARAYANGANJ, 314f, RiskLevel.LOW),
      )
    } else {
      getWardHotspots(city).map { ward ->
        CityRiskBarPoint(
          label = ward.zoneCode,
          city = ward.city,
          value = ward.demoActiveCases.toFloat(),
          riskLevel = ward.riskLevel,
        )
      }
    }
  }

  override fun getWardHotspots(city: CityFilter): List<WardHotspot> {
    return if (city == CityFilter.ALL) allWards else allWards.filter { it.city == city }
  }

  override fun getForecastBand(city: CityFilter, horizonDays: Int): List<ForecastBandPoint> {
    val factor = cityScaleFactor[city] ?: 1f
    val count = if (horizonDays <= 7) 7 else 14
    return (1..count).map { day ->
      val center = (360f + day * 14f) * factor
      val spread = (28f + day * 4.5f) * factor
      ForecastBandPoint(
        dayLabel = "D+$day",
        predictedCases = center.toInt().toFloat(),
        upperBound = (center + spread).toInt().toFloat(),
        lowerBound = (center - spread).coerceAtLeast(10f).toInt().toFloat(),
      )
    }
  }

  override fun getMlFeatureWeights(city: CityFilter): List<MlFeatureWeight> {
    val rainReading = when (city) {
      CityFilter.CHATTOGRAM -> "165 mm / 7d"
      CityFilter.DHAKA -> "125 mm / 7d"
      CityFilter.GAZIPUR -> "104 mm / 7d"
      CityFilter.NARAYANGANJ -> "112 mm / 7d"
      CityFilter.ALL -> "126 mm avg"
    }
    return listOf(
      MlFeatureWeight("7-Day Cumulative Rainfall", 34, rainReading, "Placeholder Weight"),
      MlFeatureWeight("Breteau Larval Index", 29, "Index 33.4 (Demo)", "Placeholder Weight"),
      MlFeatureWeight("Relative Humidity & Temp", 21, "82% RH • 31°C", "Placeholder Weight"),
      MlFeatureWeight("Urban Drainage & Density", 16, "High Cluster Score", "Placeholder Weight"),
    )
  }

  override fun getRiskFactors(city: CityFilter): List<RiskFactorMetric> {
    val boost = when (city) {
      CityFilter.DHAKA -> 6
      CityFilter.CHATTOGRAM -> 4
      CityFilter.GAZIPUR -> -3
      CityFilter.NARAYANGANJ -> -6
      CityFilter.ALL -> 0
    }
    return listOf(
      RiskFactorMetric(
        factorName = "Aedes Breteau Index (Demo)",
        scoreOutOf100 = (78 + boost).coerceIn(20, 96),
        riskLevel = RiskLevel.CRITICAL,
        description = "Synthetic household container & construction site larval proxy.",
      ),
      RiskFactorMetric(
        factorName = "Monsoon Waterlogging Index (Demo)",
        scoreOutOf100 = (72 + boost).coerceIn(20, 96),
        riskLevel = RiskLevel.HIGH,
        description = "Placeholder stormwater stagnation score across low-lying wards.",
      ),
      RiskFactorMetric(
        factorName = "Urban Mobility & Density (Demo)",
        scoreOutOf100 = (64 + boost).coerceIn(20, 96),
        riskLevel = RiskLevel.MODERATE,
        description = "Inter-ward commuter overlap and housing density placeholder.",
      ),
      RiskFactorMetric(
        factorName = "Vector Control Coverage (Demo)",
        scoreOutOf100 = (42 - boost / 2).coerceIn(20, 90),
        riskLevel = RiskLevel.LOW,
        description = "Inverse readiness metric for municipal fogging & larvicide teams.",
      ),
    )
  }

  override fun getResourceSummaries(city: CityFilter): List<CityResourceSummary> {
    val all = listOf(
      CityResourceSummary(CityFilter.DHAKA, 1450, 1180, 620, 34, 68, RiskLevel.CRITICAL),
      CityResourceSummary(CityFilter.CHATTOGRAM, 680, 510, 290, 18, 62, RiskLevel.HIGH),
      CityResourceSummary(CityFilter.GAZIPUR, 410, 275, 165, 12, 74, RiskLevel.MODERATE),
      CityResourceSummary(CityFilter.NARAYANGANJ, 340, 205, 140, 10, 81, RiskLevel.LOW),
    )
    return if (city == CityFilter.ALL) all else all.filter { it.city == city }
  }

  override fun getInitialAssistantMessages(): List<AiChatMessage> = listOf(
    AiChatMessage(
      id = "welcome-1",
      isUser = false,
      content = "Welcome to DengueGuard AI Assistant (Stage 1 UI Shell). Live LLM inference is not connected yet. Tap any quick prompt below to preview how municipal outbreak briefings and ward action plans will render.",
      timestampLabel = "DEMO SHELL",
    ),
  )

  override fun getReportTemplates(city: CityFilter): List<ReportTemplate> {
    val scope = city.displayName
    return listOf(
      ReportTemplate(
        id = "rep-daily",
        title = "Daily Municipal Outbreak Bulletin ($scope)",
        audience = "City Corporation Health Officers",
        frequency = "Daily • 08:00 BST",
        format = "PDF / JSON",
        lastGeneratedLabel = "DEMO PREVIEW",
        summaryPreview = "Summarizes synthetic ward risk tiers, demo hospital occupancy, and top priority fogging targets for $scope.",
      ),
      ReportTemplate(
        id = "rep-dghs",
        title = "Weekly Epidemiological Surveillance Digest ($scope)",
        audience = "DGHS & Urban Health Planners",
        frequency = "Weekly • Sunday",
        format = "CSV / XLSX",
        lastGeneratedLabel = "DEMO PREVIEW",
        summaryPreview = "Contains 12-week placeholder trend series, larval index breakdowns, and drainage hotspot tables for $scope.",
      ),
      ReportTemplate(
        id = "rep-dispatch",
        title = "Vector Control & Fogging Dispatch Sheet ($scope)",
        audience = "Field Entomology Teams",
        frequency = "On-Demand",
        format = "PDF",
        lastGeneratedLabel = "DEMO PREVIEW",
        summaryPreview = "Ward-by-ward larvicide squad checklist and critical cluster coordinates (synthetic demo coordinates).",
      ),
    )
  }

  override fun getDatasetConnectors(): List<DatasetConnector> = listOf(
    DatasetConnector(
      id = "ds-cases",
      name = "Municipal Clinical Case Feed",
      sourceAgency = "DGHS / City Corporation Hospitals (Planned)",
      recordCountLabel = "2,398 Demo Rows",
      statusLabel = "DEMO SEEDED",
      schemaFields = "ward_id, city, date_iso, confirmed_count, platelet_demand",
      isReady = true,
    ),
    DatasetConnector(
      id = "ds-weather",
      name = "BMD Weather & Rainfall Telemetry",
      sourceAgency = "Bangladesh Meteorological Dept API (Planned)",
      recordCountLabel = "4 Cities • 12 Wks",
      statusLabel = "AWAITING API",
      schemaFields = "station_id, rainfall_mm, humidity_pct, temp_c, timestamp",
      isReady = false,
    ),
    DatasetConnector(
      id = "ds-gis",
      name = "Urban Ward GIS & Drainage Polygons",
      sourceAgency = "DNCC / DSCC / CCC / GCC / NCC GIS (Planned)",
      recordCountLabel = "10 Demo Wards",
      statusLabel = "DEMO SEEDED",
      schemaFields = "ward_code, polygon_geojson, elevation_m, density_sqkm",
      isReady = true,
    ),
    DatasetConnector(
      id = "ds-ml",
      name = "Stage-2 Outbreak Forecast Pipeline",
      sourceAgency = "DengueGuard ML Inference Service (Planned)",
      recordCountLabel = "Model v0.0-stub",
      statusLabel = "NOT CONNECTED",
      schemaFields = "ward_code, horizon_days, risk_score, ci_low, ci_high",
      isReady = false,
    ),
  )
}
