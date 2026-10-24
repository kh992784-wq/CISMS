package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.SimGreenPrimary
import com.example.ui.theme.SimRedPrimary

enum class BarTheme {
  RED,
  GREEN
}

@Composable
fun SimsTopAppBar(
  title: String,
  barTheme: BarTheme = BarTheme.RED,
  onNavigationClick: (() -> Unit)? = null,
  isNavigationMenu: Boolean = false,
  actions: @Composable RowScope.() -> Unit = {}
) {
  val backgroundColor = if (barTheme == BarTheme.RED) SimRedPrimary else SimGreenPrimary

  Box(
    modifier = Modifier
      .fillMaxWidth()
      .background(backgroundColor)
      .statusBarsPadding()
      .height(56.dp)
      .padding(horizontal = 4.dp),
    contentAlignment = Alignment.CenterStart
  ) {
    Row(
      modifier = Modifier.fillMaxWidth(),
      verticalAlignment = Alignment.CenterVertically
    ) {
      if (onNavigationClick != null) {
        IconButton(
          onClick = onNavigationClick,
          modifier = Modifier.testTag("top_bar_nav_button")
        ) {
          Icon(
            imageVector = if (isNavigationMenu) Icons.Default.Menu else Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = if (isNavigationMenu) "Navigation Menu" else "Back",
            tint = Color.White
          )
        }
      } else {
        Spacer(modifier = Modifier.width(16.dp))
      }

      Text(
        text = title,
        color = Color.White,
        fontSize = 18.sp,
        fontWeight = FontWeight.SemiBold,
        modifier = Modifier.weight(1f)
      )

      actions()
    }
  }
}
