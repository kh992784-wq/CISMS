package com.example.data.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * 'Student' Room Entity storing Student data (name, email, id, grade, etc.)
 * to support CRUD operations for the Student Information Management System.
 */
@Entity(
  tableName = "students",
  indices = [
    Index(value = ["studentId"], unique = true),
    Index(value = ["email"], unique = true)
  ]
)
data class Student(
  @PrimaryKey(autoGenerate = true)
  val id: Long = 0,
  val studentId: String,
  val fullName: String, // name
  val email: String, // email
  val grade: String = "A", // grade (e.g. A+, A, B, O)
  val fatherName: String = "",
  val mobile: String = "",
  val dateOfBirth: String = "",
  val gender: String = "Male",
  val course: String = "B.Tech",
  val branch: String = "Computer Science & Engineering",
  val semester: String = "1st Semester",
  val tenthMarks: String = "80%",
  val twelfthMarks: String = "75%",
  val cgpa: String = "8.00",
  val password: String = "password123",
  val registeredDate: String = ""
)

/**
 * Backward compatibility alias for Student Entity.
 */
typealias StudentEntity = Student
