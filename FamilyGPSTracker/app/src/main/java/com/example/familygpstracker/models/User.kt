package com.example.familygpstracker.models

data class User(
    val child: Child,
    val childId: String,
    val email: String,
    val name: String,
    val parent: Parent,
    val parentId: String,
    val password: String,
    val userId: String,
    val userType: UserType,
    val userTypeId: String
)