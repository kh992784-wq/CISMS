package com.example.data.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
  tableName = "admins",
  indices = [
    Index(value = ["username"], unique = true)
  ]
)
data class AdminEntity(
  @PrimaryKey(autoGenerate = true)
  val id: Long = 0,
  val username: String,
  val password: String,
  val fullName: String = "Administrator",
  val email: String = "admin@sims.edu"
)
