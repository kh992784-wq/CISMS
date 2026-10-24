package com.example.ui.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ui.screens.AcademicDetailsScreen
import com.example.ui.screens.AdminDashboardScreen
import com.example.ui.screens.AdminRegistrationScreen
import com.example.ui.screens.AllStudentsListScreen
import com.example.ui.screens.ChangePasswordScreen
import com.example.ui.screens.EditProfileScreen
import com.example.ui.screens.EditStudentAdminScreen
import com.example.ui.screens.LoginScreen
import com.example.ui.screens.SearchStudentScreen
import com.example.ui.screens.SplashScreen
import com.example.ui.screens.StudentDashboardScreen
import com.example.ui.screens.StudentDetailsAdminScreen
import com.example.ui.screens.StudentProfileScreen
import com.example.ui.screens.StudentRegistrationScreen
import com.example.ui.viewmodel.SimsViewModel

/**
 * Route definitions for the Student Information Management System Navigation Host.
 */
sealed class Screen(val route: String) {
  object Splash : Screen("splash")
  object Login : Screen("login")
  object StudentRegister : Screen("student_register")
  object AdminRegister : Screen("admin_register")
  object StudentDashboard : Screen("student_dashboard")
  object AdminDashboard : Screen("admin_dashboard")
  object StudentProfile : Screen("student_profile")
  object AcademicDetails : Screen("academic_details")
  object EditProfile : Screen("edit_profile")
  object ChangePassword : Screen("change_password")
  object AllStudents : Screen("all_students")
  object SearchStudent : Screen("search_student")
  object StudentDetailsAdmin : Screen("student_details_admin")
  object EditStudentAdmin : Screen("edit_student_admin")
}

/**
 * Route string constants for destination lookup.
 */
object SimsDestinations {
  const val SPLASH = "splash"
  const val LOGIN = "login"
  const val STUDENT_REGISTER = "student_register"
  const val ADMIN_REGISTER = "admin_register"
  const val STUDENT_DASHBOARD = "student_dashboard"
  const val ADMIN_DASHBOARD = "admin_dashboard"
  const val STUDENT_PROFILE = "student_profile"
  const val ACADEMIC_DETAILS = "academic_details"
  const val EDIT_PROFILE = "edit_profile"
  const val CHANGE_PASSWORD = "change_password"
  const val ALL_STUDENTS = "all_students"
  const val SEARCH_STUDENT = "search_student"
  const val STUDENT_DETAILS_ADMIN = "student_details_admin"
  const val EDIT_STUDENT_ADMIN = "edit_student_admin"
}

/**
 * Jetpack Compose Navigation Host defining routes and transitions between:
 * - Splash Screen
 * - Login Screen & Registration Screens (Student / Admin)
 * - Student Dashboard & Sub-screens (Profile, Academic Details, Edit Profile, Change Password)
 * - Admin Dashboard & Sub-screens (All Students List, Search Student, View/Edit Student)
 */
