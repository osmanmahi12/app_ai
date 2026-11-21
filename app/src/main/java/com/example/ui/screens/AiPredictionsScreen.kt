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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.example.ui.components.DashboardCard
import com.example.ui.components.ForecastBandChartCard
import com.example.ui.theme.CharcoalNavy
import com.example.ui.theme.CyanBright
import com.example.ui.theme.CyanMuted
import com.example.ui.theme.RiskModerateYellow
import com.example.ui.theme.SlateBorder
import com.example.ui.theme.TealAccent
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.viewmodel.DengueDashboardUiState

@Composable
fun AiPredictionsScreen(
  state: DengueDashboardUiState,
  onSelectHorizon: (Int) -> Unit,
  modifier: Modifier = Modifier,
) {
  LazyColumn(
    modifier = modifier.fillMaxSize(),
    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 14.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp),
  ) {
    item(key = "ml_banner") {
      DashboardCard(borderColor = RiskModerateYellow.copy(alpha = 0.4f)) {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
          verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
          Text(
            text = "Stage-2 ML Forecast (UI Placeholder)",
            style = MaterialTheme.typography.titleMedium,
            color = TextPrimary,
          )
          Text(
            text = "Live prediction models are not connected yet. Curves show demo placeholder envelopes.",
            style = MaterialTheme.typography.bodySmall,
            color = TextSecondary,
          )
        }
      }
    }

    item(key = "horizon_row") {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
      ) {
        Text(
          text = "Forecast Horizon",
          style = MaterialTheme.typography.titleMedium,
          color = TextPrimary,
        )
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
          listOf(7, 14).forEach { days ->
            val selected = state.forecastHorizonDays == days
            Surface(
              shape = RoundedCornerShape(10.dp),
              color = if (selected) CyanMuted else CharcoalNavy,
              border = BorderStroke(1.dp, if (selected) CyanBright else SlateBorder),
              modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .clickable { onSelectHorizon(days) }
                .testTag("horizon_${days}d"),
            ) {
              Text(
                text = "$days Days",
                style = MaterialTheme.typography.labelLarge,
                color = if (selected) CyanBright else TextSecondary,
                modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
              )
            }
          }
        }
      }
    }

    item(key = "forecast_chart") {
      ForecastBandChartCard(
        title = "${state.forecastHorizonDays}-Day Trajectory (${state.selectedCity.displayName})",
        points = state.forecastBand,
      )
    }

    item(key = "features_header") {
      Text(
        text = "Model Input Signals",
        style = MaterialTheme.typography.titleMedium,
        color = TextPrimary,
      )
    }

    items(state.mlFeatureWeights, key = { it.name }) { feature ->
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
          ) {
            Text(feature.name, style = MaterialTheme.typography.titleSmall, color = TextPrimary)
            Text("${feature.weightPct}%", style = MaterialTheme.typography.labelMedium, color = CyanBright)
          }
          LinearProgressIndicator(
            progress = { feature.weightPct / 100f },
            modifier = Modifier
              .fillMaxWidth()
              .height(7.dp)
              .clip(RoundedCornerShape(4.dp)),
            color = TealAccent,
            trackColor = CharcoalNavy,
          )
          Text(
            text = feature.currentDemoReading,
            style = MaterialTheme.typography.bodySmall,
            color = TextSecondary,
          )
        }
      }
    }

    item { Spacer(Modifier.height(12.dp)) }
  }
}
