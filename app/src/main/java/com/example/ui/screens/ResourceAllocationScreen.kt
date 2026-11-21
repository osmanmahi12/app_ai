package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.example.ui.components.DashboardCard
import com.example.ui.components.RiskBadge
import com.example.ui.theme.CharcoalNavy
import com.example.ui.theme.CyanBright
import com.example.ui.theme.CyanMuted
import com.example.ui.theme.RiskHighOrange
import com.example.ui.theme.SlateBorder
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.viewmodel.DengueDashboardUiState
import kotlin.math.roundToInt

@Composable
fun ResourceAllocationScreen(
  state: DengueDashboardUiState,
  onBoostChanged: (Float) -> Unit,
  onDispatchClick: (String) -> Unit,
  modifier: Modifier = Modifier,
) {
  LazyColumn(
    modifier = modifier.fillMaxSize(),
    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 14.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp),
  ) {
    item(key = "dispatch_slider") {
      DashboardCard {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
          verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
          ) {
            Text("Fogging Reserve Boost", style = MaterialTheme.typography.titleMedium, color = TextPrimary)
            Text("+${state.foggingDispatchBoostPct.roundToInt()}%", style = MaterialTheme.typography.labelMedium, color = CyanBright)
          }
          Slider(
            value = state.foggingDispatchBoostPct,
            onValueChange = onBoostChanged,
            valueRange = 0f..50f,
            colors = SliderDefaults.colors(
              thumbColor = CyanBright,
              activeTrackColor = CyanBright,
              inactiveTrackColor = CharcoalNavy,
            ),
            modifier = Modifier.testTag("fogging_boost_slider"),
          )
        }
      }
    }

    items(state.resourceSummaries, key = { it.city.name }) { item ->
      val occupancy = (item.occupiedBeds.toFloat() / item.allocatedBeds.coerceAtLeast(1)).coerceIn(0f, 1f)
      val boostedSquads = (item.foggingSquadsActive * (1f + state.foggingDispatchBoostPct / 100f)).roundToInt()
      val boostedCoverage = (item.larvicideCoveragePct + (state.foggingDispatchBoostPct * 0.35f).roundToInt()).coerceAtMost(98)

      DashboardCard {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
          verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
          ) {
            Text(item.city.displayName, style = MaterialTheme.typography.titleLarge, color = TextPrimary)
            RiskBadge(riskLevel = item.priorityLevel)
          }

          Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
            ) {
              Text("Bed Occupancy", style = MaterialTheme.typography.bodySmall, color = TextSecondary)
              Text(
                text = "${item.occupiedBeds}/${item.allocatedBeds} (${(occupancy * 100).roundToInt()}%)",
                style = MaterialTheme.typography.labelMedium,
                color = if (occupancy > 0.78f) RiskHighOrange else CyanBright,
              )
            }
            LinearProgressIndicator(
              progress = { occupancy },
              modifier = Modifier
                .fillMaxWidth()
                .height(7.dp)
                .clip(RoundedCornerShape(4.dp)),
              color = item.priorityLevel.color,
              trackColor = CharcoalNavy,
            )
          }

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
          ) {
            ResourceMiniTile("Platelets", "${item.plateletUnitsAvailable}", Modifier.weight(1f))
            ResourceMiniTile("Squads", "$boostedSquads", Modifier.weight(1f))
            ResourceMiniTile("Coverage", "$boostedCoverage%", Modifier.weight(1f))
          }

          Button(
            onClick = {
              onDispatchClick("Demo dispatch staged for ${item.city.displayName} ($boostedSquads squads).")
            },
            colors = ButtonDefaults.buttonColors(
              containerColor = CyanMuted,
              contentColor = CyanBright,
            ),
            border = BorderStroke(1.dp, CyanBright.copy(alpha = 0.45f)),
            modifier = Modifier
              .fillMaxWidth()
              .testTag("dispatch_button_${item.city.name.lowercase()}"),
          ) {
            Text("Stage Demo Dispatch", style = MaterialTheme.typography.labelLarge)
          }
        }
      }
    }

    item { Spacer(Modifier.height(12.dp)) }
  }
}

@Composable
private fun ResourceMiniTile(
  label: String,
  value: String,
  modifier: Modifier = Modifier,
) {
  Surface(
    modifier = modifier,
    shape = RoundedCornerShape(10.dp),
    color = CharcoalNavy,
    border = BorderStroke(1.dp, SlateBorder),
  ) {
    Column(
      modifier = Modifier.padding(10.dp),
      verticalArrangement = Arrangement.spacedBy(2.dp),
    ) {
      Text(text = label, style = MaterialTheme.typography.labelSmall, color = TextSecondary)
      Text(text = value, style = MaterialTheme.typography.titleMedium, color = TextPrimary)
    }
  }
}
