package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.BottomWaveShape
import com.example.ui.components.SimsActionButton
import com.example.ui.components.SimsInputField
import com.example.ui.components.SimsLogo
import com.example.ui.components.TopWaveShape
import com.example.ui.theme.SimBorder
import com.example.ui.theme.SimGreenPrimary
import com.example.ui.theme.SimRedPrimary
import com.example.ui.theme.SimTextPrimary
import com.example.ui.theme.SimTextSecondary
import com.example.ui.viewmodel.SimsViewModel

@Composable
fun LoginScreen(
  viewModel: SimsViewModel,
  onStudentLoginSuccess: () -> Unit,
  onAdminLoginSuccess: () -> Unit,
  onNavigateToStudentRegister: () -> Unit,
  onNavigateToAdminRegister: () -> Unit
) {
  var isAdminTab by remember { mutableStateOf(false) }
  var usernameOrEmail by remember { mutableStateOf("") }
  var password by remember { mutableStateOf("") }
  var errorMessage by remember { mutableStateOf<String?>(null) }

  val scrollState = rememberScrollState()

  Box(
    modifier = Modifier
      .fillMaxSize()
      .background(Color.White)
      .imePadding()
      .testTag("login_screen")
  ) {
    // Top decorative wave
    TopWaveShape(
      modifier = Modifier.align(Alignment.TopCenter),
      height = 95.dp
    )

    Column(
      modifier = Modifier
        .fillMaxSize()
        .verticalScroll(scrollState)
        .padding(horizontal = 24.dp)
        .padding(top = 40.dp, bottom = 80.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      // SIMS Logo
      SimsLogo(
        iconSize = 85.dp,
        titleSize = 28,
        showTagline = false
      )

      Spacer(modifier = Modifier.height(24.dp))

      // Tab Toggle Pills: Student Login vs Admin Login
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(8.dp))
          .background(Color(0xFFF1F5F9))
          .padding(4.dp),
        horizontalArrangement = Arrangement.Center
      ) {
        // Student Login Tab
        Box(
          modifier = Modifier
            .weight(1f)
            .height(40.dp)
            .clip(RoundedCornerShape(6.dp))
            .background(if (!isAdminTab) SimGreenPrimary else Color.Transparent)
            .clickable {
              isAdminTab = false
              errorMessage = null
            }
            .testTag("tab_student_login"),
          contentAlignment = Alignment.Center
        ) {
          Text(
            text = "Student Login",
            color = if (!isAdminTab) Color.White else SimTextSecondary,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold
          )
        }

        Spacer(modifier = Modifier.width(4.dp))

        // Admin Login Tab
        Box(
          modifier = Modifier
            .weight(1f)
            .height(40.dp)
            .clip(RoundedCornerShape(6.dp))
            .background(if (isAdminTab) SimGreenPrimary else Color.Transparent)
            .clickable {
              isAdminTab = true
              errorMessage = null
            }
            .testTag("tab_admin_login"),
          contentAlignment = Alignment.Center
        ) {
          Text(
            text = "Admin Login",
            color = if (isAdminTab) Color.White else SimTextSecondary,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold
          )
        }
      }

      Spacer(modifier = Modifier.height(20.dp))

      // Form inputs
      SimsInputField(
        value = usernameOrEmail,
        onValueChange = {
          usernameOrEmail = it
          errorMessage = null
        },
        placeholder = if (isAdminTab) "Username" else "Username / Email",
        leadingIcon = Icons.Default.Person,
        testTag = "login_username_input"
      )

      Spacer(modifier = Modifier.height(14.dp))

      SimsInputField(
        value = password,
        onValueChange = {
          password = it
          errorMessage = null
        },
        placeholder = "Password",
        leadingIcon = Icons.Default.Lock,
        isPassword = true,
        testTag = "login_password_input"
      )

      if (errorMessage != null) {
        Spacer(modifier = Modifier.height(10.dp))
        Text(
          text = errorMessage ?: "",
          color = SimRedPrimary,
          fontSize = 13.sp,
          fontWeight = FontWeight.Medium
        )
      }

      Spacer(modifier = Modifier.height(20.dp))

      // Login Button
      SimsActionButton(
        text = "Login",
        onClick = {
          if (isAdminTab) {
            viewModel.loginAdmin(
              username = usernameOrEmail,
              password = password,
              onSuccess = onAdminLoginSuccess,
              onError = { errorMessage = it }
            )
          } else {
            viewModel.loginStudent(
              usernameOrEmail = usernameOrEmail,
              password = password,
              onSuccess = onStudentLoginSuccess,
              onError = { errorMessage = it }
            )
          }
        },
        testTag = "login_submit_button"
      )

      Spacer(modifier = Modifier.height(18.dp))

      // Registration Link
      Row(
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = "Don't have an account? ",
          color = SimTextSecondary,
          fontSize = 13.sp
        )
        Text(
          text = "Register",
          color = SimGreenPrimary,
          fontSize = 13.sp,
          fontWeight = FontWeight.Bold,
          modifier = Modifier
            .clickable {
              if (isAdminTab) {
                onNavigateToAdminRegister()
              } else {
                onNavigateToStudentRegister()
              }
            }
            .testTag("login_register_link")
        )
      }

      Spacer(modifier = Modifier.height(24.dp))

      // Motto
      Text(
        text = "Better Education\nBrighter Future",
        color = SimTextSecondary,
        fontSize = 11.sp,
        fontWeight = FontWeight.Normal,
        lineHeight = 15.sp
      )
    }

    // Bottom decorative wave
    BottomWaveShape(
      modifier = Modifier
        .align(Alignment.BottomCenter)
        .navigationBarsPadding(),
      height = 90.dp
    )
  }
}