@Composable
fun AppNavHost(
  viewModel: SimsViewModel,
  modifier: Modifier = Modifier,
  navController: NavHostController = rememberNavController(),
  snackbarHostState: SnackbarHostState? = null
) {
  NavHost(
    navController = navController,
    startDestination = Screen.Splash.route,
    modifier = modifier.fillMaxSize(),
    enterTransition = {
      slideIntoContainer(
        AnimatedContentTransitionScope.SlideDirection.Left,
        animationSpec = tween(300)
      ) + fadeIn(animationSpec = tween(300))
    },
    exitTransition = {
      slideOutOfContainer(
        AnimatedContentTransitionScope.SlideDirection.Left,
        animationSpec = tween(300)
      ) + fadeOut(animationSpec = tween(300))
    },
    popEnterTransition = {
      slideIntoContainer(
        AnimatedContentTransitionScope.SlideDirection.Right,
        animationSpec = tween(300)
      ) + fadeIn(animationSpec = tween(300))
    },
    popExitTransition = {
      slideOutOfContainer(
        AnimatedContentTransitionScope.SlideDirection.Right,
        animationSpec = tween(300)
      ) + fadeOut(animationSpec = tween(300))
    }
  ) {
    // 1. Splash Screen
    composable(Screen.Splash.route) {
      SplashScreen(
        onNavigateToLogin = {
          navController.navigate(Screen.Login.route) {
            popUpTo(Screen.Splash.route) { inclusive = true }
          }
        }
      )
    }

    // 2. Login Screen (handles both Student and Admin credentials)
    composable(Screen.Login.route) {
      LoginScreen(
        viewModel = viewModel,
        onStudentLoginSuccess = {
          navController.navigate(Screen.StudentDashboard.route) {
            popUpTo(Screen.Login.route) { inclusive = true }
          }
        },
        onAdminLoginSuccess = {
          navController.navigate(Screen.AdminDashboard.route) {
            popUpTo(Screen.Login.route) { inclusive = true }
          }
        },
        onNavigateToStudentRegister = {
          navController.navigate(Screen.StudentRegister.route)
        },
        onNavigateToAdminRegister = {
          navController.navigate(Screen.AdminRegister.route)
        }
      )
    }

    // 3. Student Registration Screen
    composable(Screen.StudentRegister.route) {
      StudentRegistrationScreen(
        viewModel = viewModel,
        onNavigateBack = { navController.popBackStack() },
        onRegistrationSuccess = {
          navController.popBackStack()
        }
      )
    }

    // 4. Admin Registration Screen
    composable(Screen.AdminRegister.route) {
      AdminRegistrationScreen(
        viewModel = viewModel,
        onNavigateBack = { navController.popBackStack() },
        onRegistrationSuccess = {
          navController.popBackStack()
        }
      )
    }

    // 5. Student Dashboard Screen
    composable(Screen.StudentDashboard.route) {
      BackHandler {
        viewModel.logout()
        navController.navigate(Screen.Login.route) {
          popUpTo(Screen.StudentDashboard.route) { inclusive = true }
        }
      }

      StudentDashboardScreen(
        viewModel = viewModel,
        onNavigateToProfile = {
          navController.navigate(Screen.StudentProfile.route)
        },
        onNavigateToAcademic = {
          navController.navigate(Screen.AcademicDetails.route)
        },
        onNavigateToEditProfile = {
          navController.navigate(Screen.EditProfile.route)
        },
        onNavigateToChangePassword = {
          navController.navigate(Screen.ChangePassword.route)
        },
        onLogout = {
          viewModel.logout()
          navController.navigate(Screen.Login.route) {
            popUpTo(Screen.StudentDashboard.route) { inclusive = true }
          }
        }
      )
    }

    // 6. Student Profile Screen
    composable(Screen.StudentProfile.route) {
      StudentProfileScreen(
        viewModel = viewModel,
        onNavigateBack = { navController.popBackStack() },
        onNavigateToEditProfile = {
          navController.navigate(Screen.EditProfile.route)
        },
        onNavigateToAcademicDetails = {
          navController.navigate(Screen.AcademicDetails.route)
        }
      )
    }

    // 7. Academic Details Screen
    composable(Screen.AcademicDetails.route) {
      AcademicDetailsScreen(
        viewModel = viewModel,
        onNavigateBack = { navController.popBackStack() }
      )
    }

    // 8. Edit Profile Screen
    composable(Screen.EditProfile.route) {
      EditProfileScreen(
        viewModel = viewModel,
        onNavigateBack = { navController.popBackStack() }
      )
    }

    // 9. Change Password Screen
    composable(Screen.ChangePassword.route) {
      ChangePasswordScreen(
        viewModel = viewModel,
        onNavigateBack = { navController.popBackStack() }
      )
    }

    // 10. Admin Dashboard Screen
    composable(Screen.AdminDashboard.route) {
      BackHandler {
        viewModel.logout()
        navController.navigate(Screen.Login.route) {
          popUpTo(Screen.AdminDashboard.route) { inclusive = true }
        }
      }

      AdminDashboardScreen(
        viewModel = viewModel,
        onNavigateToManageStudents = {
          navController.navigate(Screen.AllStudents.route)
        },
        onNavigateToSearchStudent = {
          navController.navigate(Screen.SearchStudent.route)
        },
        onNavigateToAddStudent = {
          navController.navigate(Screen.StudentRegister.route)
        },
        onSelectStudentDetails = { student ->
          viewModel.selectStudent(student)
          navController.navigate(Screen.StudentDetailsAdmin.route)
        },
        onSelectStudentEdit = { student ->
          viewModel.selectStudent(student)
          navController.navigate(Screen.EditStudentAdmin.route)
        },
        onLogout = {
          viewModel.logout()
          navController.navigate(Screen.Login.route) {
            popUpTo(Screen.AdminDashboard.route) { inclusive = true }
          }
        }
      )
    }

    // 11. All Students List Screen
    composable(Screen.AllStudents.route) {
      AllStudentsListScreen(
        viewModel = viewModel,
        onNavigateBack = { navController.popBackStack() },
        onSelectStudentDetails = { student ->
          viewModel.selectStudent(student)
          navController.navigate(Screen.StudentDetailsAdmin.route)
        },
        onSelectStudentEdit = { student ->
          viewModel.selectStudent(student)
          navController.navigate(Screen.EditStudentAdmin.route)
        }
      )
    }

    // 12. Search Student Screen
    composable(Screen.SearchStudent.route) {
      SearchStudentScreen(
        viewModel = viewModel,
        onNavigateBack = { navController.popBackStack() },
        onSelectStudentDetails = { student ->
          viewModel.selectStudent(student)
          navController.navigate(Screen.StudentDetailsAdmin.route)
        }
      )
    }

    // 13. Student Details Screen (Admin View)
    composable(Screen.StudentDetailsAdmin.route) {
      StudentDetailsAdminScreen(
        viewModel = viewModel,
        onNavigateBack = { navController.popBackStack() },
        onNavigateToEdit = {
          navController.navigate(Screen.EditStudentAdmin.route)
        }
      )
    }

    // 14. Edit Student Screen (Admin View)
    composable(Screen.EditStudentAdmin.route) {
      EditStudentAdminScreen(
        viewModel = viewModel,
        onNavigateBack = { navController.popBackStack() }
      )
    }
  }
}

/**
 * Backward compatibility alias for SimsNavGraph.
 */
@Composable
fun SimsNavGraph(
  navController: NavHostController,
  viewModel: SimsViewModel,
  snackbarHostState: SnackbarHostState? = null,
  modifier: Modifier = Modifier
) {
  AppNavHost(
    viewModel = viewModel,
    modifier = modifier,
    navController = navController,
    snackbarHostState = snackbarHostState
  )
}
