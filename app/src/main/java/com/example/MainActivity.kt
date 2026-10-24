package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.ui.navigation.SimsNavGraph
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.viewmodel.SimsViewModel

class MainActivity : ComponentActivity() {
  private val viewModel: SimsViewModel by viewModels()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      MyApplicationTheme {
        val navController = rememberNavController()
        val snackbarHostState = remember { SnackbarHostState() }
        val snackbarMessage by viewModel.snackbarMessage.collectAsState()

        LaunchedEffect(snackbarMessage) {
          snackbarMessage?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearSnackbarMessage()
          }
        }

        Scaffold(
          modifier = Modifier.fillMaxSize(),
          snackbarHost = { SnackbarHost(snackbarHostState) }
        ) { _ ->
          SimsNavGraph(
            navController = navController,
            viewModel = viewModel,
            snackbarHostState = snackbarHostState
          )
        }
      }
    }
  }
}
