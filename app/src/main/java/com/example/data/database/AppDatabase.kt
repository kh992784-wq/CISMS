package com.example.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.data.dao.AdminDao
import com.example.data.dao.StudentDao
import com.example.data.model.AdminEntity
import com.example.data.model.Student
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * 'AppDatabase' class providing Room persistence for the Student Information Management System.
 */
@Database(
  entities = [Student::class, AdminEntity::class],
  version = 1,
  exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
  abstract fun studentDao(): StudentDao
  abstract fun adminDao(): AdminDao

  companion object {
    @Volatile
    private var INSTANCE: AppDatabase? = null

    fun getDatabase(context: Context, scope: CoroutineScope): AppDatabase {
      return INSTANCE ?: synchronized(this) {
        val instance = Room.databaseBuilder(
          context.applicationContext,
          AppDatabase::class.java,
          "student_database"
        )
          .addCallback(AppDatabaseCallback(scope))
          .fallbackToDestructiveMigration()
          .build()
        INSTANCE = instance
        instance
      }
    }

    private class AppDatabaseCallback(
      private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {
      override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)
        INSTANCE?.let { database ->
          scope.launch(Dispatchers.IO) {
            populateInitialData(database.adminDao(), database.studentDao())
          }
        }
      }
    }

    suspend fun populateInitialData(adminDao: AdminDao, studentDao: StudentDao) {
      val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
      val pastDate = "2026-10-15"

      // Seed default admin
      if (adminDao.countAdmins() == 0) {
        adminDao.insertAdmin(
          AdminEntity(
            username = "admin",
            password = "password123",
            fullName = "Admin",
            email = "admin@sims.edu"
          )
        )
      }

      // Seed students matching the reference design
      val initialStudents = listOf(
        Student(
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
          registeredDate = today
        ),
        Student(
          studentId = "2025ST002",
          fullName = "Rahul",
          fatherName = "Suresh Sharma",
          email = "rahul@gmail.com",
          mobile = "9876543211",
          dateOfBirth = "15-08-2004",
          gender = "Male",
          course = "B.Tech",
          branch = "Mechanical Engineering",
          semester = "4th Semester",
          grade = "A",
          tenthMarks = "82%",
          twelfthMarks = "75%",
          cgpa = "8.10",
          password = "password123",
          registeredDate = today
        ),
        Student(
          studentId = "2025ST003",
          fullName = "Priya",
          fatherName = "Ramesh Patel",
          email = "priya@gmail.com",
          mobile = "9876543212",
          dateOfBirth = "20-01-2005",
          gender = "Female",
          course = "BCA",
          branch = "Computer Applications",
          semester = "2nd Semester",
          grade = "A+",
          tenthMarks = "88%",
          twelfthMarks = "84%",
          cgpa = "8.90",
          password = "password123",
          registeredDate = today
        ),
        Student(
          studentId = "2025ST004",
          fullName = "Aman",
          fatherName = "Vijay Verma",
          email = "aman@gmail.com",
          mobile = "9876543213",
          dateOfBirth = "05-11-2004",
          gender = "Male",
          course = "B.Tech",
          branch = "Civil Engineering",
          semester = "3rd Semester",
          grade = "B+",
          tenthMarks = "79%",
          twelfthMarks = "74%",
          cgpa = "7.80",
          password = "password123",
          registeredDate = pastDate
        ),
        Student(
          studentId = "2025ST005",
          fullName = "Neha",
          fatherName = "Anil Singh",
          email = "neha@gmail.com",
          mobile = "9876543214",
          dateOfBirth = "11-03-2005",
          gender = "Female",
          course = "B.Sc",
          branch = "Physics",
          semester = "1st Semester",
          grade = "O",
          tenthMarks = "91%",
          twelfthMarks = "89%",
          cgpa = "9.20",
          password = "password123",
          registeredDate = pastDate
        ),
        Student(
          studentId = "2025ST006",
          fullName = "Rohan",
          fatherName = "Sunil Gupta",
          email = "rohan@gmail.com",
          mobile = "9876543215",
          dateOfBirth = "29-07-2004",
          gender = "Male",
          course = "B.Tech",
          branch = "Electrical Engineering",
          semester = "4th Semester",
          grade = "A",
          tenthMarks = "84%",
          twelfthMarks = "80%",
          cgpa = "8.35",
          password = "password123",
          registeredDate = pastDate
        ),
        Student(
          studentId = "2025ST007",
          fullName = "Ananya",
          fatherName = "Deepak Joshi",
          email = "ananya@gmail.com",
          mobile = "9876543216",
          dateOfBirth = "18-09-2005",
          gender = "Female",
          course = "B.Tech",
          branch = "Information Technology",
          semester = "2nd Semester",
          grade = "A+",
          tenthMarks = "89%",
          twelfthMarks = "86%",
          cgpa = "9.05",
          password = "password123",
          registeredDate = pastDate
        ),
        Student(
          studentId = "2025ST008",
          fullName = "Kavita",
          fatherName = "Mahesh Yadav",
          email = "kavita@gmail.com",
          mobile = "9876543217",
          dateOfBirth = "23-04-2005",
          gender = "Female",
          course = "BCA",
          branch = "Data Science",
          semester = "2nd Semester",
          grade = "A",
          tenthMarks = "86%",
          twelfthMarks = "81%",
          cgpa = "8.45",
          password = "password123",
          registeredDate = pastDate
        ),
        Student(
          studentId = "2025ST009",
          fullName = "Siddharth",
          fatherName = "Alok Roy",
          email = "siddharth@gmail.com",
          mobile = "9876543218",
          dateOfBirth = "14-12-2004",
          gender = "Male",
          course = "B.Tech",
          branch = "Computer Science & Engineering",
          semester = "5th Semester",
          grade = "O",
          tenthMarks = "93%",
          twelfthMarks = "90%",
          cgpa = "9.40",
          password = "password123",
          registeredDate = pastDate
        ),
        Student(
          studentId = "2025ST010",
          fullName = "Divya",
          fatherName = "Vinod Nair",
          email = "divya@gmail.com",
          mobile = "9876543219",
          dateOfBirth = "02-06-2005",
          gender = "Female",
          course = "B.Sc",
          branch = "Mathematics",
          semester = "3rd Semester",
          grade = "A+",
          tenthMarks = "87%",
          twelfthMarks = "83%",
          cgpa = "8.75",
          password = "password123",
          registeredDate = pastDate
        ),
        Student(
          studentId = "2025ST011",
          fullName = "Arjun",
          fatherName = "Santosh Mehta",
          email = "arjun@gmail.com",
          mobile = "9876543220",
          dateOfBirth = "30-10-2004",
          gender = "Male",
          course = "B.Tech",
          branch = "Mechanical Engineering",
          semester = "3rd Semester",
          grade = "A",
          tenthMarks = "81%",
          twelfthMarks = "77%",
          cgpa = "8.15",
          password = "password123",
          registeredDate = pastDate
        ),
        Student(
          studentId = "2025ST012",
          fullName = "Sneha",
          fatherName = "Prakash Rao",
          email = "sneha@gmail.com",
          mobile = "9876543221",
          dateOfBirth = "09-02-2005",
          gender = "Female",
          course = "MBA",
          branch = "Marketing & HR",
          semester = "1st Semester",
          grade = "A+",
          tenthMarks = "88%",
          twelfthMarks = "85%",
          cgpa = "8.90",
          password = "password123",
          registeredDate = pastDate
        ),
        Student(
          studentId = "2025ST013",
          fullName = "Varun",
          fatherName = "Manoj Tiwari",
          email = "varun@gmail.com",
          mobile = "9876543222",
          dateOfBirth = "16-07-2004",
          gender = "Male",
          course = "B.Tech",
          branch = "Civil Engineering",
          semester = "4th Semester",
          grade = "B+",
          tenthMarks = "78%",
          twelfthMarks = "73%",
          cgpa = "7.65",
          password = "password123",
          registeredDate = pastDate
        ),
        Student(
          studentId = "2025ST014",
          fullName = "Pooja",
          fatherName = "Harish Bhatia",
          email = "pooja@gmail.com",
          mobile = "9876543223",
          dateOfBirth = "25-11-2005",
          gender = "Female",
          course = "BCA",
          branch = "Cloud Computing",
          semester = "2nd Semester",
          grade = "A",
          tenthMarks = "85%",
          twelfthMarks = "82%",
          cgpa = "8.60",
          password = "password123",
          registeredDate = pastDate
        ),
        Student(
          studentId = "2025ST015",
          fullName = "Manish",
          fatherName = "Satish Pandey",
          email = "manish@gmail.com",
          mobile = "9876543224",
          dateOfBirth = "04-04-2004",
          gender = "Male",
          course = "M.Tech",
          branch = "Artificial Intelligence",
          semester = "2nd Semester",
          grade = "O",
          tenthMarks = "92%",
          twelfthMarks = "88%",
          cgpa = "9.10",
          password = "password123",
          registeredDate = pastDate
        ),
        Student(
          studentId = "2025ST016",
          fullName = "Tanvi",
          fatherName = "Girish Deshmukh",
          email = "tanvi@gmail.com",
          mobile = "9876543225",
          dateOfBirth = "19-08-2005",
          gender = "Female",
          course = "B.Tech",
          branch = "Computer Science & Engineering",
          semester = "3rd Semester",
          grade = "A+",
          tenthMarks = "90%",
          twelfthMarks = "87%",
          cgpa = "8.95",
          password = "password123",
          registeredDate = pastDate
        ),
        Student(
          studentId = "2025ST017",
          fullName = "Aditya",
          fatherName = "Naveen Choudhary",
          email = "aditya@gmail.com",
          mobile = "9876543226",
          dateOfBirth = "10-01-2005",
          gender = "Male",
          course = "B.Tech",
          branch = "Electronics & Communication",
          semester = "3rd Semester",
          grade = "A",
          tenthMarks = "83%",
          twelfthMarks = "79%",
          cgpa = "8.25",
          password = "password123",
          registeredDate = pastDate
        ),
        Student(
          studentId = "2025ST018",
          fullName = "Isha",
          fatherName = "Rajendra Saxena",
          email = "isha@gmail.com",
          mobile = "9876543227",
          dateOfBirth = "28-03-2005",
          gender = "Female",
          course = "B.Sc",
          branch = "Chemistry",
          semester = "2nd Semester",
          grade = "A",
          tenthMarks = "86%",
          twelfthMarks = "82%",
          cgpa = "8.50",
          password = "password123",
          registeredDate = pastDate
        ),
        Student(
          studentId = "2025ST019",
          fullName = "Kunal",
          fatherName = "Ashok Malhotra",
          email = "kunal@gmail.com",
          mobile = "9876543228",
          dateOfBirth = "07-09-2004",
          gender = "Male",
          course = "B.Tech",
          branch = "Mechanical Engineering",
          semester = "5th Semester",
          grade = "B+",
          tenthMarks = "80%",
          twelfthMarks = "76%",
          cgpa = "7.95",
          password = "password123",
          registeredDate = pastDate
        ),
        Student(
          studentId = "2025ST020",
          fullName = "Ritu",
          fatherName = "Hemant Jain",
          email = "ritu@gmail.com",
          mobile = "9876543229",
          dateOfBirth = "17-05-2005",
          gender = "Female",
          course = "BCA",
          branch = "Software Engineering",
          semester = "1st Semester",
          grade = "A+",
          tenthMarks = "89%",
          twelfthMarks = "85%",
          cgpa = "8.80",
          password = "password123",
          registeredDate = pastDate
        ),
        Student(
          studentId = "2025ST021",
          fullName = "Harsh",
          fatherName = "Virendra Mishra",
          email = "harsh@gmail.com",
          mobile = "9876543230",
          dateOfBirth = "03-12-2004",
          gender = "Male",
          course = "B.Tech",
          branch = "Computer Science & Engineering",
          semester = "4th Semester",
          grade = "A",
          tenthMarks = "87%",
          twelfthMarks = "84%",
          cgpa = "8.70",
          password = "password123",
          registeredDate = pastDate
        ),
        Student(
          studentId = "2025ST022",
          fullName = "Megha",
          fatherName = "Brijesh Agarwal",
          email = "megha@gmail.com",
          mobile = "9876543231",
          dateOfBirth = "21-10-2005",
          gender = "Female",
          course = "B.Sc",
          branch = "Computer Science",
          semester = "2nd Semester",
          grade = "O",
          tenthMarks = "92%",
          twelfthMarks = "89%",
          cgpa = "9.15",
          password = "password123",
          registeredDate = pastDate
        ),
        Student(
          studentId = "2025ST023",
          fullName = "Vikas",
          fatherName = "Pradeep Yadav",
          email = "vikas@gmail.com",
          mobile = "9876543232",
          dateOfBirth = "13-02-2004",
          gender = "Male",
          course = "B.Tech",
          branch = "Civil Engineering",
          semester = "5th Semester",
          grade = "B",
          tenthMarks = "77%",
          twelfthMarks = "72%",
          cgpa = "7.50",
          password = "password123",
          registeredDate = pastDate
        ),
        Student(
          studentId = "2025ST024",
          fullName = "Shreya",
          fatherName = "Sudhir Kulkarni",
          email = "shreya@gmail.com",
          mobile = "9876543233",
          dateOfBirth = "06-06-2005",
          gender = "Female",
          course = "MBA",
          branch = "Finance",
          semester = "2nd Semester",
          grade = "O",
          tenthMarks = "91%",
          twelfthMarks = "88%",
          cgpa = "9.25",
          password = "password123",
          registeredDate = pastDate
        ),
        Student(
          studentId = "2025ST025",
          fullName = "Mohit",
          fatherName = "Devendra Rawat",
          email = "mohit@gmail.com",
          mobile = "9876543234",
          dateOfBirth = "24-09-2004",
          gender = "Male",
          course = "B.Tech",
          branch = "Electrical Engineering",
          semester = "3rd Semester",
          grade = "A",
          tenthMarks = "84%",
          twelfthMarks = "79%",
          cgpa = "8.20",
          password = "password123",
          registeredDate = pastDate
        )
      )
      studentDao.insertAll(initialStudents)
    }
  }
}

/**
 * 'StudentDatabase' implementation providing access to AppDatabase.
 */
object StudentDatabase {
  fun getInstance(context: Context, scope: CoroutineScope): AppDatabase {
    return AppDatabase.getDatabase(context, scope)
  }

  fun getInMemoryDatabase(context: Context): AppDatabase {
    return Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
      .allowMainThreadQueries()
      .build()
  }
}

/**
 * Backward compatibility alias for SimsDatabase.
 */
typealias SimsDatabase = AppDatabase
