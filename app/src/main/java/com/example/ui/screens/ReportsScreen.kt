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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.example.model.ReportTemplate
import com.example.ui.components.DashboardCard
import com.example.ui.components.DemoDataBadge
import com.example.ui.theme.CharcoalCard
import com.example.ui.theme.CharcoalNavy
import com.example.ui.theme.CyanBright
import com.example.ui.theme.CyanMuted
import com.example.ui.theme.SlateBorder
import com.example.ui.theme.TealAccent
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.viewmodel.DengueDashboardUiState

@Composable
fun ReportsScreen(
  state: DengueDashboardUiState,
  onPreviewReport: (ReportTemplate) -> Unit,
  onClosePreview: () -> Unit,
  onExportReport: (ReportTemplate) -> Unit,
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
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
      ) {
        Text(
          text = "Situation Reports (${state.selectedCity.displayName})",
          style = MaterialTheme.typography.titleMedium,
          color = TextPrimary,
        )
        DemoDataBadge(label = "TEMPLATES")
      }
    }

    items(state.reports, key = { it.id }) { report ->
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
              text = report.format,
              style = MaterialTheme.typography.labelMedium,
              color = TealAccent,
            )
            Text(
              text = report.frequency,
              style = MaterialTheme.typography.labelSmall,
              color = TextMuted,
            )
          }
          Text(
            text = report.title,
            style = MaterialTheme.typography.titleMedium,
            color = TextPrimary,
          )
          Text(
            text = "Audience: ${report.audience}",
            style = MaterialTheme.typography.labelSmall,
            color = CyanBright,
          )
          Text(
            text = report.summaryPreview,
            style = MaterialTheme.typography.bodySmall,
            color = TextSecondary,
          )
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
          ) {
            OutlinedButton(
              onClick = { onPreviewReport(report) },
              border = BorderStroke(1.dp, SlateBorder),
              colors = ButtonDefaults.outlinedButtonColors(contentColor = TextPrimary),
              modifier = Modifier
                .weight(1f)
                .testTag("preview_report_${report.id}"),
            ) {
              Text("Preview", style = MaterialTheme.typography.labelLarge)
            }
            Button(
              onClick = { onExportReport(report) },
              colors = ButtonDefaults.buttonColors(
                containerColor = CyanMuted,
                contentColor = CyanBright,
              ),
              border = BorderStroke(1.dp, CyanBright.copy(alpha = 0.5f)),
              modifier = Modifier
                .weight(1f)
                .testTag("export_report_${report.id}"),
            ) {
              Text("Export Demo", style = MaterialTheme.typography.labelLarge)
            }
          }
        }
      }
    }

    item { Spacer(Modifier.height(18.dp)) }
  }

  val preview = state.previewingReport
  if (preview != null) {
    AlertDialog(
      onDismissRequest = onClosePreview,
      containerColor = CharcoalCard,
      titleContentColor = TextPrimary,
      textContentColor = TextSecondary,
      title = {
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
          DemoDataBadge(label = "DEMO REPORT PREVIEW")
          Text(text = preview.title, style = MaterialTheme.typography.titleMedium)
        }
      },
      text = {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
          Text("Format: ${preview.format} • ${preview.frequency}", color = TealAccent)
          Text(preview.summaryPreview)
          Text(
            text = "Included KPIs: ${state.kpiSummary.totalDemoCases} demo cases, ${state.kpiSummary.highRiskWardsCount} high-risk wards, status ${state.kpiSummary.predictedStatusLabel}.",
            style = MaterialTheme.typography.bodySmall,
            color = TextMuted,
          )
        }
      },
      confirmButton = {
        TextButton(onClick = {
          onClosePreview()
          onExportReport(preview)
        }) {
          Text("Export Demo", color = CyanBright)
        }
      },
      dismissButton = {
        TextButton(onClick = onClosePreview) {
          Text("Close", color = TextSecondary)
        }
      },
    )
  }
}
