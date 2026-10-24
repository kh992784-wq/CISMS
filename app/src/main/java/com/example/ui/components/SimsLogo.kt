package com.example.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.SimGreenPrimary
import com.example.ui.theme.SimRedPrimary
import com.example.ui.theme.SimTextPrimary
import com.example.ui.theme.SimTextSecondary

@Composable
fun SimsLogo(
  modifier: Modifier = Modifier,
  iconSize: Dp = 100.dp,
  titleSize: Int = 32,
  showTagline: Boolean = false,
  showMotto: Boolean = false
) {
  Column(
    modifier = modifier,
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Center
  ) {
    Box(
      modifier = Modifier
        .size(iconSize)
        .clip(CircleShape),
      contentAlignment = Alignment.Center
    ) {
      Image(
        painter = painterResource(id = R.drawable.sims_logo),
        contentDescription = "SIMS Logo",
        modifier = Modifier.size(iconSize)
      )
    }

    Spacer(modifier = Modifier.height(10.dp))

    Text(
      text = "SIMS",
      color = SimGreenPrimary,
      fontSize = titleSize.sp,
      fontWeight = FontWeight.ExtraBold,
      letterSpacing = 1.5.sp
    )

    Spacer(modifier = Modifier.height(4.dp))

    Text(
      text = "Student Information\nManagement System",
      color = SimTextPrimary,
      fontSize = 13.sp,
      fontWeight = FontWeight.Medium,
      textAlign = TextAlign.Center,
      lineHeight = 17.sp
    )

    if (showTagline) {
      Spacer(modifier = Modifier.height(20.dp))
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        Text("Manage", fontSize = 13.sp, color = SimTextSecondary, fontWeight = FontWeight.Medium)
        Box(modifier = Modifier.size(5.dp).clip(CircleShape).background(SimGreenPrimary))
        Text("Track", fontSize = 13.sp, color = SimTextSecondary, fontWeight = FontWeight.Medium)
        Box(modifier = Modifier.size(5.dp).clip(CircleShape).background(SimRedPrimary))
        Text("Grow", fontSize = 13.sp, color = SimTextSecondary, fontWeight = FontWeight.Medium)
      }
    }

    if (showMotto) {
      Spacer(modifier = Modifier.height(12.dp))
      Text(
        text = "Better Education\nBrighter Future",
        color = SimTextSecondary,
        fontSize = 11.sp,
        fontWeight = FontWeight.Normal,
        textAlign = TextAlign.Center,
        lineHeight = 15.sp
      )
    }
  }
}
