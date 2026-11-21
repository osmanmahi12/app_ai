package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Coronavirus
import androidx.compose.material.icons.filled.LocationCity
import androidx.compose.material.icons.filled.Radar
import androidx.compose.material.icons.filled.WarningAmber
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.model.CityFilter
import com.example.model.NavSection
import com.example.model.WardHotspot
import com.example.ui.components.IncidenceLineChartCard
import com.example.ui.components.KpiCard
import com.example.ui.components.RiskBadge
import com.example.ui.components.RiskBarChartCard
import com.example.ui.theme.CharcoalCard
import com.example.ui.theme.CyanBright
import com.example.ui.theme.RiskHighOrange
import com.example.ui.theme.SlateBorder
import com.example.ui.theme.TealAccent
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.viewmodel.DengueDashboardUiState

@Composable
fun OverviewScreen(
  state: DengueDashboardUiState,
  isWideLayout: Boolean,
  onNavigateSection: (NavSection) -> Unit,
  onSelectCityFromBar: (CityFilter) -> Unit,
  onInspectWard: (WardHotspot) -> Unit,
  modifier: Modifier = Modifier,
) {
  LazyColumn(
    modifier = modifier.fillMaxSize(),
    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 14.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp),
  ) {
    // 4 Executive KPI Cards immediately at the top
    item(key = "kpi_grid", contentType = "kpi") {
      val kpi = state.kpiSummary
      if (isWideLayout) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
          KpiCard(
            title = "Total Cases",
            value = kpi.totalDemoCases.toString(),
            subtitle = kpi.weeklyDeltaLabel,
            icon = Icons.Filled.Coronavirus,
            accentColor = CyanBright,
            modifier = Modifier.weight(1f),
            tagName = "kpi_total_cases",
          )
          KpiCard(
            title = "Cities",
            value = kpi.citiesMonitored.toString(),
            subtitle = kpi.wardsTrackedLabel,
            icon = Icons.Filled.LocationCity,
            accentColor = TealAccent,
            modifier = Modifier.weight(1f),
            tagName = "kpi_cities_monitored",
          )
          KpiCard(
            title = "High-Risk Wards",
            value = kpi.highRiskWardsCount.toString(),
            subtitle = "${kpi.criticalWardsCount} Critical",
            icon = Icons.Filled.WarningAmber,
            accentColor = RiskHighOrange,
            modifier = Modifier.weight(1f),
            tagName = "kpi_high_risk_wards",
          )
          KpiCard(
            title = "Outbreak Status",
            value = kpi.predictedStatusLabel,
            subtitle = "Demo Placeholder",
            icon = Icons.Filled.Radar,
            accentColor = kpi.predictedStatusRisk.color,
            riskBadge = kpi.predictedStatusRisk,
            modifier = Modifier.weight(1f),
            tagName = "kpi_outbreak_status",
          )
        }
      } else {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
          ) {
            KpiCard(
              title = "Total Cases",
              value = kpi.totalDemoCases.toString(),
              subtitle = kpi.weeklyDeltaLabel,
              icon = Icons.Filled.Coronavirus,
              accentColor = CyanBright,
              modifier = Modifier.weight(1f),
              tagName = "kpi_total_cases",
            )
            KpiCard(
              title = "Cities",
              value = kpi.citiesMonitored.toString(),
              subtitle = kpi.wardsTrackedLabel,
              icon = Icons.Filled.LocationCity,
              accentColor = TealAccent,
              modifier = Modifier.weight(1f),
              tagName = "kpi_cities_monitored",
            )
          }
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
          ) {
            KpiCard(
              title = "High-Risk Wards",
              value = kpi.highRiskWardsCount.toString(),
              subtitle = "${kpi.criticalWardsCount} Critical",
              icon = Icons.Filled.WarningAmber,
              accentColor = RiskHighOrange,
              modifier = Modifier.weight(1f),
              tagName = "kpi_high_risk_wards",
            )
            KpiCard(
              title = "Outbreak Status",
              value = kpi.predictedStatusLabel,
              subtitle = "Demo Placeholder",
              icon = Icons.Filled.Radar,
              accentColor = kpi.predictedStatusRisk.color,
              riskBadge = kpi.predictedStatusRisk,
              modifier = Modifier.weight(1f),
              tagName = "kpi_outbreak_status",
            )
          }
        }
      }
    }

    // Charts
    item(key = "charts", contentType = "charts") {
      val lineTitle = "Weekly Cases (${state.selectedCity.displayName})"
      val barTitle = if (state.selectedCity == CityFilter.ALL) {
        "City Risk Breakdown"
      } else {
        "${state.selectedCity.displayName} Wards"
      }

      if (isWideLayout) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(14.dp),
        ) {
          IncidenceLineChartCard(
            title = lineTitle,
            points = state.weeklyTrend,
            modifier = Modifier.weight(1.15f),
          )
          RiskBarChartCard(
            title = barTitle,
            bars = state.riskBars,
            modifier = Modifier.weight(1f),
            onBarSelected = { bar ->
              if (state.selectedCity == CityFilter.ALL) {
                onSelectCityFromBar(bar.city)
              }
            },
          )
        }
      } else {
        Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
          IncidenceLineChartCard(
            title = lineTitle,
            points = state.weeklyTrend,
          )
          RiskBarChartCard(
            title = barTitle,
            bars = state.riskBars,
            onBarSelected = { bar ->
              if (state.selectedCity == CityFilter.ALL) {
                onSelectCityFromBar(bar.city)
              }
            },
          )
        }
      }
    }

    // Priority Wards Header
    item(key = "watchlist_header", contentType = "header") {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
      ) {
        Text(
          text = "Priority Wards",
          style = MaterialTheme.typography.titleLarge,
          color = TextPrimary,
        )
        TextButton(
          onClick = { onNavigateSection(NavSection.OUTBREAK_MAP) },
          modifier = Modifier.testTag("overview_open_map_button"),
        ) {
          Text("View Map", color = CyanBright, style = MaterialTheme.typography.labelLarge)
        }
      }
    }

    items(
      items = state.wards.take(4),
      key = { it.id },
      contentType = { "ward_row" },
    ) { ward ->
      WardWatchlistRow(
        ward = ward,
        onClick = { onInspectWard(ward) },
      )
    }

    item { Spacer(Modifier.height(12.dp)) }
  }
}

@Composable
fun WardWatchlistRow(
  ward: WardHotspot,
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
) {
  Surface(
    modifier = modifier
      .fillMaxWidth()
      .clip(MaterialTheme.shapes.medium)
      .clickable(onClick = onClick)
      .testTag("ward_row_${ward.id}"),
    shape = MaterialTheme.shapes.medium,
    color = CharcoalCard,
    border = BorderStroke(1.dp, SlateBorder),
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp, vertical = 14.dp),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.SpaceBetween,
    ) {
      Column(
        modifier = Modifier.weight(1f),
        verticalArrangement = Arrangement.spacedBy(3.dp),
      ) {
        Text(
          text = ward.wardName,
          style = MaterialTheme.typography.titleSmall,
          color = TextPrimary,
          maxLines = 1,
          overflow = TextOverflow.Ellipsis,
        )
        Text(
          text = "${ward.city.displayName} • ${ward.demoActiveCases} demo cases",
          style = MaterialTheme.typography.bodySmall,
          color = TextSecondary,
          maxLines = 1,
          overflow = TextOverflow.Ellipsis,
        )
      }
      Spacer(Modifier.width(12.dp))
      RiskBadge(riskLevel = ward.riskLevel)
    }
  }
}
