package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.model.MapLayerMode
import com.example.model.RiskLevel
import com.example.model.WardHotspot
import com.example.ui.components.DashboardCard
import com.example.ui.components.RiskBadge
import com.example.ui.theme.CharcoalNavy
import com.example.ui.theme.CyanBright
import com.example.ui.theme.CyanMuted
import com.example.ui.theme.NavyVoid
import com.example.ui.theme.SlateBorder
import com.example.ui.theme.TealAccent
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.viewmodel.DengueDashboardUiState
import kotlin.math.hypot

@Composable
fun OutbreakMapScreen(
  state: DengueDashboardUiState,
  isWideLayout: Boolean,
  onSelectLayer: (MapLayerMode) -> Unit,
  onSelectWard: (String) -> Unit,
  modifier: Modifier = Modifier,
) {
  val selectedWard = state.selectedWard

  LazyColumn(
    modifier = modifier.fillMaxSize(),
    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 14.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp),
  ) {
    item(key = "layer_strip") {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
      ) {
        MapLayerMode.entries.forEach { mode ->
          val active = mode == state.mapLayerMode
          Surface(
            shape = RoundedCornerShape(10.dp),
            color = if (active) CyanMuted else CharcoalNavy,
            border = BorderStroke(1.dp, if (active) CyanBright else SlateBorder),
            modifier = Modifier
              .clip(RoundedCornerShape(10.dp))
              .clickable { onSelectLayer(mode) }
              .testTag("map_layer_${mode.name.lowercase()}"),
          ) {
            Text(
              text = mode.label,
              style = MaterialTheme.typography.labelLarge,
              color = if (active) CyanBright else TextSecondary,
              modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
            )
          }
        }
      }
    }

    item(key = "map_and_inspector") {
      if (isWideLayout) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(14.dp),
        ) {
          UrbanRadarMapCanvas(
            wards = state.wards,
            selectedWardId = selectedWard?.id,
            layerMode = state.mapLayerMode,
            onSelectWard = onSelectWard,
            modifier = Modifier.weight(1.35f),
          )
          WardInspectorCard(
            ward = selectedWard,
            modifier = Modifier.weight(1f),
          )
        }
      } else {
        Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
          UrbanRadarMapCanvas(
            wards = state.wards,
            selectedWardId = selectedWard?.id,
            layerMode = state.mapLayerMode,
            onSelectWard = onSelectWard,
          )
          WardInspectorCard(ward = selectedWard)
        }
      }
    }

    item(key = "nodes_header") {
      Text(
        text = "Ward Hotspots (${state.wards.size})",
        style = MaterialTheme.typography.titleMedium,
        color = TextPrimary,
      )
    }

    items(state.wards, key = { it.id }) { ward ->
      WardWatchlistRow(
        ward = ward,
        onClick = { onSelectWard(ward.id) },
      )
    }

    item { Spacer(Modifier.height(12.dp)) }
  }
}

