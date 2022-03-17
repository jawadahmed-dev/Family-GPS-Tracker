package com.example.familygpstracker.models

data class User(
    val child: Child?,
    val parent: Parent?,
    val userId: String,
    val userType: String
)