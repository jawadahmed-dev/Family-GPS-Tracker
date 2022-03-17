package com.example.familygpstracker.models

data class ChildDetail(
    val childId: String,
    val email: String,
    val name: String,
    val parent: Parent,
    val password: String
)