package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.model.CityRiskBarPoint
import com.example.model.ForecastBandPoint
import com.example.model.WeeklyTrendPoint
import com.example.ui.theme.CharcoalElevated
import com.example.ui.theme.CharcoalNavy
import com.example.ui.theme.CyanBright
import com.example.ui.theme.SlateBorder
import com.example.ui.theme.TealAccent
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import kotlin.math.roundToInt

@Composable
fun IncidenceLineChartCard(
  title: String,
  points: List<WeeklyTrendPoint>,
  modifier: Modifier = Modifier,
) {
  var selectedIndex by remember(points) { mutableIntStateOf((points.size - 1).coerceAtLeast(0)) }
  val inspected = points.getOrNull(selectedIndex)
  val maxVal = remember(points) {
    (points.maxOfOrNull { maxOf(it.reportedDemoCases, it.seasonalBaseline) } ?: 100f) * 1.15f
  }

  DashboardCard(modifier = modifier.testTag("incidence_line_chart_card")) {
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
        Text(
          text = title,
          style = MaterialTheme.typography.titleMedium,
          color = TextPrimary,
          modifier = Modifier.weight(1f),
          maxLines = 1,
          overflow = TextOverflow.Ellipsis,
        )
        if (inspected != null) {
          Surface(
            shape = RoundedCornerShape(8.dp),
            color = CharcoalElevated,
            border = BorderStroke(1.dp, CyanBright.copy(alpha = 0.4f)),
          ) {
            Text(
              text = "${inspected.weekLabel}: ${inspected.reportedDemoCases.toInt()} cases",
              style = MaterialTheme.typography.labelSmall,
              color = CyanBright,
              modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            )
          }
        }
      }

      Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
        ChartLegendItem(color = CyanBright, label = "Reported")
        ChartLegendItem(color = TealAccent, label = "Baseline")
      }

      Box(
        modifier = Modifier
          .fillMaxWidth()
          .height(190.dp)
          .clip(RoundedCornerShape(12.dp))
          .background(CharcoalNavy.copy(alpha = 0.7f))
          .padding(horizontal = 12.dp, vertical = 14.dp),
      ) {
        if (points.size >= 2) {
          Box(
            modifier = Modifier
              .fillMaxSize()
              .pointerInput(points) {
                detectTapGestures { offset ->
                  val stepX = size.width / (points.size - 1).coerceAtLeast(1).toFloat()
                  selectedIndex = (offset.x / stepX).roundToInt().coerceIn(0, points.lastIndex)
                }
              }
              .drawWithCache {
                val w = size.width
                val chartBottom = size.height - 10f
                val chartTop = 8f
                val chartHeight = (chartBottom - chartTop).coerceAtLeast(1f)

                fun xAt(i: Int): Float = (i.toFloat() / (points.size - 1).toFloat()) * w
                fun yAt(v: Float): Float = chartBottom - ((v / maxVal).coerceIn(0f, 1f) * chartHeight)

                val gridDash = PathEffect.dashPathEffect(floatArrayOf(8f, 8f))
                val baseDash = PathEffect.dashPathEffect(floatArrayOf(10f, 8f))
                val gridStrokePx = 1.dp.toPx().roundToInt().coerceAtLeast(1).toFloat()
                val selLineStrokePx = 1.dp.toPx().roundToInt().coerceAtLeast(1).toFloat()
                val pointStroke = Stroke(width = 2.dp.toPx())
                val selRadiusPx = 5.dp.toPx()
                val dotRadiusPx = 3.dp.toPx()
                val lineStroke = Stroke(
                  width = 3.dp.toPx(),
                  cap = StrokeCap.Round,
                  join = androidx.compose.ui.graphics.StrokeJoin.Round,
                )
                val baseStroke = Stroke(
                  width = 2.dp.toPx(),
                  cap = StrokeCap.Round,
                  join = androidx.compose.ui.graphics.StrokeJoin.Round,
                  pathEffect = baseDash,
                )

                val basePath = Path()
                val linePath = Path()
                val fillPath = Path()
                val coords = ArrayList<Offset>(points.size)

                points.forEachIndexed { idx, p ->
                  val x = xAt(idx)
                  val yb = yAt(p.seasonalBaseline)
                  val yr = yAt(p.reportedDemoCases)
                  coords.add(Offset(x, yr))
                  if (idx == 0) {
                    basePath.moveTo(x, yb)
                    linePath.moveTo(x, yr)
                    fillPath.moveTo(x, chartBottom)
                    fillPath.lineTo(x, yr)
                  } else {
                    val prevX = xAt(idx - 1)
                    val midX = (prevX + x) / 2f
                    basePath.cubicTo(midX, yAt(points[idx - 1].seasonalBaseline), midX, yb, x, yb)
                    val prevYr = yAt(points[idx - 1].reportedDemoCases)
                    linePath.cubicTo(midX, prevYr, midX, yr, x, yr)
                    fillPath.cubicTo(midX, prevYr, midX, yr, x, yr)
                  }
                  if (idx == points.lastIndex) {
                    fillPath.lineTo(x, chartBottom)
                    fillPath.close()
                  }
                }

                val fillBrush = Brush.verticalGradient(
                  colors = listOf(CyanBright.copy(alpha = 0.25f), Color.Transparent),
                  startY = chartTop,
                  endY = chartBottom,
                )

                onDrawBehind {
                  for (step in 0..3) {
                    val y = (chartTop + chartHeight * (step / 3f)).roundToInt().toFloat()
                    drawLine(
                      color = SlateBorder.copy(alpha = 0.45f),
                      start = Offset(0f, y),
                      end = Offset(w, y),
                      strokeWidth = gridStrokePx,
                      pathEffect = gridDash,
                    )
                  }
                  drawPath(path = basePath, color = TealAccent.copy(alpha = 0.85f), style = baseStroke)
                  drawPath(path = fillPath, brush = fillBrush)
                  drawPath(path = linePath, color = CyanBright, style = lineStroke)

                  val selPt = coords.getOrNull(selectedIndex)
                  if (selPt != null) {
                    drawLine(
                      color = CyanBright.copy(alpha = 0.35f),
                      start = Offset(selPt.x, chartTop),
                      end = Offset(selPt.x, chartBottom),
                      strokeWidth = selLineStrokePx,
                    )
                  }

                  coords.forEachIndexed { idx, pt ->
                    val isSel = idx == selectedIndex
                    val r = if (isSel) selRadiusPx else dotRadiusPx
                    drawCircle(color = if (isSel) CyanBright else CharcoalNavy, radius = r, center = pt)
                    drawCircle(color = CyanBright, radius = r, center = pt, style = pointStroke)
                  }
                }
              }
          )
        }
      }

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
      ) {
        points.filterIndexed { index, _ -> index == 0 || index == points.lastIndex || index % 3 == 0 }
          .forEach { p ->
            Text(
              text = p.weekLabel,
              style = MaterialTheme.typography.labelSmall,
              color = TextMuted,
            )
          }
      }
    }
  }
}

