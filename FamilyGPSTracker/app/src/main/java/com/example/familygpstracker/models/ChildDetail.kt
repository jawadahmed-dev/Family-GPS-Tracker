package com.example.familygpstracker.models

import androidx.room.Entity
import androidx.room.Ignore

@Entity(tableName = "ChildDetail")
data class ChildDetail(
    val childId: String,
    val email: String,
    val name: String,
    @Ignore
    val parent: Parent,
    val password: String
)