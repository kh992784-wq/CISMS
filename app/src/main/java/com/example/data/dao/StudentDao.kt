package com.example.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.Student
import kotlinx.coroutines.flow.Flow

/**
 * 'StudentDao' interface providing comprehensive CRUD operations for the Student Information Management System.
 */
@Dao
interface StudentDao {

  // ==================== CREATE ====================

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertStudent(student: Student): Long

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertAll(students: List<Student>)

  // ==================== READ ====================

  @Query("SELECT * FROM students ORDER BY studentId ASC")
  fun getAllStudentsFlow(): Flow<List<Student>>

  @Query("SELECT * FROM students ORDER BY studentId ASC")
  suspend fun getAllStudents(): List<Student>

  @Query("SELECT * FROM students WHERE id = :id LIMIT 1")
  suspend fun getStudentById(id: Long): Student?

  @Query("SELECT * FROM students WHERE studentId = :studentId LIMIT 1")
  suspend fun getStudentByStudentId(studentId: String): Student?

  @Query("SELECT * FROM students WHERE LOWER(email) = LOWER(:email) LIMIT 1")
  suspend fun getStudentByEmail(email: String): Student?

  @Query("SELECT * FROM students WHERE LOWER(email) = LOWER(:email) OR LOWER(studentId) = LOWER(:email) LIMIT 1")
  suspend fun getStudentByEmailOrId(email: String): Student?

  @Query("SELECT * FROM students WHERE grade = :grade ORDER BY studentId ASC")
  fun getStudentsByGradeFlow(grade: String): Flow<List<Student>>

  @Query("""
    SELECT * FROM students 
    WHERE LOWER(fullName) LIKE '%' || LOWER(:query) || '%' 
       OR LOWER(studentId) LIKE '%' || LOWER(:query) || '%'
       OR LOWER(course) LIKE '%' || LOWER(:query) || '%'
       OR LOWER(email) LIKE '%' || LOWER(:query) || '%'
       OR LOWER(grade) LIKE '%' || LOWER(:query) || '%'
    ORDER BY studentId ASC
  """)
  fun searchStudentsFlow(query: String): Flow<List<Student>>

  @Query("""
    SELECT * FROM students 
    WHERE LOWER(fullName) LIKE '%' || LOWER(:query) || '%' 
       OR LOWER(studentId) LIKE '%' || LOWER(:query) || '%'
       OR LOWER(course) LIKE '%' || LOWER(:query) || '%'
       OR LOWER(email) LIKE '%' || LOWER(:query) || '%'
       OR LOWER(grade) LIKE '%' || LOWER(:query) || '%'
    ORDER BY studentId ASC
  """)
  suspend fun searchStudents(query: String): List<Student>

  @Query("SELECT COUNT(*) FROM students")
  fun getStudentCountFlow(): Flow<Int>

  @Query("SELECT COUNT(*) FROM students WHERE registeredDate = :todayDate")
  fun getTodayRegisteredCountFlow(todayDate: String): Flow<Int>

  @Query("SELECT COUNT(DISTINCT course) FROM students")
  fun getCourseCountFlow(): Flow<Int>

  @Query("SELECT COUNT(DISTINCT branch) FROM students")
  fun getBranchCountFlow(): Flow<Int>

  @Query("SELECT MAX(id) FROM students")
  suspend fun getMaxId(): Long?

  // ==================== UPDATE ====================

  @Update
  suspend fun updateStudent(student: Student)

  @Query("UPDATE students SET fullName = :name, email = :email, grade = :grade WHERE id = :id")
  suspend fun updateBasicStudentData(id: Long, name: String, email: String, grade: String)

  // ==================== DELETE ====================

  @Delete
  suspend fun deleteStudent(student: Student)

  @Query("DELETE FROM students WHERE id = :id")
  suspend fun deleteStudentById(id: Long)

  @Query("DELETE FROM students WHERE studentId = :studentId")
  suspend fun deleteStudentByStudentId(studentId: String)

  @Query("DELETE FROM students")
  suspend fun deleteAllStudents()
}
