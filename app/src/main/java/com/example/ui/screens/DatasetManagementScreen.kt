package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.example.ui.components.DashboardCard
import com.example.ui.components.DemoDataBadge
import com.example.ui.theme.CharcoalNavy
import com.example.ui.theme.CyanBright
import com.example.ui.theme.CyanMuted
import com.example.ui.theme.RiskLowBg
import com.example.ui.theme.RiskLowBorder
import com.example.ui.theme.RiskLowGreen
import com.example.ui.theme.RiskModerateBg
import com.example.ui.theme.RiskModerateBorder
import com.example.ui.theme.RiskModerateYellow
import com.example.ui.theme.SlateBorder
import com.example.ui.theme.TealAccent
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.viewmodel.DengueDashboardUiState

@Composable
fun DatasetManagementScreen(
  state: DengueDashboardUiState,
  onResetDemoWorkspace: () -> Unit,
  onTestConnector: (String) -> Unit,
  modifier: Modifier = Modifier,
) {
  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .padding(horizontal = 16.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp),
  ) {
    item { Spacer(Modifier.height(2.dp)) }

    item {
      DashboardCard(borderColor = CyanBright.copy(alpha = 0.35f)) {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
          verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
          ) {
            Text(
              text = "Data Architecture & Demo Controls",
              style = MaterialTheme.typography.titleMedium,
              color = TextPrimary,
            )
            DemoDataBadge(label = "STAGE 1 SCHEMA")
          }
          Text(
            text = "All views are backed by a clean DengueDataRepository interface currently bound to DemoDengueDataRepository. Live API/ML connectors will plug in here in Stage 2.",
            style = MaterialTheme.typography.bodySmall,
            color = TextSecondary,
          )
          Button(
            onClick = onResetDemoWorkspace,
            colors = ButtonDefaults.buttonColors(
              containerColor = CyanMuted,
              contentColor = CyanBright,
            ),
            border = BorderStroke(1.dp, CyanBright.copy(alpha = 0.5f)),
            modifier = Modifier.testTag("reset_demo_workspace_button"),
          ) {
            Text("Reset Demo Snapshot & Filters", style = MaterialTheme.typography.labelLarge)
          }
        }
      }
    }

    item {
      Text(
        text = "Ingestion Feeds & Planned Connectors",
        style = MaterialTheme.typography.titleMedium,
        color = TextPrimary,
      )
    }

    items(state.datasetConnectors, key = { it.id }) { connector ->
      DashboardCard {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
          verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
          ) {
            Text(
              text = connector.name,
              style = MaterialTheme.typography.titleMedium,
              color = TextPrimary,
              modifier = Modifier.weight(1f),
            )
            Surface(
              shape = RoundedCornerShape(8.dp),
              color = if (connector.isReady) RiskLowBg else RiskModerateBg,
              border = BorderStroke(
                1.dp,
                if (connector.isReady) RiskLowBorder else RiskModerateBorder,
              ),
            ) {
              Text(
                text = connector.statusLabel,
                style = MaterialTheme.typography.labelSmall,
                color = if (connector.isReady) RiskLowGreen else RiskModerateYellow,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
              )
            }
          }

          Text(
            text = "${connector.sourceAgency} • ${connector.recordCountLabel}",
            style = MaterialTheme.typography.bodySmall,
            color = TealAccent,
          )

          Surface(
            shape = RoundedCornerShape(8.dp),
            color = CharcoalNavy,
            border = BorderStroke(1.dp, SlateBorder),
          ) {
            Text(
              text = "Schema: ${connector.schemaFields}",
              style = MaterialTheme.typography.labelSmall,
              color = TextMuted,
              modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            )
          }

          Button(
            onClick = {
              onTestConnector("${connector.name}: ${connector.statusLabel} (${connector.recordCountLabel})")
            },
            colors = ButtonDefaults.buttonColors(
              containerColor = CharcoalNavy,
              contentColor = TextPrimary,
            ),
            border = BorderStroke(1.dp, SlateBorder),
            modifier = Modifier.testTag("inspect_connector_${connector.id}"),
          ) {
            Text("Inspect Feed Contract", style = MaterialTheme.typography.labelLarge)
          }
        }
      }
    }

    item { Spacer(Modifier.height(18.dp)) }
  }
}
