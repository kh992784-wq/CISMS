package com.example.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Grade
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Wc
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
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
import com.example.ui.theme.SimTextSecondary
import com.example.ui.viewmodel.SimsViewModel

@Composable
fun EditStudentAdminScreen(
  viewModel: SimsViewModel,
  onNavigateBack: () -> Unit
) {
  val student by viewModel.selectedStudent.collectAsState()

  var fullName by remember(student) { mutableStateOf(student?.fullName ?: "") }
  var email by remember(student) { mutableStateOf(student?.email ?: "") }
  var mobile by remember(student) { mutableStateOf(student?.mobile ?: "") }
  var dob by remember(student) { mutableStateOf(student?.dateOfBirth ?: "") }
  var gender by remember(student) { mutableStateOf(student?.gender ?: "Male") }
  var course by remember(student) { mutableStateOf(student?.course ?: "B.Tech") }
  var branch by remember(student) { mutableStateOf(student?.branch ?: "Computer Science & Engineering") }
  var semester by remember(student) { mutableStateOf(student?.semester ?: "3rd Semester") }
  var tenthMarks by remember(student) { mutableStateOf(student?.tenthMarks ?: "85%") }
  var twelfthMarks by remember(student) { mutableStateOf(student?.twelfthMarks ?: "78%") }
  var cgpa by remember(student) { mutableStateOf(student?.cgpa ?: "8.56") }
  var grade by remember(student) { mutableStateOf(student?.grade ?: "A") }

  var errorMessage by remember { mutableStateOf<String?>(null) }
  var showDeleteDialog by remember { mutableStateOf(false) }

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
  val semesterOptions = listOf("1st Semester", "2nd Semester", "3rd Semester", "4th Semester", "5th Semester", "6th Semester", "7th Semester", "8th Semester")
  val gradeOptions = listOf("A+", "A", "B+", "B", "C", "D", "F")

  val scrollState = rememberScrollState()

  Scaffold(
    topBar = {
      SimsTopAppBar(
        title = "Edit Student",
        barTheme = BarTheme.GREEN,
        onNavigationClick = onNavigateBack
      )
    },
    modifier = Modifier.fillMaxSize().imePadding().testTag("edit_student_admin_screen")
  ) { innerPadding ->
    if (student == null) {
      Box(modifier = Modifier.fillMaxSize().padding(innerPadding), contentAlignment = Alignment.Center) {
        Text("No student loaded.")
      }
    } else {
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
        InputFieldLabel("Full Name")
        SimsInputField(
          value = fullName,
          onValueChange = { fullName = it; errorMessage = null },
          placeholder = "Full Name",
          leadingIcon = Icons.Default.Person,
          testTag = "admin_edit_name_input"
        )

        Spacer(modifier = Modifier.height(12.dp))

        InputFieldLabel("Email")
        SimsInputField(
          value = email,
          onValueChange = { email = it; errorMessage = null },
          placeholder = "Email Address",
          leadingIcon = Icons.Default.Email,
          keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
          testTag = "admin_edit_email_input"
        )

        Spacer(modifier = Modifier.height(12.dp))

        InputFieldLabel("Mobile")
        SimsInputField(
          value = mobile,
          onValueChange = { mobile = it; errorMessage = null },
          placeholder = "Mobile Number",
          leadingIcon = Icons.Default.Phone,
          keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
          testTag = "admin_edit_mobile_input"
        )

        Spacer(modifier = Modifier.height(12.dp))

        InputFieldLabel("Date of Birth")
        SimsInputField(
          value = dob,
          onValueChange = { dob = it; errorMessage = null },
          placeholder = "Date of Birth",
          leadingIcon = Icons.Default.DateRange,
          testTag = "admin_edit_dob_input"
        )

        Spacer(modifier = Modifier.height(12.dp))

        InputFieldLabel("Gender")
        SimsDropdownField(
          label = "Select Gender",
          selectedValue = gender,
          options = genderOptions,
          onOptionSelected = { gender = it },
          leadingIcon = Icons.Default.Wc,
          testTag = "admin_edit_gender_select"
        )

        Spacer(modifier = Modifier.height(12.dp))

        InputFieldLabel("Course")
        SimsDropdownField(
          label = "Select Course",
          selectedValue = course,
          options = courseOptions,
          onOptionSelected = { course = it },
          leadingIcon = Icons.Default.School,
          testTag = "admin_edit_course_select"
        )

        Spacer(modifier = Modifier.height(12.dp))

        InputFieldLabel("Branch")
        SimsDropdownField(
          label = "Select Branch",
          selectedValue = branch,
          options = branchOptions,
          onOptionSelected = { branch = it },
          leadingIcon = Icons.Default.School,
          testTag = "admin_edit_branch_select"
        )

        Spacer(modifier = Modifier.height(12.dp))

        InputFieldLabel("Semester")
        SimsDropdownField(
          label = "Select Semester",
          selectedValue = semester,
          options = semesterOptions,
          onOptionSelected = { semester = it },
          leadingIcon = Icons.Default.School,
          testTag = "admin_edit_semester_select"
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          Column(modifier = Modifier.weight(1f)) {
            InputFieldLabel("10th Marks")
            SimsInputField(
              value = tenthMarks,
              onValueChange = { tenthMarks = it },
              placeholder = "10th %",
              testTag = "admin_edit_10th_input"
            )
          }
          Column(modifier = Modifier.weight(1f)) {
            InputFieldLabel("12th Marks")
            SimsInputField(
              value = twelfthMarks,
              onValueChange = { twelfthMarks = it },
              placeholder = "12th %",
              testTag = "admin_edit_12th_input"
            )
          }
          Column(modifier = Modifier.weight(1f)) {
            InputFieldLabel("CGPA")
            SimsInputField(
              value = cgpa,
              onValueChange = { cgpa = it },
              placeholder = "CGPA",
              testTag = "admin_edit_cgpa_input"
            )
          }
        }

        Spacer(modifier = Modifier.height(12.dp))

        InputFieldLabel("Academic Grade")
        SimsDropdownField(
          label = "Select Grade",
          selectedValue = grade,
          options = gradeOptions,
          onOptionSelected = { grade = it },
          leadingIcon = Icons.Default.Grade,
          testTag = "admin_edit_grade_select"
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

        Spacer(modifier = Modifier.height(26.dp))

        // Bottom Action Buttons: Update (Green) & Delete (Red)
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
          Box(modifier = Modifier.weight(1f)) {
            SimsActionButton(
              text = "Update",
              onClick = {
                val current = student ?: return@SimsActionButton
                val updated = current.copy(
                  fullName = fullName.trim(),
                  email = email.trim(),
                  mobile = mobile.trim(),
                  dateOfBirth = dob.trim(),
                  gender = gender,
                  course = course,
                  branch = branch,
                  semester = semester,
                  grade = grade,
                  tenthMarks = tenthMarks.trim(),
                  twelfthMarks = twelfthMarks.trim(),
                  cgpa = cgpa.trim()
                )
                viewModel.adminUpdateStudent(
                  student = updated,
                  onSuccess = onNavigateBack,
                  onError = { errorMessage = it }
                )
              },
              backgroundColor = SimGreenPrimary,
              testTag = "admin_edit_save_button"
            )
          }

          Box(modifier = Modifier.weight(1f)) {
            SimsActionButton(
              text = "Delete",
              onClick = { showDeleteDialog = true },
              backgroundColor = SimRedPrimary,
              leadingIcon = Icons.Default.Delete,
              testTag = "admin_edit_delete_button"
            )
          }
        }
      }
    }

    if (showDeleteDialog && student != null) {
      AlertDialog(
        onDismissRequest = { showDeleteDialog = false },
        title = { Text("Confirm Deletion", fontWeight = FontWeight.Bold) },
        text = { Text("Delete ${student?.fullName} (${student?.studentId}) from records?") },
        confirmButton = {
          Button(
            onClick = {
              viewModel.adminDeleteStudent(student!!.id) {
                showDeleteDialog = false
                onNavigateBack()
              }
            },
            colors = ButtonDefaults.buttonColors(containerColor = SimRedPrimary)
          ) {
            Text("Delete")
          }
        },
        dismissButton = {
          OutlinedButton(onClick = { showDeleteDialog = false }) {
            Text("Cancel")
          }
        }
      )
    }
  }
}

@Composable
fun InputFieldLabel(text: String) {
  Text(
    text = text,
    fontSize = 12.sp,
    fontWeight = FontWeight.Medium,
    color = SimTextSecondary,
    modifier = Modifier.fillMaxWidth().padding(bottom = 4.dp)
  )
}