@Composable
private fun UrbanRadarMapCanvas(
  wards: List<WardHotspot>,
  selectedWardId: String?,
  layerMode: MapLayerMode,
  onSelectWard: (String) -> Unit,
  modifier: Modifier = Modifier,
) {
  DashboardCard(modifier = modifier.testTag("outbreak_radar_map")) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp),
      verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
      Text(
        text = "Bangladesh Urban Corridor (${layerMode.label})",
        style = MaterialTheme.typography.titleMedium,
        color = TextPrimary,
      )

      Box(
        modifier = Modifier
          .fillMaxWidth()
          .height(250.dp)
          .clip(RoundedCornerShape(14.dp))
          .background(NavyVoid),
      ) {
        Box(
          modifier = Modifier
            .fillMaxSize()
            .pointerInput(wards) {
              detectTapGestures { tapOffset ->
                val nearest = wards.minByOrNull { w ->
                  val px = w.mapX * size.width
                  val py = w.mapY * size.height
                  hypot(tapOffset.x - px, tapOffset.y - py)
                }
                if (nearest != null) onSelectWard(nearest.id)
              }
            }
            .drawWithCache {
              val w = size.width
              val h = size.height
              val step = 40.dp.toPx()
              val gridStrokePx = 1.dp.toPx()
              val corridorStroke = Stroke(
                width = 2.dp.toPx(),
                pathEffect = PathEffect.dashPathEffect(floatArrayOf(12f, 10f)),
              )
              val ringStroke = Stroke(width = 1.dp.toPx())
              val selRingStroke = Stroke(width = 2.dp.toPx())
              val corridorPath = Path().apply {
                moveTo(w * 0.36f, h * 0.14f)
                lineTo(w * 0.41f, h * 0.37f)
                lineTo(w * 0.58f, h * 0.56f)
                lineTo(w * 0.80f, h * 0.72f)
              }
              val center = Offset(w * 0.42f, h * 0.38f)
              val minDim = minOf(w, h)
              val ring1 = minDim * 0.20f
              val ring2 = minDim * 0.38f

              onDrawBehind {
                var x = 0f
                while (x < w) {
                  drawLine(SlateBorder.copy(alpha = 0.3f), Offset(x, 0f), Offset(x, h), gridStrokePx)
                  x += step
                }
                var y = 0f
                while (y < h) {
                  drawLine(SlateBorder.copy(alpha = 0.3f), Offset(0f, y), Offset(w, y), gridStrokePx)
                  y += step
                }
                drawPath(path = corridorPath, color = TealAccent.copy(alpha = 0.3f), style = corridorStroke)
                drawCircle(color = CyanBright.copy(alpha = 0.14f), radius = ring1, center = center, style = ringStroke)
                drawCircle(color = CyanBright.copy(alpha = 0.14f), radius = ring2, center = center, style = ringStroke)

                wards.forEach { ward ->
                  val pt = Offset(ward.mapX * w, ward.mapY * h)
                  val isSelected = ward.id == selectedWardId
                  val metricScale = when (layerMode) {
                    MapLayerMode.VECTOR_DENSITY -> ward.breteauIndex / 45f
                    MapLayerMode.STANDING_WATER -> ward.rainfallMm7d / 170f
                    MapLayerMode.HOSPITAL_LOAD -> ward.hospitalOccupancyPct / 100f
                  }.coerceIn(0.3f, 1.05f)
                  val baseRadius = 11.dp.toPx() + 10.dp.toPx() * metricScale

                  drawCircle(
                    color = ward.riskLevel.color.copy(alpha = if (isSelected) 0.34f else 0.18f),
                    radius = baseRadius * 1.4f,
                    center = pt,
                  )
                  drawCircle(
                    color = ward.riskLevel.color,
                    radius = if (isSelected) 8.dp.toPx() else 6.dp.toPx(),
                    center = pt,
                  )
                  if (isSelected) {
                    drawCircle(
                      color = Color.White,
                      radius = 12.dp.toPx(),
                      center = pt,
                      style = selRingStroke,
                    )
                  }
                }
              }
            }
        )

        Row(
          modifier = Modifier
            .align(Alignment.BottomStart)
            .padding(10.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(CharcoalNavy.copy(alpha = 0.9f))
            .padding(horizontal = 10.dp, vertical = 6.dp),
          horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
          RiskLevel.entries.forEach { level ->
            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(4.dp),
            ) {
              Box(
                modifier = Modifier
                  .size(7.dp)
                  .clip(RoundedCornerShape(4.dp))
                  .background(level.color)
              )
              Text(level.label, style = MaterialTheme.typography.labelSmall, color = TextSecondary)
            }
          }
        }
      }
    }
  }
}

@Composable
private fun WardInspectorCard(
  ward: WardHotspot?,
  modifier: Modifier = Modifier,
) {
  DashboardCard(modifier = modifier.testTag("ward_inspector_card")) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp),
      verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
      if (ward == null) {
        Text("Tap a hotspot to inspect ward details.", style = MaterialTheme.typography.bodyMedium, color = TextSecondary)
      } else {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically,
        ) {
          Text(ward.wardName, style = MaterialTheme.typography.titleMedium, color = TextPrimary, modifier = Modifier.weight(1f))
          RiskBadge(riskLevel = ward.riskLevel)
        }
        Text(
          text = "${ward.city.displayName} • ${ward.demoActiveCases} demo cases • Breteau Index ${ward.breteauIndex}",
          style = MaterialTheme.typography.bodySmall,
          color = CyanBright,
          fontWeight = FontWeight.Medium,
        )
        Text(
          text = ward.primaryDriver,
          style = MaterialTheme.typography.bodySmall,
          color = TextSecondary,
        )
      }
    }
  }
}
