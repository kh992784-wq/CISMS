package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.database.SimsDatabase
import com.example.data.model.AdminEntity
import com.example.data.model.StudentEntity
import com.example.data.repository.SimsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

enum class UserRole {
  GUEST,
  STUDENT,
  ADMIN
}

data class DashboardMetrics(
  val totalStudents: Int = 25,
  val todayRegistered: Int = 3,
  val coursesCount: Int = 5,
  val branchesCount: Int = 4
)

class SimsViewModel(application: Application) : AndroidViewModel(application) {
  private val repository: SimsRepository

  init {
    val database = SimsDatabase.getDatabase(application, viewModelScope)
    repository = SimsRepository(database.studentDao(), database.adminDao())
    // Pre-populate if not already done
    viewModelScope.launch {
      SimsDatabase.populateInitialData(database.adminDao(), database.studentDao())
    }
  }

  private val _currentUserRole = MutableStateFlow(UserRole.GUEST)
  val currentUserRole: StateFlow<UserRole> = _currentUserRole.asStateFlow()

  private val _currentStudent = MutableStateFlow<StudentEntity?>(null)
  val currentStudent: StateFlow<StudentEntity?> = _currentStudent.asStateFlow()

  private val _currentAdmin = MutableStateFlow<AdminEntity?>(null)
  val currentAdmin: StateFlow<AdminEntity?> = _currentAdmin.asStateFlow()

  val allStudents: StateFlow<List<StudentEntity>> = repository.allStudentsFlow
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  private val _searchQuery = MutableStateFlow("")
  val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

  val searchResults: StateFlow<List<StudentEntity>> = combine(
    allStudents,
    _searchQuery
  ) { list, query ->
    if (query.isBlank()) {
      list
    } else {
      val trimmed = query.trim().lowercase(Locale.getDefault())
      list.filter {
        it.fullName.lowercase(Locale.getDefault()).contains(trimmed) ||
          it.studentId.lowercase(Locale.getDefault()).contains(trimmed) ||
          it.course.lowercase(Locale.getDefault()).contains(trimmed) ||
          it.email.lowercase(Locale.getDefault()).contains(trimmed) ||
          it.branch.lowercase(Locale.getDefault()).contains(trimmed)
      }
    }
  }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  private val _selectedStudent = MutableStateFlow<StudentEntity?>(null)
  val selectedStudent: StateFlow<StudentEntity?> = _selectedStudent.asStateFlow()

  private val _snackbarMessage = MutableStateFlow<String?>(null)
  val snackbarMessage: StateFlow<String?> = _snackbarMessage.asStateFlow()

