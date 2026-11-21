package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.NavigationRailItemDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.R
import com.example.model.NavSection
import com.example.ui.theme.CharcoalCard
import com.example.ui.theme.CharcoalNavy
import com.example.ui.theme.CyanBright
import com.example.ui.theme.CyanMuted
import com.example.ui.theme.NavyDeep
import com.example.ui.theme.NavyVoid
import com.example.ui.theme.SlateBorder
import com.example.ui.theme.TealAccent
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun SidebarContent(
  selectedSection: NavSection,
  onSelectSection: (NavSection) -> Unit,
  modifier: Modifier = Modifier,
) {
  Surface(
    modifier = modifier.fillMaxHeight(),
    color = NavyDeep,
    border = BorderStroke(1.dp, SlateBorder.copy(alpha = 0.7f)),
  ) {
    Column(
      modifier = Modifier
        .fillMaxHeight()
        .statusBarsPadding()
        .navigationBarsPadding()
        .padding(horizontal = 14.dp, vertical = 16.dp),
      verticalArrangement = Arrangement.SpaceBetween,
    ) {
      Column(
        modifier = Modifier
          .weight(1f)
          .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(6.dp),
      ) {
        // Brand Header
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 6.dp, vertical = 6.dp),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
          Image(
            painter = painterResource(id = R.drawable.ic_dengueguard_logo),
            contentDescription = "DengueGuard AI emblem",
            modifier = Modifier.size(40.dp),
            contentScale = ContentScale.Fit,
          )
          Column {
            Text(
              text = "DengueGuard AI",
              style = MaterialTheme.typography.titleMedium,
              color = TextPrimary,
              fontWeight = FontWeight.Bold,
            )
            Text(
              text = "BANGLADESH URBAN INTEL",
              style = MaterialTheme.typography.labelSmall,
              color = TealAccent,
            )
          }
        }

        Spacer(Modifier.height(6.dp))
        HorizontalDivider(color = SlateBorder.copy(alpha = 0.6f))
        Spacer(Modifier.height(6.dp))

        Text(
          text = "INTELLIGENCE MODULES",
          style = MaterialTheme.typography.labelSmall,
          color = TextMuted,
          modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
        )

        NavSection.entries.forEach { section ->
          val isSelected = section == selectedSection
          Surface(
            modifier = Modifier
              .fillMaxWidth()
              .minimumInteractiveComponentSize()
              .clip(RoundedCornerShape(12.dp))
              .clickable { onSelectSection(section) }
              .testTag("sidebar_nav_${section.name.lowercase()}"),
            shape = RoundedCornerShape(12.dp),
            color = if (isSelected) CyanMuted.copy(alpha = 0.65f) else Color.Transparent,
            border = if (isSelected) BorderStroke(1.dp, CyanBright.copy(alpha = 0.55f)) else null,
          ) {
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 11.dp),
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
              Icon(
                imageVector = if (isSelected) section.selectedIcon else section.unselectedIcon,
                contentDescription = section.title,
                tint = if (isSelected) CyanBright else TextSecondary,
                modifier = Modifier.size(20.dp),
              )
              Text(
                text = section.title,
                style = MaterialTheme.typography.labelLarge,
                color = if (isSelected) TextPrimary else TextSecondary,
                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
              )
            }
          }
        }
      }

      Spacer(Modifier.height(10.dp))

      // Footer Architecture Status
      Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = CharcoalCard,
        border = BorderStroke(1.dp, SlateBorder),
      ) {
        Column(
          modifier = Modifier.padding(12.dp),
          verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp),
          ) {
            Box(
              modifier = Modifier
                .size(8.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(TealAccent)
            )
            Text(
              text = "STAGE 1 FRONTEND",
              style = MaterialTheme.typography.labelSmall,
              color = TealAccent,
            )
          }
          Text(
            text = "Demo Mode • 4 BD Cities",
            style = MaterialTheme.typography.bodySmall,
            color = TextSecondary,
          )
        }
      }
    }
  }
}

@Composable
fun MediumNavRail(
  selectedSection: NavSection,
  onSelectSection: (NavSection) -> Unit,
  onOpenFullDrawer: () -> Unit,
  modifier: Modifier = Modifier,
) {
  NavigationRail(
    modifier = modifier.fillMaxHeight(),
    containerColor = NavyDeep,
    contentColor = TextPrimary,
    header = {
      NavigationRailItem(
        selected = false,
        onClick = onOpenFullDrawer,
        icon = {
          Icon(
            imageVector = Icons.Filled.Menu,
            contentDescription = "Expand sidebar",
            tint = CyanBright,
          )
        },
        label = { Text("Menu", style = MaterialTheme.typography.labelSmall) },
      )
    },
  ) {
    NavSection.entries.take(6).forEach { section ->
      val isSelected = section == selectedSection
      NavigationRailItem(
        selected = isSelected,
        onClick = { onSelectSection(section) },
        icon = {
          Icon(
            imageVector = if (isSelected) section.selectedIcon else section.unselectedIcon,
            contentDescription = section.title,
          )
        },
        label = {
          Text(
            text = section.shortTitle,
            style = MaterialTheme.typography.labelSmall,
            maxLines = 1,
          )
        },
        colors = NavigationRailItemDefaults.colors(
          selectedIconColor = CyanBright,
          selectedTextColor = CyanBright,
          indicatorColor = CyanMuted,
          unselectedIconColor = TextSecondary,
          unselectedTextColor = TextMuted,
        ),
      )
    }
  }
}

@Composable
fun CompactBottomNavBar(
  selectedSection: NavSection,
  onSelectSection: (NavSection) -> Unit,
  onOpenMoreSidebar: () -> Unit,
  modifier: Modifier = Modifier,
) {
  val primaryTabs = listOf(
    NavSection.OVERVIEW,
    NavSection.OUTBREAK_MAP,
    NavSection.AI_PREDICTIONS,
    NavSection.RISK_ANALYSIS,
  )
  NavigationBar(
    modifier = modifier.fillMaxWidth(),
    containerColor = NavyDeep,
    tonalElevation = 4.dp,
  ) {
    primaryTabs.forEach { section ->
      val isSelected = selectedSection == section
      NavigationBarItem(
        selected = isSelected,
        onClick = { onSelectSection(section) },
        icon = {
          Icon(
            imageVector = if (isSelected) section.selectedIcon else section.unselectedIcon,
            contentDescription = section.title,
          )
        },
        label = {
          Text(
            text = section.shortTitle,
            style = MaterialTheme.typography.labelSmall,
            maxLines = 1,
          )
        },
        colors = NavigationBarItemDefaults.colors(
          selectedIconColor = CyanBright,
          selectedTextColor = CyanBright,
          indicatorColor = CyanMuted,
          unselectedIconColor = TextSecondary,
          unselectedTextColor = TextMuted,
        ),
      )
    }
    val isOtherSelected = selectedSection !in primaryTabs
    NavigationBarItem(
      selected = isOtherSelected,
      onClick = onOpenMoreSidebar,
      icon = {
        Icon(
          imageVector = Icons.Filled.Menu,
          contentDescription = "All 8 modules",
        )
      },
      label = {
        Text(
          text = "Modules",
          style = MaterialTheme.typography.labelSmall,
          maxLines = 1,
        )
      },
      colors = NavigationBarItemDefaults.colors(
        selectedIconColor = TealAccent,
        selectedTextColor = TealAccent,
        indicatorColor = CharcoalNavy,
        unselectedIconColor = TextSecondary,
        unselectedTextColor = TextMuted,
      ),
    )
  }
}
