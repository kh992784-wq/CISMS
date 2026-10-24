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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Hub
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Student
import com.example.ui.components.BarTheme
import com.example.ui.components.SimsInputField
import com.example.ui.components.SimsTopAppBar
import com.example.ui.theme.SimBorder
import com.example.ui.theme.SimDivider
import com.example.ui.theme.SimGreenPrimary
import com.example.ui.theme.SimRedPrimary
import com.example.ui.theme.SimStatBlue
import com.example.ui.theme.SimStatGreen
import com.example.ui.theme.SimStatOrange
import com.example.ui.theme.SimStatRed
import com.example.ui.theme.SimTextPrimary
import com.example.ui.theme.SimTextSecondary
import com.example.ui.viewmodel.SimsViewModel

@Composable
fun AdminDashboardScreen(
  viewModel: SimsViewModel,
  onNavigateToManageStudents: () -> Unit,
  onNavigateToSearchStudent: () -> Unit,
  onNavigateToAddStudent: (() -> Unit)? = null,
  onSelectStudentDetails: ((Student) -> Unit)? = null,
  onSelectStudentEdit: ((Student) -> Unit)? = null,
  onLogout: () -> Unit
) {
  val admin by viewModel.currentAdmin.collectAsState()
  val metrics by viewModel.dashboardMetrics.collectAsState()
  val allStudents by viewModel.allStudents.collectAsState()

  var dashboardSearchQuery by remember { mutableStateOf("") }
  var studentToDelete by remember { mutableStateOf<Student?>(null) }
  val scrollState = rememberScrollState()

  val displayedStudents = remember(allStudents, dashboardSearchQuery) {
    if (dashboardSearchQuery.isBlank()) {
      allStudents
    } else {
      val query = dashboardSearchQuery.trim().lowercase()
      allStudents.filter {
        it.fullName.lowercase().contains(query) ||
          it.studentId.lowercase().contains(query) ||
          it.course.lowercase().contains(query) ||
          it.email.lowercase().contains(query) ||
          it.grade.lowercase().contains(query)
      }
    }
  }

  Scaffold(
    topBar = {
      SimsTopAppBar(
        title = "Admin Dashboard",
        barTheme = BarTheme.GREEN,
        onNavigationClick = { /* Menu */ },
        isNavigationMenu = true,
        actions = {
          IconButton(onClick = { viewModel.showMessage("Admin notifications active") }) {
            Icon(Icons.Default.Notifications, contentDescription = "Notifications", tint = Color.White)
          }
          IconButton(onClick = { }) {
            Icon(Icons.Default.AccountCircle, contentDescription = "Admin Profile", tint = Color.White)
          }
        }
      )
    },
    floatingActionButton = {
      if (onNavigateToAddStudent != null) {
        FloatingActionButton(
          onClick = onNavigateToAddStudent,
          containerColor = SimGreenPrimary,
          contentColor = Color.White,
          modifier = Modifier.testTag("admin_fab_add_student")
        ) {
          Icon(
            imageVector = Icons.Default.PersonAdd,
            contentDescription = "Add Student",
            modifier = Modifier.size(24.dp)
          )
        }
      }
    },
    modifier = Modifier.fillMaxSize().testTag("admin_dashboard_screen")
  ) { innerPadding ->
    Column(
      modifier = Modifier
        .fillMaxSize()
        .background(Color(0xFFF8FAFC))
        .padding(innerPadding)
        .verticalScroll(scrollState)
        .padding(horizontal = 20.dp, vertical = 16.dp)
        .navigationBarsPadding(),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      // 1. Admin Header Card
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
              contentDescription = "Admin Avatar",
              tint = Color.White,
              modifier = Modifier.size(38.dp)
            )
          }

          Spacer(modifier = Modifier.width(16.dp))

          Column(modifier = Modifier.weight(1f)) {
            Text(
              text = "Welcome,",
              color = SimTextSecondary,
              fontSize = 12.sp,
              fontWeight = FontWeight.Medium
            )
            Text(
              text = admin?.fullName ?: "Admin",
              color = SimTextPrimary,
              fontSize = 18.sp,
              fontWeight = FontWeight.Bold
            )
            Text(
              text = "Administrator",
              color = SimTextSecondary,
              fontSize = 12.sp
            )
          }

          if (onNavigateToAddStudent != null) {
            Button(
              onClick = onNavigateToAddStudent,
              colors = ButtonDefaults.buttonColors(containerColor = SimGreenPrimary),
              shape = RoundedCornerShape(8.dp),
              modifier = Modifier.testTag("admin_add_student_top_button")
            ) {
              Icon(Icons.Default.PersonAdd, contentDescription = null, modifier = Modifier.size(16.dp))
              Spacer(modifier = Modifier.width(6.dp))
              Text("Add", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(18.dp))

      // 2. 4 Metric Badges in 2x2 Grid matching reference design
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(14.dp)
      ) {
        AdminStatBadge(
          title = "Total Students",
          count = metrics.totalStudents.toString(),
          icon = Icons.Default.Groups,
          backgroundColor = SimStatGreen,
          modifier = Modifier.weight(1f),
          testTag = "admin_stat_total_students"
        )
        AdminStatBadge(
          title = "Today Registered",
          count = metrics.todayRegistered.toString(),
          icon = Icons.Default.CalendarMonth,
          backgroundColor = SimStatRed,
          modifier = Modifier.weight(1f),
          testTag = "admin_stat_today_registered"
        )
      }

      Spacer(modifier = Modifier.height(14.dp))

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(14.dp)
      ) {
        AdminStatBadge(
          title = "Courses",
          count = metrics.coursesCount.toString(),
          icon = Icons.Default.School,
          backgroundColor = SimStatBlue,
          modifier = Modifier.weight(1f),
          testTag = "admin_stat_courses"
        )
        AdminStatBadge(
          title = "Branches",
          count = metrics.branchesCount.toString(),
          icon = Icons.Default.Hub,
          backgroundColor = SimStatOrange,
          modifier = Modifier.weight(1f),
          testTag = "admin_stat_branches"
        )
      }

      Spacer(modifier = Modifier.height(20.dp))

      // 3. Students List Section (View full list from Room Database)
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Text(
            text = "Students List",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = SimTextPrimary
          )
          Spacer(modifier = Modifier.width(8.dp))
          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(12.dp))
              .background(SimGreenPrimary.copy(alpha = 0.15f))
              .padding(horizontal = 8.dp, vertical = 2.dp)
          ) {
            Text(
              text = "${displayedStudents.size}",
              fontSize = 12.sp,
              fontWeight = FontWeight.Bold,
              color = SimGreenPrimary
            )
          }
        }

        Text(
          text = "Full Table View >",
          color = SimGreenPrimary,
          fontSize = 13.sp,
          fontWeight = FontWeight.SemiBold,
          modifier = Modifier
            .clickable { onNavigateToManageStudents() }
            .testTag("admin_view_full_table_link")
        )
      }

      Spacer(modifier = Modifier.height(10.dp))

      // Live Quick Search
      SimsInputField(
        value = dashboardSearchQuery,
        onValueChange = { dashboardSearchQuery = it },
        placeholder = "Filter students by name, ID, or course...",
        leadingIcon = Icons.Default.Search,
        testTag = "admin_dashboard_search_input"
      )

      Spacer(modifier = Modifier.height(12.dp))

      // Students Card List
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = androidx.compose.foundation.BorderStroke(1.dp, SimBorder),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
      ) {
        Column(modifier = Modifier.fillMaxWidth()) {
          if (displayedStudents.isEmpty()) {
            Box(
              modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
              contentAlignment = Alignment.Center
            ) {
              Text(
                text = "No students found.",
                color = SimTextSecondary,
                fontSize = 14.sp
              )
            }
          } else {
            displayedStudents.take(6).forEachIndexed { index, student ->
              Row(
                modifier = Modifier
                  .fillMaxWidth()
                  .clickable {
                    if (onSelectStudentDetails != null) {
                      onSelectStudentDetails(student)
                    } else {
                      onNavigateToManageStudents()
                    }
                  }
                  .padding(horizontal = 14.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically
              ) {
                // Circular Student Avatar
                Box(
                  modifier = Modifier
                    .size(38.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFE8F5E9)),
                  contentAlignment = Alignment.Center
                ) {
                  Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    tint = SimGreenPrimary,
                    modifier = Modifier.size(22.dp)
                  )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                  Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                      text = student.fullName,
                      color = SimTextPrimary,
                      fontSize = 14.sp,
                      fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Box(
                      modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(Color(0xFFF1F5F9))
                        .padding(horizontal = 5.dp, vertical = 1.dp)
                    ) {
                      Text(
                        text = "Grade: ${student.grade}",
                        color = SimGreenPrimary,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                      )
                    }
                  }
                  Text(
                    text = "${student.studentId} • ${student.course}",
                    color = SimTextSecondary,
                    fontSize = 12.sp
                  )
                }

                // Action Links: Edit & Delete
                Row(verticalAlignment = Alignment.CenterVertically) {
                  IconButton(
                    onClick = {
                      if (onSelectStudentEdit != null) {
                        onSelectStudentEdit(student)
                      } else {
                        onNavigateToManageStudents()
                      }
                    },
                    modifier = Modifier
                      .size(32.dp)
                      .testTag("admin_dash_edit_${student.studentId}")
                  ) {
                    Icon(
                      imageVector = Icons.Default.Edit,
                      contentDescription = "Edit Student",
                      tint = SimGreenPrimary,
                      modifier = Modifier.size(18.dp)
                    )
                  }

                  IconButton(
                    onClick = { studentToDelete = student },
                    modifier = Modifier
                      .size(32.dp)
                      .testTag("admin_dash_delete_${student.studentId}")
                  ) {
                    Icon(
                      imageVector = Icons.Default.Delete,
                      contentDescription = "Delete Student",
                      tint = SimRedPrimary,
                      modifier = Modifier.size(18.dp)
                    )
                  }
                }
              }

              if (index < displayedStudents.take(6).size - 1) {
                HorizontalDivider(color = SimDivider, thickness = 1.dp)
              }
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(20.dp))

      // 4. Navigation Menu List Card
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = androidx.compose.foundation.BorderStroke(1.dp, SimBorder),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
      ) {
        Column(modifier = Modifier.fillMaxWidth()) {
          AdminMenuItem(
            icon = Icons.Default.Groups,
            title = "Manage Students (Full Table)",
            onClick = onNavigateToManageStudents,
            testTag = "admin_menu_manage_students"
          )

          HorizontalDivider(color = SimDivider, thickness = 1.dp)

          AdminMenuItem(
            icon = Icons.Default.Search,
            title = "Search Student",
            onClick = onNavigateToSearchStudent,
            testTag = "admin_menu_search_student"
          )

          HorizontalDivider(color = SimDivider, thickness = 1.dp)

          // Logout Action in Menu List
          AdminMenuItem(
            icon = Icons.AutoMirrored.Filled.ExitToApp,
            title = "Logout",
            iconTint = SimRedPrimary,
            textColor = SimRedPrimary,
            onClick = {
              viewModel.logout()
              onLogout()
            },
            showChevron = false,
            testTag = "admin_menu_logout"
          )
        }
      }
    }

    // Delete Confirmation Dialog for Admin Dashboard
    if (studentToDelete != null) {
      AlertDialog(
        onDismissRequest = { studentToDelete = null },
        title = {
          Text(text = "Delete Student Record", fontWeight = FontWeight.Bold)
        },
        text = {
          Text(
            text = "Are you sure you want to delete ${studentToDelete?.fullName} (${studentToDelete?.studentId}) from the database? This cannot be undone."
          )
        },
        confirmButton = {
          Button(
            onClick = {
              studentToDelete?.let {
                viewModel.adminDeleteStudent(it.id) {}
              }
              studentToDelete = null
            },
            colors = ButtonDefaults.buttonColors(containerColor = SimRedPrimary)
          ) {
            Text("Delete")
          }
        },
        dismissButton = {
          OutlinedButton(onClick = { studentToDelete = null }) {
            Text("Cancel")
          }
        }
      )
    }
  }
}

