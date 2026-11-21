package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Science
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.model.CityFilter
import com.example.model.NavSection
import com.example.model.RiskLevel
import com.example.ui.theme.CharcoalCard
import com.example.ui.theme.CharcoalNavy
import com.example.ui.theme.CyanBright
import com.example.ui.theme.CyanMuted
import com.example.ui.theme.DemoBadgeBg
import com.example.ui.theme.DemoBadgeBorder
import com.example.ui.theme.NavyDeep
import com.example.ui.theme.SlateBorder
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun DemoDataBadge(
  modifier: Modifier = Modifier,
  label: String = "DEMO DATA",
) {
  Surface(
    modifier = modifier,
    shape = RoundedCornerShape(8.dp),
    color = DemoBadgeBg,
    border = BorderStroke(1.dp, DemoBadgeBorder),
  ) {
    Row(
      modifier = Modifier.padding(horizontal = 9.dp, vertical = 4.dp),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.spacedBy(5.dp),
    ) {
      Icon(
        imageVector = Icons.Filled.Science,
        contentDescription = null,
        tint = CyanBright,
        modifier = Modifier.size(13.dp),
      )
      Text(
        text = label,
        style = MaterialTheme.typography.labelSmall,
        color = CyanBright,
        maxLines = 1,
      )
    }
  }
}

@Composable
fun RiskBadge(
  riskLevel: RiskLevel,
  modifier: Modifier = Modifier,
  customLabel: String? = null,
) {
  Surface(
    modifier = modifier,
    shape = RoundedCornerShape(8.dp),
    color = riskLevel.containerColor,
    border = BorderStroke(1.dp, riskLevel.borderColor),
  ) {
    Row(
      modifier = Modifier.padding(horizontal = 9.dp, vertical = 4.dp),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.spacedBy(6.dp),
    ) {
      Box(
        modifier = Modifier
          .size(7.dp)
          .clip(CircleShape)
          .background(riskLevel.color)
      )
      Text(
        text = (customLabel ?: riskLevel.label).uppercase(),
        style = MaterialTheme.typography.labelSmall,
        color = riskLevel.color,
        fontWeight = FontWeight.Bold,
        maxLines = 1,
      )
    }
  }
}

@Composable
fun DashboardCard(
  modifier: Modifier = Modifier,
  borderColor: Color = SlateBorder,
  content: @Composable () -> Unit,
) {
  Surface(
    modifier = modifier,
    shape = MaterialTheme.shapes.large,
    color = CharcoalCard,
    border = BorderStroke(1.dp, borderColor),
  ) {
    content()
  }
}

@Composable
fun KpiCard(
  title: String,
  value: String,
  subtitle: String,
  icon: ImageVector,
  accentColor: Color,
  modifier: Modifier = Modifier,
  riskBadge: RiskLevel? = null,
  tagName: String,
) {
  DashboardCard(
    modifier = modifier.testTag(tagName),
    borderColor = SlateBorder,
  ) {
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
          text = title,
          style = MaterialTheme.typography.bodySmall,
          color = TextSecondary,
          maxLines = 1,
          overflow = TextOverflow.Ellipsis,
          modifier = Modifier.weight(1f),
        )
        Spacer(Modifier.width(6.dp))
        Icon(
          imageVector = icon,
          contentDescription = title,
          tint = accentColor,
          modifier = Modifier.size(18.dp),
        )
      }

      if (riskBadge != null) {
        RiskBadge(riskLevel = riskBadge, customLabel = value)
      } else {
        Text(
          text = value,
          style = MaterialTheme.typography.headlineMedium,
          color = TextPrimary,
          maxLines = 1,
          overflow = TextOverflow.Ellipsis,
        )
      }

      Text(
        text = subtitle,
        style = MaterialTheme.typography.labelSmall,
        color = accentColor.copy(alpha = 0.9f),
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
      )
    }
  }
}

@Composable
fun DashboardTopBar(
  currentSection: NavSection,
  selectedCity: CityFilter,
  showMenuButton: Boolean,
  onMenuClick: () -> Unit,
  onSelectCity: (CityFilter) -> Unit,
  modifier: Modifier = Modifier,
) {
  Surface(
    modifier = modifier.fillMaxWidth(),
    color = NavyDeep,
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(start = 16.dp, end = 16.dp, top = 10.dp, bottom = 12.dp),
      verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          modifier = Modifier.weight(1f),
        ) {
          if (showMenuButton) {
            IconButton(
              onClick = onMenuClick,
              modifier = Modifier
                .minimumInteractiveComponentSize()
                .testTag("open_sidebar_button"),
            ) {
              Icon(
                imageVector = Icons.Filled.Menu,
                contentDescription = "Open navigation sidebar",
                tint = CyanBright,
              )
            }
            Spacer(Modifier.width(4.dp))
          }
          Text(
            text = currentSection.title,
            style = MaterialTheme.typography.titleLarge,
            color = TextPrimary,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.testTag("topbar_section_title"),
          )
        }
        Spacer(Modifier.width(10.dp))
        DemoDataBadge(label = "DEMO DATA")
      }

      // Clean City Filter Pills
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
      ) {
        CityFilter.entries.forEach { city ->
          val isSelected = city == selectedCity
          Surface(
            shape = RoundedCornerShape(10.dp),
            color = if (isSelected) CyanMuted else CharcoalNavy,
            border = BorderStroke(1.dp, if (isSelected) CyanBright else SlateBorder),
            modifier = Modifier
              .clip(RoundedCornerShape(10.dp))
              .clickable { onSelectCity(city) }
              .testTag("city_filter_${city.name.lowercase()}"),
          ) {
            Text(
              text = city.displayName,
              style = MaterialTheme.typography.labelLarge,
              color = if (isSelected) CyanBright else TextSecondary,
              maxLines = 1,
              modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
            )
          }
        }
      }
    }
  }
}
