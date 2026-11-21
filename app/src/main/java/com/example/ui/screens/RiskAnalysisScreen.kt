package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.example.model.RiskLevel
import com.example.ui.components.DashboardCard
import com.example.ui.components.RiskBadge
import com.example.ui.theme.CharcoalNavy
import com.example.ui.theme.CyanBright
import com.example.ui.theme.CyanMuted
import com.example.ui.theme.SlateBorder
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.viewmodel.DengueDashboardUiState

@Composable
fun RiskAnalysisScreen(
  state: DengueDashboardUiState,
  onSelectRiskFilter: (RiskLevel?) -> Unit,
  onSelectWard: (String) -> Unit,
  modifier: Modifier = Modifier,
) {
  val filteredWards = state.wards.filter {
    state.selectedRiskFilter == null || it.riskLevel == state.selectedRiskFilter
  }

  LazyColumn(
    modifier = modifier.fillMaxSize(),
    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 14.dp),
    verticalArrangement = Arrangement.spacedBy(14.dp),
  ) {
    item(key = "risk_matrix_header") {
      Text(
        text = "Risk Drivers (${state.selectedCity.displayName})",
        style = MaterialTheme.typography.titleMedium,
        color = TextPrimary,
      )
    }

    items(state.riskFactors, key = { it.factorName }) { metric ->
      DashboardCard {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(14.dp),
          verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
          ) {
            Text(
              text = metric.factorName,
              style = MaterialTheme.typography.titleSmall,
              color = TextPrimary,
              modifier = Modifier.weight(1f),
            )
            RiskBadge(
              riskLevel = metric.riskLevel,
              customLabel = "${metric.scoreOutOf100}/100",
            )
          }
          LinearProgressIndicator(
            progress = { metric.scoreOutOf100 / 100f },
            modifier = Modifier
              .fillMaxWidth()
              .height(7.dp)
              .clip(RoundedCornerShape(4.dp)),
            color = metric.riskLevel.color,
            trackColor = CharcoalNavy,
          )
        }
      }
    }

    item(key = "ward_filter_chips") {
      Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text(
          text = "Filter Wards",
          style = MaterialTheme.typography.titleMedium,
          color = TextPrimary,
        )
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
          horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
          val allSelected = state.selectedRiskFilter == null
          Surface(
            shape = RoundedCornerShape(10.dp),
            color = if (allSelected) CyanMuted else CharcoalNavy,
            border = BorderStroke(1.dp, if (allSelected) CyanBright else SlateBorder),
            modifier = Modifier
              .clip(RoundedCornerShape(10.dp))
              .clickable { onSelectRiskFilter(null) }
              .testTag("risk_filter_all"),
          ) {
            Text(
              text = "All (${state.wards.size})",
              style = MaterialTheme.typography.labelLarge,
              color = if (allSelected) CyanBright else TextSecondary,
              modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            )
          }

          RiskLevel.entries.forEach { level ->
            val selected = state.selectedRiskFilter == level
            val count = state.wards.count { it.riskLevel == level }
            Surface(
              shape = RoundedCornerShape(10.dp),
              color = if (selected) level.containerColor else CharcoalNavy,
              border = BorderStroke(1.dp, if (selected) level.color else SlateBorder),
              modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .clickable { onSelectRiskFilter(level) }
                .testTag("risk_filter_${level.name.lowercase()}"),
            ) {
              Text(
                text = "${level.label} ($count)",
                style = MaterialTheme.typography.labelLarge,
                color = if (selected) level.color else TextSecondary,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
              )
            }
          }
        }
      }
    }

    if (filteredWards.isEmpty()) {
      item(key = "empty_wards") {
        DashboardCard {
          Text(
            text = "No wards match this risk level.",
            style = MaterialTheme.typography.bodyMedium,
            color = TextMuted,
            modifier = Modifier.padding(16.dp),
          )
        }
      }
    } else {
      items(filteredWards, key = { it.id }) { ward ->
        WardWatchlistRow(
          ward = ward,
          onClick = { onSelectWard(ward.id) },
        )
      }
    }

    item { Spacer(Modifier.height(12.dp)) }
  }
}