@Composable
fun RiskBarChartCard(
  title: String,
  bars: List<CityRiskBarPoint>,
  modifier: Modifier = Modifier,
  onBarSelected: ((CityRiskBarPoint) -> Unit)? = null,
) {
  var selectedIdx by remember(bars) { mutableIntStateOf(0) }
  val maxVal = remember(bars) { (bars.maxOfOrNull { it.value } ?: 100f) * 1.15f }
  val selectedBar = bars.getOrNull(selectedIdx)

  DashboardCard(modifier = modifier.testTag("risk_bar_chart_card")) {
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
        Text(
          text = title,
          style = MaterialTheme.typography.titleMedium,
          color = TextPrimary,
          modifier = Modifier.weight(1f),
          maxLines = 1,
          overflow = TextOverflow.Ellipsis,
        )
        if (selectedBar != null) {
          RiskBadge(riskLevel = selectedBar.riskLevel)
        }
      }

      Box(
        modifier = Modifier
          .fillMaxWidth()
          .height(190.dp)
          .clip(RoundedCornerShape(12.dp))
          .background(CharcoalNavy.copy(alpha = 0.7f))
          .padding(14.dp),
      ) {
        if (bars.isNotEmpty()) {
          Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.Bottom,
          ) {
            bars.forEachIndexed { idx, bar ->
              val isSel = idx == selectedIdx
              val ratio = (bar.value / maxVal).coerceIn(0.08f, 1f)
              val barColor = bar.riskLevel.color.copy(alpha = if (isSel) 0.92f else 0.55f)
              Column(
                modifier = Modifier
                  .weight(1f)
                  .fillMaxSize()
                  .clip(RoundedCornerShape(8.dp))
                  .clickable {
                    selectedIdx = idx
                    onBarSelected?.invoke(bar)
                  },
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Bottom,
              ) {
                Text(
                  text = bar.value.toInt().toString(),
                  style = MaterialTheme.typography.labelSmall,
                  color = if (isSel) TextPrimary else TextSecondary,
                  fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal,
                )
                Spacer(Modifier.height(4.dp))
                Box(
                  modifier = Modifier
                    .fillMaxWidth(0.68f)
                    .weight(1f)
                    .drawBehind {
                      val barHeight = size.height * ratio
                      val topY = size.height - barHeight
                      val radiusPx = 10.dp.toPx()
                      drawRoundRect(
                        color = barColor,
                        topLeft = Offset(0f, topY),
                        size = Size(size.width, barHeight),
                        cornerRadius = CornerRadius(radiusPx, radiusPx),
                      )
                    },
                )
                Spacer(Modifier.height(6.dp))
                Text(
                  text = bar.label,
                  style = MaterialTheme.typography.labelSmall,
                  color = if (isSel) CyanBright else TextMuted,
                  maxLines = 1,
                  overflow = TextOverflow.Ellipsis,
                )
              }
            }
          }
        }
      }
    }
  }
}

