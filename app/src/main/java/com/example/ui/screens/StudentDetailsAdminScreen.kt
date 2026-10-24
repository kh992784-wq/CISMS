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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.BarTheme
import com.example.ui.components.SimsActionButton
import com.example.ui.components.SimsTopAppBar
import com.example.ui.theme.SimBorder
import com.example.ui.theme.SimDivider
import com.example.ui.theme.SimGreenPrimary
import com.example.ui.theme.SimRedPrimary
import com.example.ui.theme.SimTextPrimary
import com.example.ui.theme.SimTextSecondary
import com.example.ui.viewmodel.SimsViewModel

@Composable
fun StudentDetailsAdminScreen(
  viewModel: SimsViewModel,
  onNavigateBack: () -> Unit,
  onNavigateToEdit: () -> Unit
) {
  val student by viewModel.selectedStudent.collectAsState()
  var showDeleteDialog by remember { mutableStateOf(false) }
  val scrollState = rememberScrollState()

  Scaffold(
    topBar = {
      SimsTopAppBar(
        title = "Student Details",
        barTheme = BarTheme.GREEN,
        onNavigationClick = onNavigateBack
      )
    },
    modifier = Modifier.fillMaxSize().testTag("student_details_admin_screen")
  ) { innerPadding ->
    if (student == null) {
      Box(
        modifier = Modifier
          .fillMaxSize()
          .padding(innerPadding),
        contentAlignment = Alignment.Center
      ) {
        Text("No student selected.", color = SimTextSecondary)
      }
    } else {
      val s = student!!
      Column(
        modifier = Modifier
          .fillMaxSize()
          .background(Color(0xFFF8FAFC))
          .padding(innerPadding)
          .verticalScroll(scrollState)
          .padding(20.dp)
          .navigationBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        // Profile Header Card
        Card(
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(12.dp),
          colors = CardDefaults.cardColors(containerColor = Color.White),
          elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Box(
              modifier = Modifier
                .size(60.dp)
                .clip(CircleShape)
                .background(SimGreenPrimary),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = Icons.Default.Person,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(38.dp)
              )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column {
              Text(
                text = s.fullName,
                color = SimTextPrimary,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
              )
              Text(
                text = s.studentId,
                color = SimTextSecondary,
                fontSize = 13.sp
              )
              Text(
                text = "${s.course} | ${s.semester}",
                color = SimTextSecondary,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Personal Information Card
        Card(
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(12.dp),
          colors = CardDefaults.cardColors(containerColor = Color.White),
          border = androidx.compose.foundation.BorderStroke(1.dp, SimBorder),
          elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
        ) {
          Column(modifier = Modifier.fillMaxWidth()) {
            Box(
              modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
              Text(
                text = "Personal Information",
                color = SimGreenPrimary,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
              )
            }
            HorizontalDivider(color = SimDivider, thickness = 1.dp)
            ProfileRowItem("Father's Name", s.fatherName)
            HorizontalDivider(color = SimDivider, thickness = 1.dp)
            ProfileRowItem("Email", s.email)
            HorizontalDivider(color = SimDivider, thickness = 1.dp)
            ProfileRowItem("Mobile", s.mobile)
            HorizontalDivider(color = SimDivider, thickness = 1.dp)
            ProfileRowItem("Date of Birth", s.dateOfBirth)
            HorizontalDivider(color = SimDivider, thickness = 1.dp)
            ProfileRowItem("Gender", s.gender)
          }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Academic Details Card
        Card(
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(12.dp),
          colors = CardDefaults.cardColors(containerColor = Color.White),
          border = androidx.compose.foundation.BorderStroke(1.dp, SimBorder),
          elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
        ) {
          Column(modifier = Modifier.fillMaxWidth()) {
            Box(
              modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
              Text(
                text = "Academic Details",
                color = SimGreenPrimary,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
              )
            }
            HorizontalDivider(color = SimDivider, thickness = 1.dp)
            ProfileRowItem("10th Marks", s.tenthMarks)
            HorizontalDivider(color = SimDivider, thickness = 1.dp)
            ProfileRowItem("12th Marks", s.twelfthMarks)
            HorizontalDivider(color = SimDivider, thickness = 1.dp)
            ProfileRowItem("CGPA", s.cgpa)
            HorizontalDivider(color = SimDivider, thickness = 1.dp)
            ProfileRowItem("Grade", s.grade)
          }
        }

        Spacer(modifier = Modifier.height(26.dp))

        // Action Buttons Row: Edit (Green) & Delete (Red)
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
          Box(modifier = Modifier.weight(1f)) {
            SimsActionButton(
              text = "Edit",
              onClick = onNavigateToEdit,
              backgroundColor = SimGreenPrimary,
              leadingIcon = Icons.Default.Edit,
              testTag = "admin_view_student_edit_button"
            )
          }

          Box(modifier = Modifier.weight(1f)) {
            SimsActionButton(
              text = "Delete",
              onClick = { showDeleteDialog = true },
              backgroundColor = SimRedPrimary,
              leadingIcon = Icons.Default.Delete,
              testTag = "admin_view_student_delete_button"
            )
          }
        }
      }
    }

    if (showDeleteDialog && student != null) {
      AlertDialog(
        onDismissRequest = { showDeleteDialog = false },
        title = { Text("Confirm Deletion", fontWeight = FontWeight.Bold) },
        text = { Text("Are you sure you want to delete ${student?.fullName}? This cannot be reversed.") },
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
