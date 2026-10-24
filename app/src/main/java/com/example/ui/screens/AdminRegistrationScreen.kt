package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.BarTheme
import com.example.ui.components.BottomWaveShape
import com.example.ui.components.SimsActionButton
import com.example.ui.components.SimsInputField
import com.example.ui.components.SimsTopAppBar
import com.example.ui.theme.SimGreenPrimary
import com.example.ui.theme.SimRedPrimary
import com.example.ui.theme.SimTextPrimary
import com.example.ui.theme.SimTextSecondary
import com.example.ui.viewmodel.SimsViewModel

@Composable
fun AdminRegistrationScreen(
  viewModel: SimsViewModel,
  onNavigateBack: () -> Unit,
  onRegistrationSuccess: () -> Unit
) {
  var username by remember { mutableStateOf("") }
  var password by remember { mutableStateOf("") }
  var errorMessage by remember { mutableStateOf<String?>(null) }

  Scaffold(
    topBar = {
      SimsTopAppBar(
        title = "Admin Registration",
        barTheme = BarTheme.GREEN,
        onNavigationClick = onNavigateBack
      )
    },
    modifier = Modifier.fillMaxSize().imePadding()
  ) { innerPadding ->
    Box(
      modifier = Modifier
        .fillMaxSize()
        .background(Color.White)
        .padding(innerPadding)
    ) {
      Column(
        modifier = Modifier
          .fillMaxSize()
          .padding(horizontal = 24.dp, vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        Spacer(modifier = Modifier.height(20.dp))

        SimsInputField(
          value = username,
          onValueChange = { username = it; errorMessage = null },
          placeholder = "Username",
          leadingIcon = Icons.Default.Person,
          testTag = "admin_reg_username_input"
        )

        Spacer(modifier = Modifier.height(16.dp))

        SimsInputField(
          value = password,
          onValueChange = { password = it; errorMessage = null },
          placeholder = "Password",
          leadingIcon = Icons.Default.Lock,
          isPassword = true,
          testTag = "admin_reg_password_input"
        )

        if (errorMessage != null) {
          Spacer(modifier = Modifier.height(12.dp))
          Text(
            text = errorMessage ?: "",
            color = SimRedPrimary,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium
          )
        }

        Spacer(modifier = Modifier.height(24.dp))

        SimsActionButton(
          text = "Register",
          onClick = {
            viewModel.registerAdmin(
              username = username,
              password = password,
              onSuccess = onRegistrationSuccess,
              onError = { errorMessage = it }
            )
          },
          testTag = "admin_reg_submit_button"
        )

        Spacer(modifier = Modifier.height(18.dp))

        Row(
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = "Already have an account? ",
            color = SimTextSecondary,
            fontSize = 13.sp
          )
          Text(
            text = "Login",
            color = SimRedPrimary,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
              .clickable { onNavigateBack() }
              .testTag("admin_reg_login_link")
          )
        }

        Spacer(modifier = Modifier.height(50.dp))

        // Shield icon with "Admin Access Only" matching reference image
        Box(
          modifier = Modifier
            .size(70.dp)
            .clip(CircleShape)
            .background(Color(0xFFE8F5E9)),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = Icons.Default.AdminPanelSettings,
            contentDescription = "Admin Shield",
            tint = SimGreenPrimary,
            modifier = Modifier.size(42.dp)
          )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Text(
          text = "Admin Access\nOnly",
          color = SimTextPrimary,
          fontSize = 13.sp,
          fontWeight = FontWeight.SemiBold,
          lineHeight = 17.sp,
          textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
      }

      // Bottom wave
      BottomWaveShape(
        modifier = Modifier
          .align(Alignment.BottomCenter)
          .navigationBarsPadding(),
        height = 90.dp
      )
    }
  }
}
