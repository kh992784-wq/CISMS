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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Wc
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
import com.example.ui.theme.SimTextSecondary
import com.example.ui.viewmodel.SimsViewModel

@Composable
fun StudentRegistrationScreen(
  viewModel: SimsViewModel,
  onNavigateBack: () -> Unit,
  onRegistrationSuccess: () -> Unit
) {
  var fullName by remember { mutableStateOf("") }
  var fatherName by remember { mutableStateOf("") }
  var email by remember { mutableStateOf("") }
  var mobile by remember { mutableStateOf("") }
  var dob by remember { mutableStateOf("12-05-2005") }
  var gender by remember { mutableStateOf("") }
  var course by remember { mutableStateOf("") }
  var branch by remember { mutableStateOf("") }
  var password by remember { mutableStateOf("") }
  var errorMessage by remember { mutableStateOf<String?>(null) }

  val genderOptions = listOf("Male", "Female", "Other")
  val courseOptions = listOf("B.Tech", "BCA", "B.Sc", "M.Tech", "MBA")
  val branchOptions = listOf(
    "Computer Science & Engineering",
    "Mechanical Engineering",
    "Civil Engineering",
    "Electrical Engineering",
    "Information Technology",
    "Data Science",
    "Software Engineering",
    "Physics",
    "Mathematics"
  )

  val scrollState = rememberScrollState()

  Scaffold(
    topBar = {
      SimsTopAppBar(
        title = "Student Registration",
        barTheme = BarTheme.RED,
        onNavigationClick = onNavigateBack
      )
    },
    modifier = Modifier.fillMaxSize().imePadding()
  ) { innerPadding ->
    Column(
      modifier = Modifier
        .fillMaxSize()
        .background(Color.White)
        .padding(innerPadding)
        .verticalScroll(scrollState)
        .padding(horizontal = 24.dp, vertical = 16.dp)
        .navigationBarsPadding(),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      // Form fields matching reference design
      SimsInputField(
        value = fullName,
        onValueChange = { fullName = it; errorMessage = null },
        placeholder = "Full Name",
        leadingIcon = Icons.Default.Person,
        testTag = "reg_full_name_input"
      )

      Spacer(modifier = Modifier.height(12.dp))

      SimsInputField(
        value = fatherName,
        onValueChange = { fatherName = it; errorMessage = null },
        placeholder = "Father's Name",
        leadingIcon = Icons.Default.Badge,
        testTag = "reg_father_name_input"
      )

      Spacer(modifier = Modifier.height(12.dp))

      SimsInputField(
        value = email,
        onValueChange = { email = it; errorMessage = null },
        placeholder = "Email Address",
        leadingIcon = Icons.Default.Email,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
        testTag = "reg_email_input"
      )

      Spacer(modifier = Modifier.height(12.dp))

      SimsInputField(
        value = mobile,
        onValueChange = { mobile = it; errorMessage = null },
        placeholder = "Mobile Number",
        leadingIcon = Icons.Default.Phone,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
        testTag = "reg_mobile_input"
      )

      Spacer(modifier = Modifier.height(12.dp))

      SimsInputField(
        value = dob,
        onValueChange = { dob = it; errorMessage = null },
        placeholder = "Date of Birth (DD-MM-YYYY)",
        leadingIcon = Icons.Default.DateRange,
        testTag = "reg_dob_input"
      )

      Spacer(modifier = Modifier.height(12.dp))

      SimsDropdownField(
        label = "Select Gender",
        selectedValue = gender,
        options = genderOptions,
        onOptionSelected = { gender = it; errorMessage = null },
        leadingIcon = Icons.Default.Wc,
        testTag = "reg_gender_select"
      )

      Spacer(modifier = Modifier.height(12.dp))

      SimsDropdownField(
        label = "Select Course",
        selectedValue = course,
        options = courseOptions,
        onOptionSelected = { course = it; errorMessage = null },
        leadingIcon = Icons.Default.School,
        testTag = "reg_course_select"
      )

      Spacer(modifier = Modifier.height(12.dp))

      SimsDropdownField(
        label = "Select Branch",
        selectedValue = branch,
        options = branchOptions,
        onOptionSelected = { branch = it; errorMessage = null },
        leadingIcon = Icons.Default.School,
        testTag = "reg_branch_select"
      )

      Spacer(modifier = Modifier.height(12.dp))

      SimsInputField(
        value = password,
        onValueChange = { password = it; errorMessage = null },
        placeholder = "Create Password",
        leadingIcon = Icons.Default.Lock,
        isPassword = true,
        testTag = "reg_password_input"
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

      Spacer(modifier = Modifier.height(20.dp))

      // Register Button
      SimsActionButton(
        text = "Register",
        onClick = {
          viewModel.registerStudent(
            fullName = fullName,
            fatherName = fatherName,
            email = email,
            mobile = mobile,
            dob = dob,
            gender = gender.ifEmpty { "Male" },
            course = course.ifEmpty { "B.Tech" },
            branch = branch.ifEmpty { "Computer Science & Engineering" },
            password = password,
            onSuccess = onRegistrationSuccess,
            onError = { errorMessage = it }
          )
        },
        testTag = "reg_submit_button"
      )

      Spacer(modifier = Modifier.height(16.dp))

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
          color = SimGreenPrimary,
          fontSize = 13.sp,
          fontWeight = FontWeight.Bold,
          modifier = Modifier
            .clickable { onNavigateBack() }
            .testTag("reg_login_link")
        )
      }
    }
  }
}
