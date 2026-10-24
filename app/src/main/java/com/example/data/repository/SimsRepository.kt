package com.example.data.repository

import com.example.data.dao.AdminDao
import com.example.data.dao.StudentDao
import com.example.data.model.AdminEntity
import com.example.data.model.StudentEntity
import kotlinx.coroutines.flow.Flow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class SimsRepository(
  private val studentDao: StudentDao,
  private val adminDao: AdminDao
) {
  val allStudentsFlow: Flow<List<StudentEntity>> = studentDao.getAllStudentsFlow()
  val studentCountFlow: Flow<Int> = studentDao.getStudentCountFlow()
  val courseCountFlow: Flow<Int> = studentDao.getCourseCountFlow()
  val branchCountFlow: Flow<Int> = studentDao.getBranchCountFlow()

  fun getTodayRegisteredCountFlow(): Flow<Int> {
    val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
    return studentDao.getTodayRegisteredCountFlow(today)
  }

  fun searchStudentsFlow(query: String): Flow<List<StudentEntity>> {
    return studentDao.searchStudentsFlow(query)
  }

  suspend fun getStudentById(id: Long): StudentEntity? {
    return studentDao.getStudentById(id)
  }

  suspend fun getStudentByStudentId(studentId: String): StudentEntity? {
    return studentDao.getStudentByStudentId(studentId)
  }

  suspend fun authenticateStudent(usernameOrEmail: String, password: String): StudentEntity? {
    val student = studentDao.getStudentByEmailOrId(usernameOrEmail.trim())
    return if (student != null && student.password == password) student else null
  }

  suspend fun authenticateAdmin(username: String, password: String): AdminEntity? {
    val admin = adminDao.getAdminByUsername(username.trim())
    return if (admin != null && admin.password == password) admin else null
  }

  suspend fun registerStudent(student: StudentEntity): Result<Long> {
    val existing = studentDao.getStudentByEmailOrId(student.email)
    if (existing != null) {
      return Result.failure(Exception("A student with email ${student.email} already exists."))
    }
    val id = studentDao.insertStudent(student)
    return Result.success(id)
  }

  suspend fun registerAdmin(username: String, password: String): Result<Long> {
    val existing = adminDao.getAdminByUsername(username.trim())
    if (existing != null) {
      return Result.failure(Exception("Admin username '$username' is already taken."))
    }
    val id = adminDao.insertAdmin(AdminEntity(username = username.trim(), password = password))
    return Result.success(id)
  }

  suspend fun updateStudent(student: StudentEntity) {
    studentDao.updateStudent(student)
  }

  suspend fun deleteStudent(student: StudentEntity) {
    studentDao.deleteStudent(student)
  }

  suspend fun deleteStudentById(id: Long) {
    studentDao.deleteStudentById(id)
  }

  suspend fun generateNextStudentId(): String {
    val maxId = studentDao.getMaxId() ?: 0L
    val nextNum = maxId + 1
    return String.format(Locale.getDefault(), "2025ST%03d", nextNum)
  }
}