@Composable
fun ForecastBandChartCard(
  title: String,
  points: List<ForecastBandPoint>,
  modifier: Modifier = Modifier,
) {
  val maxVal = remember(points) { (points.maxOfOrNull { it.upperBound } ?: 100f) * 1.15f }

  DashboardCard(modifier = modifier.testTag("forecast_band_chart")) {
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
        Text(
          text = title,
          style = MaterialTheme.typography.titleMedium,
          color = TextPrimary,
          modifier = Modifier.weight(1f),
        )
        DemoDataBadge(label = "PLACEHOLDER")
      }

      Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
        ChartLegendItem(color = CyanBright, label = "Trajectory")
        ChartLegendItem(color = TealAccent.copy(alpha = 0.5f), label = "Confidence Band")
      }

      Box(
        modifier = Modifier
          .fillMaxWidth()
          .height(180.dp)
          .clip(RoundedCornerShape(12.dp))
          .background(CharcoalNavy.copy(alpha = 0.7f))
          .padding(14.dp),
      ) {
        if (points.size >= 2) {
          Box(
            modifier = Modifier
              .fillMaxSize()
              .drawWithCache {
                val w = size.width
                val h = size.height - 8f
                fun xAt(i: Int) = (i.toFloat() / (points.size - 1)) * w
                fun yAt(v: Float) = h - ((v / maxVal).coerceIn(0f, 1f) * (h - 8f))

                val bandPath = Path()
                points.forEachIndexed { idx, p ->
                  val x = xAt(idx)
                  val y = yAt(p.upperBound)
                  if (idx == 0) bandPath.moveTo(x, y) else bandPath.lineTo(x, y)
                }
                for (idx in points.lastIndex downTo 0) {
                  bandPath.lineTo(xAt(idx), yAt(points[idx].lowerBound))
                }
                bandPath.close()

                val centerPath = Path()
                points.forEachIndexed { idx, p ->
                  val x = xAt(idx)
                  val y = yAt(p.predictedCases)
                  if (idx == 0) centerPath.moveTo(x, y) else centerPath.lineTo(x, y)
                }

                val centerStroke = Stroke(
                  width = 2.5.dp.toPx(),
                  cap = StrokeCap.Round,
                  pathEffect = PathEffect.dashPathEffect(floatArrayOf(12f, 8f)),
                )

                onDrawBehind {
                  drawPath(path = bandPath, color = TealAccent.copy(alpha = 0.22f))
                  drawPath(path = centerPath, color = CyanBright, style = centerStroke)
                }
              }
          )
        }
      }

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
      ) {
        points.filterIndexed { i, _ -> i == 0 || i == points.lastIndex || i == points.size / 2 }
          .forEach { pt ->
            Text(
              text = "${pt.dayLabel} (~${pt.predictedCases.toInt()})",
              style = MaterialTheme.typography.labelSmall,
              color = TextMuted,
            )
          }
      }
    }
  }
}

@Composable
private fun ChartLegendItem(color: Color, label: String) {
  Row(
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.spacedBy(6.dp),
  ) {
    Box(
      modifier = Modifier
        .size(9.dp)
        .clip(CircleShape)
        .background(color)
    )
    Text(
      text = label,
      style = MaterialTheme.typography.bodySmall,
      color = TextSecondary,
    )
  }
}
