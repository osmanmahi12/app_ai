package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.R
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
fun AiAssistantScreen(
  state: DengueDashboardUiState,
  onInputChanged: (String) -> Unit,
  onSendMessage: (String?) -> Unit,
  modifier: Modifier = Modifier,
) {
  val quickPrompts = listOf(
    "Summarize critical wards",
    "Draft municipal fogging advisory",
    "Check hospital platelet readiness",
  )

  Column(
    modifier = modifier
      .fillMaxSize()
      .padding(horizontal = 16.dp),
  ) {
    Spacer(Modifier.height(10.dp))

    // Assistant Header Card
    DashboardCard(borderColor = CyanBright.copy(alpha = 0.35f)) {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
      ) {
        Image(
          painter = painterResource(id = R.drawable.ic_ai_emblem),
          contentDescription = "DengueGuard AI Assistant avatar",
          modifier = Modifier.size(44.dp),
          contentScale = ContentScale.Fit,
        )
        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
          ) {
            Text(
              text = "DengueGuard Copilot",
              style = MaterialTheme.typography.titleMedium,
              color = TextPrimary,
            )
            DemoDataBadge(label = "UI SHELL ONLY")
          }
          Text(
            text = "Stage 1 placeholder chat interface • Scope: ${state.selectedCity.displayName}",
            style = MaterialTheme.typography.bodySmall,
            color = TextSecondary,
          )
        }
      }
    }

    Spacer(Modifier.height(10.dp))

    // Quick Prompt Chips
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .horizontalScroll(rememberScrollState()),
      horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
      quickPrompts.forEachIndexed { idx, prompt ->
        Surface(
          shape = RoundedCornerShape(10.dp),
          color = CharcoalNavy,
          border = BorderStroke(1.dp, TealAccent.copy(alpha = 0.45f)),
          modifier = Modifier
            .minimumInteractiveComponentSize()
            .clip(RoundedCornerShape(10.dp))
            .clickable { onSendMessage(prompt) }
            .testTag("assistant_quick_prompt_$idx"),
        ) {
          Text(
            text = prompt,
            style = MaterialTheme.typography.labelLarge,
            color = TealAccent,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
          )
        }
      }
    }

    Spacer(Modifier.height(10.dp))

    // Message Thread
    LazyColumn(
      modifier = Modifier
        .weight(1f)
        .fillMaxWidth(),
      verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
      items(state.chatMessages, key = { it.id }) { msg ->
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = if (msg.isUser) Arrangement.End else Arrangement.Start,
        ) {
          Surface(
            modifier = Modifier.fillMaxWidth(0.88f),
            shape = RoundedCornerShape(14.dp),
            color = if (msg.isUser) CyanMuted else CharcoalCard,
            border = BorderStroke(
              1.dp,
              if (msg.isUser) CyanBright.copy(alpha = 0.5f) else SlateBorder,
            ),
          ) {
            Column(
              modifier = Modifier.padding(12.dp),
              verticalArrangement = Arrangement.spacedBy(6.dp),
            ) {
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
              ) {
                Text(
                  text = if (msg.isUser) "OPERATOR" else "DENGUEGUARD SHELL",
                  style = MaterialTheme.typography.labelSmall,
                  color = if (msg.isUser) CyanBright else TealAccent,
                )
                Text(
                  text = msg.timestampLabel,
                  style = MaterialTheme.typography.labelSmall,
                  color = TextMuted,
                )
              }
              Text(
                text = msg.content,
                style = MaterialTheme.typography.bodyMedium,
                color = TextPrimary,
              )
            }
          }
        }
      }
    }

    Spacer(Modifier.height(8.dp))

    // Input Bar
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(bottom = 12.dp),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
      OutlinedTextField(
        value = state.chatInput,
        onValueChange = onInputChanged,
        modifier = Modifier
          .weight(1f)
          .testTag("assistant_input_field"),
        placeholder = {
          Text(
            text = "Ask about ${state.selectedCity.displayName} demo status...",
            style = MaterialTheme.typography.bodyMedium,
            color = TextMuted,
          )
        },
        singleLine = true,
        shape = RoundedCornerShape(14.dp),
        colors = OutlinedTextFieldDefaults.colors(
          focusedBorderColor = CyanBright,
          unfocusedBorderColor = SlateBorder,
          focusedTextColor = TextPrimary,
          unfocusedTextColor = TextPrimary,
          focusedContainerColor = CharcoalNavy,
          unfocusedContainerColor = CharcoalNavy,
        ),
      )
      Surface(
        shape = RoundedCornerShape(14.dp),
        color = CyanMuted,
        border = BorderStroke(1.dp, CyanBright.copy(alpha = 0.55f)),
      ) {
        IconButton(
          onClick = { onSendMessage(null) },
          modifier = Modifier
            .minimumInteractiveComponentSize()
            .testTag("assistant_send_button"),
        ) {
          Icon(
            imageVector = Icons.AutoMirrored.Filled.Send,
            contentDescription = "Send demo prompt",
            tint = CyanBright,
          )
        }
      }
    }
  }
}
