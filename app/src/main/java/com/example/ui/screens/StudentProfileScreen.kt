package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.School
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
fun StudentProfileScreen(
  viewModel: SimsViewModel,
  onNavigateBack: () -> Unit,
  onNavigateToEditProfile: () -> Unit,
  onNavigateToAcademicDetails: (() -> Unit)? = null
) {
  // Reactive observation from Room Database via ViewModel StateFlow
  val student by viewModel.currentStudent.collectAsState()
  val scrollState = rememberScrollState()

  Scaffold(
    topBar = {
      SimsTopAppBar(
        title = "My Profile",
        barTheme = BarTheme.RED,
        onNavigationClick = onNavigateBack
      )
    },
    modifier = Modifier.fillMaxSize().testTag("student_profile_screen")
  ) { innerPadding ->
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
      // Profile Header Card matching reference image
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
          // Circular Red Avatar
          Box(
            modifier = Modifier
              .size(60.dp)
              .clip(CircleShape)
              .background(SimRedPrimary),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Default.Person,
              contentDescription = "Profile Avatar",
              tint = Color.White,
              modifier = Modifier.size(36.dp)
            )
          }

          Spacer(modifier = Modifier.width(16.dp))

          Column {
            Text(
              text = student?.fullName ?: "Himanshu",
              color = SimTextPrimary,
              fontSize = 18.sp,
              fontWeight = FontWeight.Bold
            )
            Text(
              text = "Student ID: ${student?.studentId ?: "2025ST001"}",
              color = SimTextSecondary,
              fontSize = 13.sp
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
              Box(
                modifier = Modifier
                  .clip(RoundedCornerShape(4.dp))
                  .background(SimGreenPrimary)
                  .padding(horizontal = 6.dp, vertical = 2.dp)
              ) {
                Text(
                  text = student?.course ?: "B.Tech",
                  color = Color.White,
                  fontSize = 11.sp,
                  fontWeight = FontWeight.SemiBold
                )
              }
              Spacer(modifier = Modifier.width(6.dp))
              Text(
                text = "Grade: ${student?.grade ?: "A"}",
                color = SimGreenPrimary,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
              )
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(18.dp))

      // Personal Details Table Card
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = androidx.compose.foundation.BorderStroke(1.dp, SimBorder),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
      ) {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
        ) {
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .padding(horizontal = 16.dp, vertical = 8.dp)
          ) {
            Text(
              text = "Personal Details",
              color = SimRedPrimary,
              fontSize = 14.sp,
              fontWeight = FontWeight.Bold
            )
          }
          HorizontalDivider(color = SimDivider, thickness = 1.dp)
          ProfileRowItem("Full Name", student?.fullName ?: "Himanshu")
          HorizontalDivider(color = SimDivider, thickness = 1.dp)
          ProfileRowItem("Father's Name", student?.fatherName ?: "Rajesh Kumar")
          HorizontalDivider(color = SimDivider, thickness = 1.dp)
          ProfileRowItem("Email", student?.email ?: "himanshu@gmail.com")
          HorizontalDivider(color = SimDivider, thickness = 1.dp)
          ProfileRowItem("Mobile", student?.mobile ?: "9876543210")
          HorizontalDivider(color = SimDivider, thickness = 1.dp)
          ProfileRowItem("Date of Birth", student?.dateOfBirth ?: "12-05-2005")
          HorizontalDivider(color = SimDivider, thickness = 1.dp)
          ProfileRowItem("Gender", student?.gender ?: "Male")
        }
      }

      Spacer(modifier = Modifier.height(18.dp))

      // Academic Overview Card allowing students to view their own academic details
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = androidx.compose.foundation.BorderStroke(1.dp, SimBorder),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
      ) {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
        ) {
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .padding(horizontal = 16.dp, vertical = 8.dp)
          ) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(
                text = "Academic Details",
                color = SimGreenPrimary,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
              )
              if (onNavigateToAcademicDetails != null) {
                Text(
                  text = "View Full >",
                  color = SimGreenPrimary,
                  fontSize = 12.sp,
                  fontWeight = FontWeight.SemiBold,
                  modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .padding(horizontal = 4.dp, vertical = 2.dp)
                    .testTag("profile_view_full_academic_link")
                )
              }
            }
          }
          HorizontalDivider(color = SimDivider, thickness = 1.dp)
          ProfileRowItem("Course", student?.course ?: "B.Tech")
          HorizontalDivider(color = SimDivider, thickness = 1.dp)
          ProfileRowItem("Branch", student?.branch ?: "Computer Science & Engineering")
          HorizontalDivider(color = SimDivider, thickness = 1.dp)
          ProfileRowItem("Semester", student?.semester ?: "3rd Semester")
          HorizontalDivider(color = SimDivider, thickness = 1.dp)
          ProfileRowItem("CGPA", student?.cgpa ?: "8.56")
          HorizontalDivider(color = SimDivider, thickness = 1.dp)
          ProfileRowItem("Grade", student?.grade ?: "A")
        }
      }

      Spacer(modifier = Modifier.height(24.dp))

      // Action Buttons: Edit Profile (Green) & View Full Academic Details
      SimsActionButton(
        text = "Edit Profile",
        onClick = onNavigateToEditProfile,
        backgroundColor = SimGreenPrimary,
        leadingIcon = Icons.Default.Edit,
        testTag = "profile_edit_button"
      )

      if (onNavigateToAcademicDetails != null) {
        Spacer(modifier = Modifier.height(12.dp))
        OutlinedButton(
          onClick = onNavigateToAcademicDetails,
          shape = RoundedCornerShape(10.dp),
          modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .testTag("profile_view_academic_button"),
          border = androidx.compose.foundation.BorderStroke(1.dp, SimGreenPrimary)
        ) {
          Icon(
            imageVector = Icons.Default.School,
            contentDescription = null,
            tint = SimGreenPrimary,
            modifier = Modifier.size(20.dp)
          )
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = "View Full Academic Details",
            color = SimGreenPrimary,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold
          )
        }
      }
    }
  }
}

@Composable
fun ProfileRowItem(label: String, value: String) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .padding(horizontal = 16.dp, vertical = 12.dp),
    verticalAlignment = Alignment.CenterVertically
  ) {
    Text(
      text = label,
      color = SimTextSecondary,
      fontSize = 13.sp,
      fontWeight = FontWeight.Medium,
      modifier = Modifier.weight(1.2f)
    )
    Text(
      text = value,
      color = SimTextPrimary,
      fontSize = 13.sp,
      fontWeight = FontWeight.SemiBold,
      modifier = Modifier.weight(1.8f)
    )
  }
}
