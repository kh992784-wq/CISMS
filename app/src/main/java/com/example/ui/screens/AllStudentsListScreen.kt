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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.StudentEntity
import com.example.ui.components.BarTheme
import com.example.ui.components.SimsInputField
import com.example.ui.components.SimsTopAppBar
import com.example.ui.theme.SimBorder
import com.example.ui.theme.SimDivider
import com.example.ui.theme.SimGreenPrimary
import com.example.ui.theme.SimRedPrimary
import com.example.ui.theme.SimTextPrimary
import com.example.ui.theme.SimTextSecondary
import com.example.ui.viewmodel.SimsViewModel

@Composable
fun AllStudentsListScreen(
  viewModel: SimsViewModel,
  onNavigateBack: () -> Unit,
  onSelectStudentDetails: (StudentEntity) -> Unit,
  onSelectStudentEdit: (StudentEntity) -> Unit
) {
  val searchQuery by viewModel.searchQuery.collectAsState()
  val studentsList by viewModel.searchResults.collectAsState()

  var studentToDelete by remember { mutableStateOf<StudentEntity?>(null) }
  var currentPage by remember { mutableIntStateOf(1) }
  val itemsPerPage = 6

  val totalPages = maxOf(1, (studentsList.size + itemsPerPage - 1) / itemsPerPage)
  val safeCurrentPage = currentPage.coerceIn(1, totalPages)
  val startIndex = (safeCurrentPage - 1) * itemsPerPage
  val pagedStudents = studentsList.drop(startIndex).take(itemsPerPage)

  Scaffold(
    topBar = {
      SimsTopAppBar(
        title = "All Students",
        barTheme = BarTheme.GREEN,
        onNavigationClick = onNavigateBack
      )
    },
    modifier = Modifier.fillMaxSize().testTag("all_students_screen")
  ) { innerPadding ->
    Column(
      modifier = Modifier
        .fillMaxSize()
        .background(Color(0xFFF8FAFC))
        .padding(innerPadding)
        .padding(horizontal = 16.dp, vertical = 14.dp)
        .navigationBarsPadding()
    ) {
      // Search Bar
      SimsInputField(
        value = searchQuery,
        onValueChange = {
          viewModel.setSearchQuery(it)
          currentPage = 1
        },
        placeholder = "Search by name or ID...",
        leadingIcon = Icons.Default.Search,
        testTag = "all_students_search_input"
      )

      Spacer(modifier = Modifier.height(14.dp))

      // Students Table Card
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .weight(1f),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = androidx.compose.foundation.BorderStroke(1.dp, SimBorder),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
      ) {
        Column(modifier = Modifier.fillMaxWidth()) {
          // Table Header
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .background(Color(0xFFF1F5F9))
              .padding(horizontal = 12.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = "ID",
              color = SimTextPrimary,
              fontSize = 12.sp,
              fontWeight = FontWeight.Bold,
              modifier = Modifier.weight(1.3f)
            )
            Text(
              text = "Name",
              color = SimTextPrimary,
              fontSize = 12.sp,
              fontWeight = FontWeight.Bold,
              modifier = Modifier.weight(1.3f)
            )
            Text(
              text = "Course",
              color = SimTextPrimary,
              fontSize = 12.sp,
              fontWeight = FontWeight.Bold,
              modifier = Modifier.weight(1.0f)
            )
            Text(
              text = "Actions",
              color = SimTextPrimary,
              fontSize = 12.sp,
              fontWeight = FontWeight.Bold,
              textAlign = TextAlign.End,
              modifier = Modifier.weight(1.1f)
            )
          }

          HorizontalDivider(color = SimBorder, thickness = 1.dp)

          if (pagedStudents.isEmpty()) {
            Box(
              modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
              contentAlignment = Alignment.Center
            ) {
              Text(
                text = "No students found.",
                color = SimTextSecondary,
                fontSize = 14.sp
              )
            }
          } else {
            LazyColumn(
              modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
            ) {
              itemsIndexed(pagedStudents) { index, student ->
                Row(
                  modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onSelectStudentDetails(student) }
                    .padding(horizontal = 12.dp, vertical = 10.dp),
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Text(
                    text = student.studentId,
                    color = SimTextPrimary,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.weight(1.3f)
                  )
                  Text(
                    text = student.fullName,
                    color = SimTextPrimary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.weight(1.3f)
                  )
                  Text(
                    text = student.course,
                    color = SimTextSecondary,
                    fontSize = 11.sp,
                    modifier = Modifier.weight(1.0f)
                  )

                  // Action Icons (Edit & Delete)
                  Row(
                    modifier = Modifier.weight(1.1f),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                  ) {
                    IconButton(
                      onClick = { onSelectStudentEdit(student) },
                      modifier = Modifier.size(28.dp).testTag("action_edit_${student.studentId}")
                    ) {
                      Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit student",
                        tint = SimGreenPrimary,
                        modifier = Modifier.size(17.dp)
                      )
                    }

                    Spacer(modifier = Modifier.width(4.dp))

                    IconButton(
                      onClick = { studentToDelete = student },
                      modifier = Modifier.size(28.dp).testTag("action_delete_${student.studentId}")
                    ) {
                      Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete student",
                        tint = SimRedPrimary,
                        modifier = Modifier.size(17.dp)
                      )
                    }
                  }
                }

                if (index < pagedStudents.size - 1) {
                  HorizontalDivider(color = SimDivider, thickness = 1.dp)
                }
              }
            }
          }

          HorizontalDivider(color = SimBorder, thickness = 1.dp)

          // Pagination Bar: < 1 2 3 >
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(vertical = 10.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
          ) {
            IconButton(
              onClick = { if (currentPage > 1) currentPage-- },
              enabled = currentPage > 1,
              modifier = Modifier.size(30.dp)
            ) {
              Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBackIos,
                contentDescription = "Previous page",
                tint = if (currentPage > 1) SimGreenPrimary else SimTextSecondary.copy(alpha = 0.4f),
                modifier = Modifier.size(12.dp)
              )
            }

            Spacer(modifier = Modifier.width(6.dp))

            for (p in 1..totalPages.coerceAtMost(5)) {
              Box(
                modifier = Modifier
                  .size(26.dp)
                  .clip(RoundedCornerShape(4.dp))
                  .background(if (p == safeCurrentPage) SimGreenPrimary else Color.Transparent)
                  .clickable { currentPage = p },
                contentAlignment = Alignment.Center
              ) {
                Text(
                  text = p.toString(),
                  color = if (p == safeCurrentPage) Color.White else SimTextPrimary,
                  fontSize = 12.sp,
                  fontWeight = if (p == safeCurrentPage) FontWeight.Bold else FontWeight.Normal
                )
              }
              Spacer(modifier = Modifier.width(4.dp))
            }

            Spacer(modifier = Modifier.width(6.dp))

            IconButton(
              onClick = { if (currentPage < totalPages) currentPage++ },
              enabled = currentPage < totalPages,
              modifier = Modifier.size(30.dp)
            ) {
              Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                contentDescription = "Next page",
                tint = if (currentPage < totalPages) SimGreenPrimary else SimTextSecondary.copy(alpha = 0.4f),
                modifier = Modifier.size(12.dp)
              )
            }
          }
        }
      }
    }

    // Delete Confirmation Dialog
    if (studentToDelete != null) {
      AlertDialog(
        onDismissRequest = { studentToDelete = null },
        title = {
          Text(text = "Delete Student", fontWeight = FontWeight.Bold)
        },
        text = {
          Text("Are you sure you want to delete ${studentToDelete?.fullName} (${studentToDelete?.studentId})? This action cannot be undone.")
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
