package com.example.familygpstracker.models

data class ParentDetail(
    val children: List<Child>,
    val email: String,
    val name: String,
    val parentId: String,
    val password: String,
    val phoneNumber: String
)