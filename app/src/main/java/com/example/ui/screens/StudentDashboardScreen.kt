package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
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
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.BarTheme
import com.example.ui.components.SimsActionButton
import com.example.ui.components.SimsTopAppBar
import com.example.ui.theme.SimBorder
import com.example.ui.theme.SimGreenPrimary
import com.example.ui.theme.SimRedPrimary
import com.example.ui.theme.SimTextPrimary
import com.example.ui.theme.SimTextSecondary
import com.example.ui.viewmodel.SimsViewModel

@Composable
fun StudentDashboardScreen(
  viewModel: SimsViewModel,
  onNavigateToProfile: () -> Unit,
  onNavigateToAcademic: () -> Unit,
  onNavigateToEditProfile: () -> Unit,
  onNavigateToChangePassword: () -> Unit,
  onLogout: () -> Unit
) {
  val student by viewModel.currentStudent.collectAsState()
  val scrollState = rememberScrollState()

  Scaffold(
    topBar = {
      SimsTopAppBar(
        title = "Student Dashboard",
        barTheme = BarTheme.RED,
        onNavigationClick = { /* Drawer/Menu */ },
        isNavigationMenu = true,
        actions = {
          IconButton(onClick = { viewModel.showMessage("No new notifications") }) {
            Icon(Icons.Default.Notifications, contentDescription = "Notifications", tint = Color.White)
          }
          IconButton(onClick = onNavigateToProfile) {
            Icon(Icons.Default.AccountCircle, contentDescription = "Profile", tint = Color.White)
          }
        }
      )
    },
    modifier = Modifier.fillMaxSize().testTag("student_dashboard_screen")
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
      // Profile Info Header Card
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .testTag("student_welcome_card"),
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
          // Circular Avatar with Green Border
          Box(
            modifier = Modifier
              .size(64.dp)
              .clip(CircleShape)
              .background(Color(0xFF0D6E42)),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Default.Person,
              contentDescription = "Student Avatar",
              tint = Color.White,
              modifier = Modifier.size(40.dp)
            )
          }

          Spacer(modifier = Modifier.width(16.dp))

          Column(
            modifier = Modifier.weight(1f)
          ) {
            Text(
              text = "Welcome,",
              color = SimTextSecondary,
              fontSize = 12.sp,
              fontWeight = FontWeight.Medium
            )
            Text(
              text = student?.fullName ?: "Himanshu",
              color = SimTextPrimary,
              fontSize = 17.sp,
              fontWeight = FontWeight.Bold
            )
            Text(
              text = "Student ID: ${student?.studentId ?: "2025ST001"}",
              color = SimTextSecondary,
              fontSize = 12.sp
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Course Pill Badge
            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(4.dp))
                .background(SimGreenPrimary)
                .padding(horizontal = 8.dp, vertical = 2.dp)
            ) {
              Text(
                text = student?.course ?: "B.Tech",
                color = Color.White,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold
              )
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(16.dp))

      // Quick Academic Summary Card
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .clickable { onNavigateToAcademic() }
          .testTag("student_academic_summary_card"),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = androidx.compose.foundation.BorderStroke(1.dp, SimBorder),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Column(modifier = Modifier.weight(1f)) {
            Text(
              text = "Academic Overview",
              fontSize = 11.sp,
              fontWeight = FontWeight.Medium,
              color = SimTextSecondary
            )
            Text(
              text = "${student?.branch ?: "Computer Science & Engineering"} (${student?.semester ?: "3rd Sem"})",
              fontSize = 12.sp,
              fontWeight = FontWeight.SemiBold,
              color = SimTextPrimary,
              maxLines = 1
            )
          }

          Spacer(modifier = Modifier.width(8.dp))

          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(6.dp))
              .background(Color(0xFFE8F5E9))
              .padding(horizontal = 8.dp, vertical = 4.dp)
          ) {
            Text(
              text = "CGPA: ${student?.cgpa ?: "8.56"}",
              color = SimGreenPrimary,
              fontSize = 12.sp,
              fontWeight = FontWeight.Bold
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(20.dp))

      // 2x2 Quick Action Grid
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
      ) {
        DashboardActionCard(
          icon = Icons.Default.Person,
          title = "My Profile",
          onClick = onNavigateToProfile,
          modifier = Modifier.weight(1f),
          testTag = "dashboard_card_my_profile"
        )
        DashboardActionCard(
          icon = Icons.Default.School,
          title = "Academic Details",
          onClick = onNavigateToAcademic,
          modifier = Modifier.weight(1f),
          testTag = "dashboard_card_academic_details"
        )
      }

      Spacer(modifier = Modifier.height(16.dp))

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
      ) {
        DashboardActionCard(
          icon = Icons.Default.Edit,
          title = "Edit Profile",
          onClick = onNavigateToEditProfile,
          modifier = Modifier.weight(1f),
          testTag = "dashboard_card_edit_profile"
        )
        DashboardActionCard(
          icon = Icons.Default.Lock,
          title = "Change Password",
          onClick = onNavigateToChangePassword,
          modifier = Modifier.weight(1f),
          testTag = "dashboard_card_change_password"
        )
      }

      Spacer(modifier = Modifier.height(32.dp))

      // Logout Button (Solid Red with Exit icon)
      SimsActionButton(
        text = "Logout",
        onClick = {
          viewModel.logout()
          onLogout()
        },
        backgroundColor = SimRedPrimary,
        leadingIcon = Icons.AutoMirrored.Filled.ExitToApp,
        testTag = "student_logout_button"
      )
    }
  }
}

@Composable
fun DashboardActionCard(
  icon: ImageVector,
  title: String,
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
  testTag: String = ""
) {
  Card(
    modifier = modifier
      .aspectRatio(1.15f)
      .clickable { onClick() }
      .testTag(testTag),
    shape = RoundedCornerShape(12.dp),
    colors = CardDefaults.cardColors(containerColor = Color.White),
    border = androidx.compose.foundation.BorderStroke(1.dp, SimBorder),
    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
  ) {
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(14.dp),
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.Center
    ) {
      Box(
        modifier = Modifier
          .size(46.dp)
          .clip(CircleShape)
          .background(Color(0xFFE8F5E9)),
        contentAlignment = Alignment.Center
      ) {
        Icon(
          imageVector = icon,
          contentDescription = title,
          tint = SimGreenPrimary,
          modifier = Modifier.size(26.dp)
        )
      }

      Spacer(modifier = Modifier.height(10.dp))

      Text(
        text = title,
        color = SimTextPrimary,
        fontSize = 13.sp,
        fontWeight = FontWeight.Medium,
        textAlign = androidx.compose.ui.text.style.TextAlign.Center
      )
    }
  }
}
