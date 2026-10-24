package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.BarTheme
import com.example.ui.components.SimsActionButton
import com.example.ui.components.SimsInputField
import com.example.ui.components.SimsTopAppBar
import com.example.ui.theme.SimGreenPrimary
import com.example.ui.theme.SimRedPrimary
import com.example.ui.viewmodel.SimsViewModel

@Composable
fun ChangePasswordScreen(
  viewModel: SimsViewModel,
  onNavigateBack: () -> Unit
) {
  var currentPassword by remember { mutableStateOf("") }
  var newPassword by remember { mutableStateOf("") }
  var confirmPassword by remember { mutableStateOf("") }
  var errorMessage by remember { mutableStateOf<String?>(null) }

  val scrollState = rememberScrollState()

  Scaffold(
    topBar = {
      SimsTopAppBar(
        title = "Change Password",
        barTheme = BarTheme.RED,
        onNavigationClick = onNavigateBack
      )
    },
    modifier = Modifier.fillMaxSize().imePadding().testTag("change_password_screen")
  ) { innerPadding ->
    Column(
      modifier = Modifier
        .fillMaxSize()
        .background(Color.White)
        .padding(innerPadding)
        .verticalScroll(scrollState)
        .padding(horizontal = 24.dp, vertical = 24.dp)
        .navigationBarsPadding(),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Spacer(modifier = Modifier.height(10.dp))

      SimsInputField(
        value = currentPassword,
        onValueChange = { currentPassword = it; errorMessage = null },
        placeholder = "Current Password",
        leadingIcon = Icons.Default.Lock,
        isPassword = true,
        testTag = "change_pass_current_input"
      )

      Spacer(modifier = Modifier.height(16.dp))

      SimsInputField(
        value = newPassword,
        onValueChange = { newPassword = it; errorMessage = null },
        placeholder = "New Password",
        leadingIcon = Icons.Default.Lock,
        isPassword = true,
        testTag = "change_pass_new_input"
      )

      Spacer(modifier = Modifier.height(16.dp))

      SimsInputField(
        value = confirmPassword,
        onValueChange = { confirmPassword = it; errorMessage = null },
        placeholder = "Confirm Password",
        leadingIcon = Icons.Default.Lock,
        isPassword = true,
        testTag = "change_pass_confirm_input"
      )

      if (errorMessage != null) {
        Spacer(modifier = Modifier.height(14.dp))
        Text(
          text = errorMessage ?: "",
          color = SimRedPrimary,
          fontSize = 13.sp,
          fontWeight = FontWeight.Medium
        )
      }

      Spacer(modifier = Modifier.height(28.dp))

      // Change Password Button
      SimsActionButton(
        text = "Change Password",
        onClick = {
          viewModel.changeStudentPassword(
            currentPass = currentPassword,
            newPass = newPassword,
            confirmPass = confirmPassword,
            onSuccess = onNavigateBack,
            onError = { errorMessage = it }
          )
        },
        backgroundColor = SimGreenPrimary,
        testTag = "change_pass_submit_button"
      )
    }
  }
}
