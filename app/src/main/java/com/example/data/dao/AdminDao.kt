package com.example.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.model.AdminEntity

@Dao
interface AdminDao {
  @Query("SELECT * FROM admins WHERE LOWER(username) = LOWER(:username) LIMIT 1")
  suspend fun getAdminByUsername(username: String): AdminEntity?

  @Query("SELECT COUNT(*) FROM admins")
  suspend fun countAdmins(): Int

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertAdmin(admin: AdminEntity): Long
}
