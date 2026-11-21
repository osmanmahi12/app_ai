package com.example

import android.graphics.Color as AndroidColor
import android.os.Build
import android.os.Bundle
import android.view.Display
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.model.NavSection
import com.example.ui.components.CompactBottomNavBar
import com.example.ui.components.DashboardTopBar
import com.example.ui.components.MediumNavRail
import com.example.ui.components.SidebarContent
import com.example.ui.screens.AiAssistantScreen
import com.example.ui.screens.AiPredictionsScreen
import com.example.ui.screens.DatasetManagementScreen
import com.example.ui.screens.OutbreakMapScreen
import com.example.ui.screens.OverviewScreen
import com.example.ui.screens.ReportsScreen
import com.example.ui.screens.ResourceAllocationScreen
import com.example.ui.screens.RiskAnalysisScreen
import com.example.ui.theme.DengueGuardTheme
import com.example.ui.theme.NavyDeep
import com.example.ui.theme.NavyVoid
import com.example.viewmodel.DengueDashboardUiState
import com.example.viewmodel.DengueDashboardViewModel
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    requestHighestDisplayMode()
    enableEdgeToEdge(
      statusBarStyle = SystemBarStyle.dark(AndroidColor.TRANSPARENT),
      navigationBarStyle = SystemBarStyle.dark(AndroidColor.TRANSPARENT),
    )
    setContent {
      DengueGuardTheme {
        DengueGuardApp()
      }
    }
  }

  private fun requestHighestDisplayMode() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      runCatching {
        val displayManager = getSystemService(android.hardware.display.DisplayManager::class.java)
        val activeDisplay = displayManager?.getDisplay(Display.DEFAULT_DISPLAY)
        val bestMode = activeDisplay?.supportedModes?.maxWithOrNull(
          compareBy<Display.Mode> { it.refreshRate }
            .thenBy { it.physicalWidth * it.physicalHeight }
        )
        if (bestMode != null) {
          window.attributes = window.attributes.apply {
            preferredDisplayModeId = bestMode.modeId
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
              preferredRefreshRate = bestMode.refreshRate
            }
          }
        }
      }
    }
  }
}

@Composable
fun DengueGuardApp(
  viewModel: DengueDashboardViewModel = viewModel(),
) {
  val state by viewModel.uiState.collectAsStateWithLifecycle()
  val snackbarHostState = remember { SnackbarHostState() }
  val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
  val scope = rememberCoroutineScope()

  LaunchedEffect(state.snackbarMessage) {
    val msg = state.snackbarMessage
    if (msg != null) {
      snackbarHostState.showSnackbar(msg)
      viewModel.clearSnackbar()
    }
  }

  BackHandler(enabled = state.selectedSection != NavSection.OVERVIEW) {
    viewModel.selectSection(NavSection.OVERVIEW)
  }

  BoxWithConstraints(
    modifier = Modifier
      .fillMaxSize()
      .background(NavyVoid)
  ) {
    val isExpanded = maxWidth >= 840.dp
    val isMedium = maxWidth in 600.dp..839.dp
    val isCompact = maxWidth < 600.dp

    if (isExpanded) {
      Row(modifier = Modifier.fillMaxSize()) {
        SidebarContent(
          selectedSection = state.selectedSection,
          onSelectSection = viewModel::selectSection,
          modifier = Modifier.width(248.dp),
        )
        Scaffold(
          modifier = Modifier.weight(1f).fillMaxHeight(),
          containerColor = NavyVoid,
          contentWindowInsets = WindowInsets.safeDrawing,
          snackbarHost = { SnackbarHost(snackbarHostState) },
        ) { innerPadding ->
          Column(
            modifier = Modifier
              .fillMaxSize()
              .padding(innerPadding),
          ) {
            DashboardTopBar(
              currentSection = state.selectedSection,
              selectedCity = state.selectedCity,
              showMenuButton = false,
              onMenuClick = {},
              onSelectCity = viewModel::selectCity,
            )
            DashboardMainContent(
              state = state,
              isWideLayout = true,
              viewModel = viewModel,
              modifier = Modifier.weight(1f),
            )
          }
        }
      }
    } else {
      ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
          ModalDrawerSheet(
            drawerContainerColor = NavyDeep,
            modifier = Modifier.width(272.dp),
          ) {
            SidebarContent(
              selectedSection = state.selectedSection,
              onSelectSection = { section ->
                viewModel.selectSection(section)
                scope.launch { drawerState.close() }
              },
            )
          }
        },
      ) {
        Scaffold(
          modifier = Modifier.fillMaxSize(),
          containerColor = NavyVoid,
          contentWindowInsets = WindowInsets.safeDrawing,
          snackbarHost = { SnackbarHost(snackbarHostState) },
          bottomBar = {
            if (isCompact) {
              CompactBottomNavBar(
                selectedSection = state.selectedSection,
                onSelectSection = viewModel::selectSection,
                onOpenMoreSidebar = { scope.launch { drawerState.open() } },
              )
            }
          },
        ) { innerPadding ->
          Row(
            modifier = Modifier
              .fillMaxSize()
              .padding(innerPadding),
          ) {
            if (isMedium) {
              MediumNavRail(
                selectedSection = state.selectedSection,
                onSelectSection = viewModel::selectSection,
                onOpenFullDrawer = { scope.launch { drawerState.open() } },
              )
            }
            Column(modifier = Modifier.weight(1f).fillMaxHeight()) {
              DashboardTopBar(
                currentSection = state.selectedSection,
                selectedCity = state.selectedCity,
                showMenuButton = isCompact,
                onMenuClick = { scope.launch { drawerState.open() } },
                onSelectCity = viewModel::selectCity,
                modifier = if (!isCompact) Modifier.statusBarsPadding() else Modifier,
              )
              DashboardMainContent(
                state = state,
                isWideLayout = isMedium,
                viewModel = viewModel,
                modifier = Modifier.weight(1f),
              )
            }
          }
        }
      }
    }
  }
}

