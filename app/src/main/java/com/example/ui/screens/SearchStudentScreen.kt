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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
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
import com.example.data.model.StudentEntity
import com.example.ui.components.BarTheme
import com.example.ui.components.SimsActionButton
import com.example.ui.components.SimsInputField
import com.example.ui.components.SimsTopAppBar
import com.example.ui.theme.SimBorder
import com.example.ui.theme.SimGreenPrimary
import com.example.ui.theme.SimTextPrimary
import com.example.ui.theme.SimTextSecondary
import com.example.ui.viewmodel.SimsViewModel

@Composable
fun SearchStudentScreen(
  viewModel: SimsViewModel,
  onNavigateBack: () -> Unit,
  onSelectStudentDetails: (StudentEntity) -> Unit
) {
  var searchQuery by remember { mutableStateOf("") }
  val allStudents by viewModel.allStudents.collectAsState()

  val filteredStudents = remember(searchQuery, allStudents) {
    if (searchQuery.isBlank()) {
      emptyList()
    } else {
      val query = searchQuery.trim().lowercase()
      allStudents.filter {
        it.fullName.lowercase().contains(query) ||
          it.studentId.lowercase().contains(query) ||
          it.course.lowercase().contains(query) ||
          it.email.lowercase().contains(query)
      }
    }
  }

  Scaffold(
    topBar = {
      SimsTopAppBar(
        title = "Search Student",
        barTheme = BarTheme.GREEN,
        onNavigationClick = onNavigateBack
      )
    },
    modifier = Modifier.fillMaxSize().testTag("search_student_screen")
  ) { innerPadding ->
    Column(
      modifier = Modifier
        .fillMaxSize()
        .background(Color(0xFFF8FAFC))
        .padding(innerPadding)
        .padding(20.dp)
        .navigationBarsPadding(),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      // Search Bar Row with Green Search Button matching reference image
      Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
      ) {
        SimsInputField(
          value = searchQuery,
          onValueChange = { searchQuery = it },
          placeholder = "Search student by name or ID...",
          modifier = Modifier.weight(1f),
          testTag = "search_student_query_input"
        )

        Spacer(modifier = Modifier.width(8.dp))

        IconButton(
          onClick = { /* triggered via state */ },
          colors = IconButtonDefaults.iconButtonColors(
            containerColor = SimGreenPrimary,
            contentColor = Color.White
          ),
          modifier = Modifier
            .size(50.dp)
            .clip(RoundedCornerShape(10.dp))
            .testTag("search_student_button")
        ) {
          Icon(
            imageVector = Icons.Default.Search,
            contentDescription = "Search",
            modifier = Modifier.size(24.dp)
          )
        }
      }

      Spacer(modifier = Modifier.height(20.dp))

      if (searchQuery.isBlank()) {
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .weight(1f),
          contentAlignment = Alignment.Center
        ) {
          Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
              imageVector = Icons.Default.Search,
              contentDescription = null,
              tint = SimTextSecondary.copy(alpha = 0.5f),
              modifier = Modifier.size(54.dp)
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
              text = "Enter a student name or ID to search",
              color = SimTextSecondary,
              fontSize = 14.sp
            )
          }
        }
      } else if (filteredStudents.isEmpty()) {
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .weight(1f),
          contentAlignment = Alignment.Center
        ) {
          Text(
            text = "No students found matching \"$searchQuery\"",
            color = SimTextSecondary,
            fontSize = 14.sp
          )
        }
      } else {
        LazyColumn(
          modifier = Modifier
            .fillMaxWidth()
            .weight(1f),
          verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
          items(filteredStudents) { student ->
            SearchResultCard(
              student = student,
              onViewDetails = { onSelectStudentDetails(student) }
            )
          }
        }
      }
    }
  }
}

@Composable
fun SearchResultCard(
  student: StudentEntity,
  onViewDetails: () -> Unit
) {
  Card(
    modifier = Modifier.fillMaxWidth(),
    shape = RoundedCornerShape(12.dp),
    colors = CardDefaults.cardColors(containerColor = Color.White),
    border = androidx.compose.foundation.BorderStroke(1.dp, SimBorder),
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
      ) {
        // Green Avatar Circle
        Box(
          modifier = Modifier
            .size(54.dp)
            .clip(CircleShape)
            .background(SimGreenPrimary),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = Icons.Default.Person,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(34.dp)
          )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
          Text(
            text = student.fullName,
            color = SimTextPrimary,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
          )
          Text(
            text = "ID: ${student.studentId}",
            color = SimTextSecondary,
            fontSize = 12.sp
          )
          Text(
            text = "${student.course} | ${student.semester}",
            color = SimTextSecondary,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium
          )
        }
      }

      Spacer(modifier = Modifier.height(14.dp))

      SimsActionButton(
        text = "View Details",
        onClick = onViewDetails,
        backgroundColor = SimGreenPrimary,
        testTag = "search_view_details_${student.studentId}"
      )
    }
  }
}
