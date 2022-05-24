package com.example.familygpstracker.models

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "ParentDetail")
data class ParentDetail(

    @PrimaryKey
    val parentId: String,
    @Ignore
    val children: List<Child>,
    val email: String,
    val name: String,
    val password: String,
    val phoneNumber: String
)