@Composable
private fun DashboardMainContent(
  state: DengueDashboardUiState,
  isWideLayout: Boolean,
  viewModel: DengueDashboardViewModel,
  modifier: Modifier = Modifier,
) {
  Box(modifier = modifier.fillMaxSize()) {
    when (state.selectedSection) {
      NavSection.OVERVIEW -> OverviewScreen(
        state = state,
        isWideLayout = isWideLayout,
        onNavigateSection = viewModel::selectSection,
        onSelectCityFromBar = viewModel::selectCity,
        onInspectWard = { ward ->
          viewModel.selectWard(ward.id)
          viewModel.selectSection(NavSection.OUTBREAK_MAP)
        },
      )
      NavSection.OUTBREAK_MAP -> OutbreakMapScreen(
        state = state,
        isWideLayout = isWideLayout,
        onSelectLayer = viewModel::selectMapLayer,
        onSelectWard = viewModel::selectWard,
      )
      NavSection.AI_PREDICTIONS -> AiPredictionsScreen(
        state = state,
        onSelectHorizon = viewModel::setForecastHorizon,
      )
      NavSection.RISK_ANALYSIS -> RiskAnalysisScreen(
        state = state,
        onSelectRiskFilter = viewModel::selectRiskFilter,
        onSelectWard = { wardId ->
          viewModel.selectWard(wardId)
          viewModel.selectSection(NavSection.OUTBREAK_MAP)
        },
      )
      NavSection.RESOURCE_ALLOCATION -> ResourceAllocationScreen(
        state = state,
        onBoostChanged = viewModel::setFoggingBoost,
        onDispatchClick = viewModel::triggerDemoActionFeedback,
      )
      NavSection.AI_ASSISTANT -> AiAssistantScreen(
        state = state,
        onInputChanged = viewModel::updateChatInput,
        onSendMessage = viewModel::sendDemoChatMessage,
      )
      NavSection.REPORTS -> ReportsScreen(
        state = state,
        onPreviewReport = viewModel::openReportPreview,
        onClosePreview = viewModel::closeReportPreview,
        onExportReport = { report ->
          viewModel.triggerDemoActionFeedback("Demo export staged: ${report.title}")
        },
      )
      NavSection.DATASET_MANAGEMENT -> DatasetManagementScreen(
        state = state,
        onResetDemoWorkspace = viewModel::resetDemoWorkspace,
        onTestConnector = viewModel::triggerDemoActionFeedback,
      )
    }
  }
}
