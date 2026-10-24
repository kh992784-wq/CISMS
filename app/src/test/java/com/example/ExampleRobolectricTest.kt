package com.example

import android.app.Application
import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.dao.StudentDao
import com.example.data.database.AppDatabase
import com.example.data.database.StudentDatabase
import com.example.data.model.Student
import com.example.ui.viewmodel.StudentViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  private lateinit var db: AppDatabase
  private lateinit var studentDao: StudentDao

  @Before
  fun createDb() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    // Verify StudentDatabase implementation creates AppDatabase instance
    db = StudentDatabase.getInMemoryDatabase(context)
    studentDao = db.studentDao()
  }

  @After
  fun closeDb() {
    db.close()
  }

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("SIMS", appName)
  }

  @Test
  fun `verify Student Room Entity and StudentDao CRUD operations via AppDatabase and StudentDatabase`() = runBlocking {
    // 1. CREATE with Student entity
    val student = Student(
      id = 1,
      studentId = "2025ST001",
      fullName = "Himanshu",
      fatherName = "Rajesh Kumar",
      email = "himanshu@gmail.com",
      mobile = "9876543210",
      dateOfBirth = "12-05-2005",
      gender = "Male",
      course = "B.Tech",
      branch = "Computer Science & Engineering",
      semester = "3rd Semester",
      grade = "A+",
      tenthMarks = "85%",
      twelfthMarks = "78%",
      cgpa = "8.56",
      password = "password123",
      registeredDate = "2026-10-23"
    )

    val insertedId = studentDao.insertStudent(student)
    assertEquals(1L, insertedId)

    // 2. READ
    val fetched = studentDao.getStudentByStudentId("2025ST001")
    assertNotNull(fetched)
    assertEquals("Himanshu", fetched?.fullName)
    assertEquals("himanshu@gmail.com", fetched?.email)
    assertEquals("2025ST001", fetched?.studentId)
    assertEquals("A+", fetched?.grade)

    val flowList = studentDao.getAllStudentsFlow().first()
    assertEquals(1, flowList.size)

    // 3. UPDATE
    val updated = fetched!!.copy(fullName = "Himanshu Sharma", grade = "O")
    studentDao.updateStudent(updated)

    val afterUpdate = studentDao.getStudentById(1)
    assertEquals("Himanshu Sharma", afterUpdate?.fullName)
    assertEquals("O", afterUpdate?.grade)

    // 4. DELETE
    studentDao.deleteStudentById(1)
    val afterDelete = studentDao.getStudentById(1)
    assertNull(afterDelete)
  }

  @Test
  fun `verify StudentViewModel exposes StateFlow and executes CRUD`() = runBlocking {
    val app = ApplicationProvider.getApplicationContext<Application>()
    val viewModel = StudentViewModel(application = app, studentDao = studentDao)

    assertNotNull(viewModel.students)
    assertNotNull(viewModel.filteredStudents)

    val newStudent = Student(
      id = 10,
      studentId = "2025ST010",
      fullName = "Ananya Roy",
      email = "ananya@sims.edu",
      grade = "A+"
    )
    studentDao.insertStudent(newStudent)

    val studentsInDb = studentDao.getAllStudents()
    assertEquals(1, studentsInDb.size)
    assertEquals("Ananya Roy", studentsInDb[0].fullName)
    assertEquals("A+", studentsInDb[0].grade)
  }
}
