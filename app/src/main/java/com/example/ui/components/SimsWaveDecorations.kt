package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.ui.theme.SimGreenPrimary
import com.example.ui.theme.SimRedPrimary

@Composable
fun TopWaveShape(
  modifier: Modifier = Modifier,
  height: Dp = 90.dp,
  primaryColor: Color = SimRedPrimary,
  secondaryColor: Color = SimGreenPrimary
) {
  Canvas(
    modifier = modifier
      .fillMaxWidth()
      .height(height)
  ) {
    val w = size.width
    val h = size.height

    // Subtle green accent curve
    val greenPath = Path().apply {
      moveTo(0f, 0f)
      lineTo(w, 0f)
      lineTo(w, h * 0.45f)
      cubicTo(w * 0.7f, h * 0.75f, w * 0.3f, h * 0.2f, 0f, h * 0.6f)
      close()
    }
    drawPath(greenPath, color = secondaryColor)

    // Primary red curve
    val redPath = Path().apply {
      moveTo(0f, 0f)
      lineTo(w, 0f)
      lineTo(w, h * 0.35f)
      cubicTo(w * 0.65f, h * 0.6f, w * 0.35f, h * 0.1f, 0f, h * 0.5f)
      close()
    }
    drawPath(redPath, color = primaryColor)
  }
}

@Composable
fun BottomWaveShape(
  modifier: Modifier = Modifier,
  height: Dp = 100.dp,
  primaryColor: Color = SimRedPrimary,
  secondaryColor: Color = SimGreenPrimary
) {
  Canvas(
    modifier = modifier
      .fillMaxWidth()
      .height(height)
  ) {
    val w = size.width
    val h = size.height

    // Red bottom accent wave
    val redPath = Path().apply {
      moveTo(0f, h)
      lineTo(w, h)
      lineTo(w, h * 0.3f)
      cubicTo(w * 0.75f, h * 0.8f, w * 0.25f, h * 0.2f, 0f, h * 0.6f)
      close()
    }
    drawPath(redPath, color = primaryColor)

    // Green bottom accent curve
    val greenPath = Path().apply {
      moveTo(0f, h)
      lineTo(w, h)
      lineTo(w, h * 0.55f)
      cubicTo(w * 0.7f, h * 0.95f, w * 0.3f, h * 0.45f, 0f, h * 0.8f)
      close()
    }
    drawPath(greenPath, color = secondaryColor)
  }
}