  val dashboardMetrics: StateFlow<DashboardMetrics> = combine(
    repository.studentCountFlow,
    repository.getTodayRegisteredCountFlow(),
    repository.courseCountFlow,
    repository.branchCountFlow
  ) { total, today, courses, branches ->
    DashboardMetrics(
      totalStudents = total,
      todayRegistered = today,
      coursesCount = if (courses > 0) courses else 5,
      branchesCount = if (branches > 0) branches else 4
    )
  }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), DashboardMetrics())

  fun clearSnackbarMessage() {
    _snackbarMessage.value = null
  }

  fun showMessage(msg: String) {
    _snackbarMessage.value = msg
  }

  fun setSearchQuery(query: String) {
    _searchQuery.value = query
  }

  fun selectStudent(student: StudentEntity) {
    _selectedStudent.value = student
  }

  fun selectStudentById(id: Long) {
    viewModelScope.launch {
      val student = repository.getStudentById(id)
      _selectedStudent.value = student
    }
  }

  fun loginStudent(
    usernameOrEmail: String,
    password: String,
    onSuccess: () -> Unit,
    onError: (String) -> Unit
  ) {
    if (usernameOrEmail.isBlank() || password.isBlank()) {
      onError("Please enter both username/email and password.")
      return
    }

    viewModelScope.launch {
      val student = repository.authenticateStudent(usernameOrEmail, password)
      if (student != null) {
        _currentStudent.value = student
        _currentUserRole.value = UserRole.STUDENT
        onSuccess()
      } else {
        onError("Invalid credentials. Please verify your Student ID/Email and password.")
      }
    }
  }

  fun loginAdmin(
    username: String,
    password: String,
    onSuccess: () -> Unit,
    onError: (String) -> Unit
  ) {
    if (username.isBlank() || password.isBlank()) {
      onError("Please enter both admin username and password.")
      return
    }

    viewModelScope.launch {
      val admin = repository.authenticateAdmin(username, password)
      if (admin != null) {
        _currentAdmin.value = admin
        _currentUserRole.value = UserRole.ADMIN
        onSuccess()
      } else {
        onError("Invalid admin credentials. (Default: username: admin / password: password123)")
      }
    }
  }

  fun registerStudent(
    fullName: String,
    fatherName: String,
    email: String,
    mobile: String,
    dob: String,
    gender: String,
    course: String,
    branch: String,
    password: String,
    onSuccess: () -> Unit,
    onError: (String) -> Unit
  ) {
    if (fullName.isBlank()) {
      onError("Full Name is required.")
      return
    }
    if (email.isBlank() || !email.contains("@")) {
      onError("A valid Email Address is required.")
      return
    }
    if (mobile.isBlank() || mobile.length < 7) {
      onError("A valid Mobile Number is required.")
      return
    }
    if (password.length < 4) {
      onError("Password must be at least 4 characters long.")
      return
    }

    viewModelScope.launch {
      val generatedId = repository.generateNextStudentId()
      val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

      val newStudent = StudentEntity(
        studentId = generatedId,
        fullName = fullName.trim(),
        fatherName = fatherName.trim().ifEmpty { "Not Specified" },
        email = email.trim(),
        mobile = mobile.trim(),
        dateOfBirth = dob.ifEmpty { "12-05-2005" },
        gender = gender.ifEmpty { "Male" },
        course = course.ifEmpty { "B.Tech" },
        branch = branch.ifEmpty { "Computer Science & Engineering" },
        semester = "1st Semester",
        tenthMarks = "80%",
        twelfthMarks = "75%",
        cgpa = "8.00",
        password = password,
        registeredDate = today
      )

      val result = repository.registerStudent(newStudent)
      if (result.isSuccess) {
        showMessage("Registration successful! Student ID: $generatedId")
        onSuccess()
      } else {
        onError(result.exceptionOrNull()?.message ?: "Registration failed.")
      }
    }
  }

  fun registerAdmin(
    username: String,
    password: String,
    onSuccess: () -> Unit,
    onError: (String) -> Unit
  ) {
    if (username.isBlank() || username.length < 3) {
      onError("Username must be at least 3 characters.")
      return
    }
    if (password.length < 4) {
      onError("Password must be at least 4 characters.")
      return
    }

    viewModelScope.launch {
      val result = repository.registerAdmin(username, password)
      if (result.isSuccess) {
        showMessage("Admin registered successfully! You can now log in.")
        onSuccess()
      } else {
        onError(result.exceptionOrNull()?.message ?: "Admin registration failed.")
      }
    }
  }

  fun updateCurrentStudentProfile(
    fullName: String,
    email: String,
    mobile: String,
    dob: String,
    gender: String,
    onSuccess: () -> Unit,
    onError: (String) -> Unit
  ) {
    val current = _currentStudent.value ?: run {
      onError("No active student session.")
      return
    }

    if (fullName.isBlank()) {
      onError("Full Name cannot be empty.")
      return
    }
    if (email.isBlank() || !email.contains("@")) {
      onError("Please provide a valid email.")
      return
    }

    val updated = current.copy(
      fullName = fullName.trim(),
      email = email.trim(),
      mobile = mobile.trim(),
      dateOfBirth = dob.trim(),
      gender = gender.trim()
    )

    viewModelScope.launch {
      repository.updateStudent(updated)
      _currentStudent.value = updated
      showMessage("Profile updated successfully!")
      onSuccess()
    }
  }

  fun changeStudentPassword(
    currentPass: String,
    newPass: String,
    confirmPass: String,
    onSuccess: () -> Unit,
    onError: (String) -> Unit
  ) {
    val current = _currentStudent.value ?: run {
      onError("No active student session.")
      return
    }

    if (current.password != currentPass) {
      onError("Current password does not match.")
      return
    }
    if (newPass.length < 4) {
      onError("New password must be at least 4 characters.")
      return
    }
    if (newPass != confirmPass) {
      onError("New password and confirm password do not match.")
      return
    }

    val updated = current.copy(password = newPass)
    viewModelScope.launch {
      repository.updateStudent(updated)
      _currentStudent.value = updated
      showMessage("Password changed successfully!")
      onSuccess()
    }
  }

  fun adminUpdateStudent(
    student: StudentEntity,
    onSuccess: () -> Unit,
    onError: (String) -> Unit
  ) {
    if (student.fullName.isBlank()) {
      onError("Name cannot be empty.")
      return
    }

    viewModelScope.launch {
      repository.updateStudent(student)
      _selectedStudent.value = student
      // Also update current student if editing self
      if (_currentStudent.value?.id == student.id) {
        _currentStudent.value = student
      }
      showMessage("Student record updated successfully!")
      onSuccess()
    }
  }

  fun adminDeleteStudent(
    studentId: Long,
    onSuccess: () -> Unit
  ) {
    viewModelScope.launch {
      repository.deleteStudentById(studentId)
      _selectedStudent.value = null
      showMessage("Student record deleted successfully.")
      onSuccess()
    }
  }

  fun logout() {
    _currentStudent.value = null
    _currentAdmin.value = null
    _currentUserRole.value = UserRole.GUEST
    _selectedStudent.value = null
    _searchQuery.value = ""
  }
}
