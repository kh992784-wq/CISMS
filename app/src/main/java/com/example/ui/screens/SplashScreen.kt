package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.example.ui.components.BottomWaveShape
import com.example.ui.components.SimsLogo
import com.example.ui.components.TopWaveShape
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
  onNavigateToLogin: () -> Unit
) {
  LaunchedEffect(Unit) {
    delay(2000)
    onNavigateToLogin()
  }

  Box(
    modifier = Modifier
      .fillMaxSize()
      .background(Color.White)
      .clickable { onNavigateToLogin() }
      .testTag("splash_screen")
  ) {
    // Top decorative wave
    TopWaveShape(
      modifier = Modifier.align(Alignment.TopCenter),
      height = 110.dp
    )

    // Center Branding
    Column(
      modifier = Modifier
        .fillMaxSize(),
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.Center
    ) {
      SimsLogo(
        iconSize = 130.dp,
        titleSize = 36,
        showTagline = true
      )
    }

    // Bottom decorative wave
    BottomWaveShape(
      modifier = Modifier
        .align(Alignment.BottomCenter)
        .navigationBarsPadding(),
      height = 110.dp
    )
  }
}
