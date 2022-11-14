package com.example.familygpstracker.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Parent")
data class Parent(
    val email: String,
    val name: String,
    @PrimaryKey
    val parentId: String,
    val password: String,
    val phoneNumber: String,
    val deviceToken: String
)