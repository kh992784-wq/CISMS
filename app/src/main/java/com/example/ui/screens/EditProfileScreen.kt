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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Wc
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.BarTheme
import com.example.ui.components.SimsActionButton
import com.example.ui.components.SimsDropdownField
import com.example.ui.components.SimsInputField
import com.example.ui.components.SimsTopAppBar
import com.example.ui.theme.SimGreenPrimary
import com.example.ui.theme.SimRedPrimary
import com.example.ui.viewmodel.SimsViewModel

@Composable
fun EditProfileScreen(
  viewModel: SimsViewModel,
  onNavigateBack: () -> Unit
) {
  val student by viewModel.currentStudent.collectAsState()

  var fullName by remember(student) { mutableStateOf(student?.fullName ?: "") }
  var email by remember(student) { mutableStateOf(student?.email ?: "") }
  var mobile by remember(student) { mutableStateOf(student?.mobile ?: "") }
  var dob by remember(student) { mutableStateOf(student?.dateOfBirth ?: "") }
  var gender by remember(student) { mutableStateOf(student?.gender ?: "Male") }
  var errorMessage by remember { mutableStateOf<String?>(null) }

  val genderOptions = listOf("Male", "Female", "Other")
  val scrollState = rememberScrollState()

  Scaffold(
    topBar = {
      SimsTopAppBar(
        title = "Edit Profile",
        barTheme = BarTheme.RED,
        onNavigationClick = onNavigateBack
      )
    },
    modifier = Modifier.fillMaxSize().imePadding().testTag("edit_profile_screen")
  ) { innerPadding ->
    Column(
      modifier = Modifier
        .fillMaxSize()
        .background(Color.White)
        .padding(innerPadding)
        .verticalScroll(scrollState)
        .padding(horizontal = 24.dp, vertical = 20.dp)
        .navigationBarsPadding(),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Text(
        text = "Full Name",
        fontSize = 12.sp,
        fontWeight = FontWeight.Medium,
        color = Color(0xFF64748B),
        modifier = Modifier.fillMaxWidth().padding(bottom = 4.dp)
      )
      SimsInputField(
        value = fullName,
        onValueChange = { fullName = it; errorMessage = null },
        placeholder = "Full Name",
        leadingIcon = Icons.Default.Person,
        testTag = "edit_profile_name_input"
      )

      Spacer(modifier = Modifier.height(14.dp))

      Text(
        text = "Email",
        fontSize = 12.sp,
        fontWeight = FontWeight.Medium,
        color = Color(0xFF64748B),
        modifier = Modifier.fillMaxWidth().padding(bottom = 4.dp)
      )
      SimsInputField(
        value = email,
        onValueChange = { email = it; errorMessage = null },
        placeholder = "Email Address",
        leadingIcon = Icons.Default.Email,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
        testTag = "edit_profile_email_input"
      )

      Spacer(modifier = Modifier.height(14.dp))

      Text(
        text = "Mobile",
        fontSize = 12.sp,
        fontWeight = FontWeight.Medium,
        color = Color(0xFF64748B),
        modifier = Modifier.fillMaxWidth().padding(bottom = 4.dp)
      )
      SimsInputField(
        value = mobile,
        onValueChange = { mobile = it; errorMessage = null },
        placeholder = "Mobile Number",
        leadingIcon = Icons.Default.Phone,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
        testTag = "edit_profile_mobile_input"
      )

      Spacer(modifier = Modifier.height(14.dp))

      Text(
        text = "Date of Birth",
        fontSize = 12.sp,
        fontWeight = FontWeight.Medium,
        color = Color(0xFF64748B),
        modifier = Modifier.fillMaxWidth().padding(bottom = 4.dp)
      )
      SimsInputField(
        value = dob,
        onValueChange = { dob = it; errorMessage = null },
        placeholder = "Date of Birth",
        leadingIcon = Icons.Default.DateRange,
        testTag = "edit_profile_dob_input"
      )

      Spacer(modifier = Modifier.height(14.dp))

      Text(
        text = "Gender",
        fontSize = 12.sp,
        fontWeight = FontWeight.Medium,
        color = Color(0xFF64748B),
        modifier = Modifier.fillMaxWidth().padding(bottom = 4.dp)
      )
      SimsDropdownField(
        label = "Select Gender",
        selectedValue = gender,
        options = genderOptions,
        onOptionSelected = { gender = it; errorMessage = null },
        leadingIcon = Icons.Default.Wc,
        testTag = "edit_profile_gender_select"
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

      Spacer(modifier = Modifier.height(28.dp))

      // Update Button
      SimsActionButton(
        text = "Update",
        onClick = {
          viewModel.updateCurrentStudentProfile(
            fullName = fullName,
            email = email,
            mobile = mobile,
            dob = dob,
            gender = gender,
            onSuccess = onNavigateBack,
            onError = { errorMessage = it }
          )
        },
        backgroundColor = SimGreenPrimary,
        testTag = "edit_profile_update_button"
      )
    }
  }
}