@Composable
fun AdminStatBadge(
  title: String,
  count: String,
  icon: ImageVector,
  backgroundColor: Color,
  modifier: Modifier = Modifier,
  testTag: String = ""
) {
  Card(
    modifier = modifier.testTag(testTag),
    shape = RoundedCornerShape(10.dp),
    colors = CardDefaults.cardColors(containerColor = backgroundColor)
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 14.dp, vertical = 12.dp)
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = title,
          color = Color.White.copy(alpha = 0.95f),
          fontSize = 12.sp,
          fontWeight = FontWeight.Medium
        )
      }

      Spacer(modifier = Modifier.height(6.dp))

      Row(
        verticalAlignment = Alignment.CenterVertically
      ) {
        Icon(
          imageVector = icon,
          contentDescription = null,
          tint = Color.White,
          modifier = Modifier.size(22.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
          text = count,
          color = Color.White,
          fontSize = 20.sp,
          fontWeight = FontWeight.Bold
        )
      }
    }
  }
}

@Composable
fun AdminMenuItem(
  icon: ImageVector,
  title: String,
  onClick: () -> Unit,
  iconTint: Color = SimTextPrimary,
  textColor: Color = SimTextPrimary,
  showChevron: Boolean = true,
  testTag: String = ""
) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .clickable { onClick() }
      .padding(horizontal = 16.dp, vertical = 16.dp)
      .testTag(testTag),
    verticalAlignment = Alignment.CenterVertically
  ) {
    Icon(
      imageVector = icon,
      contentDescription = null,
      tint = iconTint,
      modifier = Modifier.size(20.dp)
    )

    Spacer(modifier = Modifier.width(14.dp))

    Text(
      text = title,
      color = textColor,
      fontSize = 14.sp,
      fontWeight = FontWeight.Medium,
      modifier = Modifier.weight(1f)
    )

    if (showChevron) {
      Icon(
        imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
        contentDescription = "Go",
        tint = SimTextSecondary,
        modifier = Modifier.size(14.dp)
      )
    }
  }
}
