package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.BarTheme
import com.example.ui.components.SimsTopAppBar
import com.example.ui.theme.SimBorder
import com.example.ui.theme.SimDivider
import com.example.ui.theme.SimGreenPrimary
import com.example.ui.theme.SimTextPrimary
import com.example.ui.theme.SimTextSecondary
import com.example.ui.viewmodel.SimsViewModel

@Composable
fun AcademicDetailsScreen(
  viewModel: SimsViewModel,
  onNavigateBack: () -> Unit
) {
  val student by viewModel.currentStudent.collectAsState()
  val scrollState = rememberScrollState()

  Scaffold(
    topBar = {
      SimsTopAppBar(
        title = "Academic Details",
        barTheme = BarTheme.RED,
        onNavigationClick = onNavigateBack
      )
    },
    modifier = Modifier.fillMaxSize().testTag("academic_details_screen")
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
      // Card 1: Course & Branch
      AcademicSectionCard(
        title = "Course & Branch",
        rows = listOf(
          "Course" to (student?.course ?: "B.Tech"),
          "Branch" to (student?.branch ?: "Computer Science & Engineering"),
          "Semester" to (student?.semester ?: "3rd Semester")
        )
      )

      Spacer(modifier = Modifier.height(18.dp))

      // Card 2: 10th & 12th Marks
      AcademicSectionCard(
        title = "10th & 12th Marks",
        rows = listOf(
          "10th Marks" to (student?.tenthMarks ?: "85%"),
          "12th Marks" to (student?.twelfthMarks ?: "78%")
        )
      )

      Spacer(modifier = Modifier.height(18.dp))

      // Card 3: Academic Performance (CGPA & Grade)
      AcademicSectionCard(
        title = "Last Year CGPA & Grade",
        rows = listOf(
          "CGPA" to (student?.cgpa ?: "8.56"),
          "Grade" to (student?.grade ?: "A")
        )
      )
    }
  }
}

@Composable
fun AcademicSectionCard(
  title: String,
  rows: List<Pair<String, String>>
) {
  Card(
    modifier = Modifier.fillMaxWidth(),
    shape = RoundedCornerShape(10.dp),
    colors = CardDefaults.cardColors(containerColor = Color.White),
    border = androidx.compose.foundation.BorderStroke(1.dp, SimBorder),
    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
  ) {
    Column(modifier = Modifier.fillMaxWidth()) {
      // Solid Green Header Bar matching reference image
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .background(SimGreenPrimary)
          .padding(horizontal = 16.dp, vertical = 10.dp)
      ) {
        Text(
          text = title,
          color = Color.White,
          fontSize = 14.sp,
          fontWeight = FontWeight.SemiBold
        )
      }

      Column(modifier = Modifier.padding(vertical = 4.dp)) {
        rows.forEachIndexed { index, (label, value) ->
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
          if (index < rows.size - 1) {
            HorizontalDivider(color = SimDivider, thickness = 1.dp)
          }
        }
      }
    }
  }
}
