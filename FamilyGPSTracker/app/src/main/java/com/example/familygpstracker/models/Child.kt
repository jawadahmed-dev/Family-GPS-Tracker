package com.example.familygpstracker.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Child")
data class Child(
    @PrimaryKey
    val childId: String,
    val email: String,
    val name: String,
    val password: String,
    val parentId: String?
)