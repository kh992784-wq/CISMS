package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.dao.StudentDao
import com.example.data.database.AppDatabase
import com.example.data.database.StudentDatabase
import com.example.data.model.Student
import kotlinx.coroutines.Dispatchers
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

/**
 * 'StudentViewModel' handles communication between the UI and the Room Database,
 * exposing reactive StateFlows for the list of students, search results, and
 * delivering complete CRUD operations for the Student Information Management System.
 */
class StudentViewModel(
  application: Application,
  private val studentDao: StudentDao = StudentDatabase.getInstance(
    application,
    CoroutineScopeHolder.scope
  ).studentDao()
) : AndroidViewModel(application) {

  // Reactive StateFlow exposing the complete list of students from Room Database
  val students: StateFlow<List<Student>> = studentDao.getAllStudentsFlow()
    .stateIn(
      scope = viewModelScope,
      started = SharingStarted.Eagerly,
      initialValue = emptyList()
    )

  private val _searchQuery = MutableStateFlow("")
  val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

  private val _selectedGradeFilter = MutableStateFlow<String?>(null)
  val selectedGradeFilter: StateFlow<String?> = _selectedGradeFilter.asStateFlow()

  // Filtered students StateFlow combining students list, search query, and grade filter
  val filteredStudents: StateFlow<List<Student>> = combine(
    students,
    _searchQuery,
    _selectedGradeFilter
  ) { list, query, grade ->
    var result = list

    if (grade != null && grade.isNotBlank()) {
      result = result.filter { it.grade.equals(grade, ignoreCase = true) }
    }

    if (query.isNotBlank()) {
      val trimmed = query.trim().lowercase(Locale.getDefault())
      result = result.filter {
        it.fullName.lowercase(Locale.getDefault()).contains(trimmed) ||
          it.studentId.lowercase(Locale.getDefault()).contains(trimmed) ||
          it.email.lowercase(Locale.getDefault()).contains(trimmed) ||
          it.course.lowercase(Locale.getDefault()).contains(trimmed) ||
          it.grade.lowercase(Locale.getDefault()).contains(trimmed)
      }
    }

    result
  }.stateIn(
    scope = viewModelScope,
    started = SharingStarted.WhileSubscribed(5000),
    initialValue = emptyList()
  )

  private val _selectedStudent = MutableStateFlow<Student?>(null)
  val selectedStudent: StateFlow<Student?> = _selectedStudent.asStateFlow()

  private val _isLoading = MutableStateFlow(false)
  val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

  private val _statusMessage = MutableStateFlow<String?>(null)
  val statusMessage: StateFlow<String?> = _statusMessage.asStateFlow()

  // ==================== CRUD OPERATIONS ====================

  /**
   * CREATE: Insert a new student into the Room database.
   */
  fun insert(student: Student, onComplete: ((Long) -> Unit)? = null) {
    viewModelScope.launch(Dispatchers.IO) {
      _isLoading.value = true
      try {
        val newId = studentDao.insertStudent(student)
        _statusMessage.value = "Student '${student.fullName}' added successfully."
        onComplete?.invoke(newId)
      } catch (e: Exception) {
        _statusMessage.value = "Failed to insert student: ${e.message}"
      } finally {
        _isLoading.value = false
      }
    }
  }

  /**
   * CREATE: Helper function to create and insert a student with primary attributes.
   */
  fun addStudent(
    fullName: String,
    email: String,
    studentId: String,
    grade: String = "A",
    course: String = "B.Tech",
    branch: String = "Computer Science & Engineering",
    mobile: String = "",
    onComplete: ((Long) -> Unit)? = null
  ) {
    val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
    val newStudent = Student(
      studentId = studentId,
      fullName = fullName,
      email = email,
      grade = grade,
      course = course,
      branch = branch,
      mobile = mobile,
      registeredDate = today
    )
    insert(newStudent, onComplete)
  }

  /**
   * READ: Select student by ID from the database into the selectedStudent StateFlow.
   */
  fun selectStudentById(id: Long) {
    viewModelScope.launch(Dispatchers.IO) {
      val student = studentDao.getStudentById(id)
      _selectedStudent.value = student
    }
  }

  /**
   * READ: Select student directly.
   */
  fun selectStudent(student: Student?) {
    _selectedStudent.value = student
  }

  /**
   * UPDATE: Update an existing student record in the Room database.
   */
  fun update(student: Student, onComplete: (() -> Unit)? = null) {
    viewModelScope.launch(Dispatchers.IO) {
      _isLoading.value = true
      try {
        studentDao.updateStudent(student)
        _selectedStudent.value = student
        _statusMessage.value = "Student '${student.fullName}' updated successfully."
        onComplete?.invoke()
      } catch (e: Exception) {
        _statusMessage.value = "Failed to update student: ${e.message}"
      } finally {
        _isLoading.value = false
      }
    }
  }

  /**
   * UPDATE: Update basic student attributes (name, email, grade).
   */
  fun updateStudentBasicData(
    id: Long,
    name: String,
    email: String,
    grade: String,
    onComplete: (() -> Unit)? = null
  ) {
    viewModelScope.launch(Dispatchers.IO) {
      _isLoading.value = true
      try {
        studentDao.updateBasicStudentData(id, name, email, grade)
        val updated = studentDao.getStudentById(id)
        _selectedStudent.value = updated
        _statusMessage.value = "Student data updated successfully."
        onComplete?.invoke()
      } catch (e: Exception) {
        _statusMessage.value = "Failed to update: ${e.message}"
      } finally {
        _isLoading.value = false
      }
    }
  }

  /**
   * DELETE: Delete a student record from the Room database.
   */
  fun delete(student: Student, onComplete: (() -> Unit)? = null) {
    viewModelScope.launch(Dispatchers.IO) {
      _isLoading.value = true
      try {
        studentDao.deleteStudent(student)
        if (_selectedStudent.value?.id == student.id) {
          _selectedStudent.value = null
        }
        _statusMessage.value = "Student '${student.fullName}' deleted."
        onComplete?.invoke()
      } catch (e: Exception) {
        _statusMessage.value = "Failed to delete student: ${e.message}"
      } finally {
        _isLoading.value = false
      }
    }
  }

  /**
   * DELETE: Delete a student by database ID.
   */
  fun deleteById(id: Long, onComplete: (() -> Unit)? = null) {
    viewModelScope.launch(Dispatchers.IO) {
      _isLoading.value = true
      try {
        studentDao.deleteStudentById(id)
        if (_selectedStudent.value?.id == id) {
          _selectedStudent.value = null
        }
        _statusMessage.value = "Student record deleted."
        onComplete?.invoke()
      } catch (e: Exception) {
        _statusMessage.value = "Failed to delete: ${e.message}"
      } finally {
        _isLoading.value = false
      }
    }
  }

  // ==================== SEARCH & FILTERS ====================

  fun setSearchQuery(query: String) {
    _searchQuery.value = query
  }

  fun setGradeFilter(grade: String?) {
    _selectedGradeFilter.value = grade
  }

  fun clearStatusMessage() {
    _statusMessage.value = null
  }

  // Internal helper for default constructor scope
  private object CoroutineScopeHolder {
    val scope = kotlinx.coroutines.CoroutineScope(Dispatchers.IO)
  }
}
