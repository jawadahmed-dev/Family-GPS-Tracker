package com.example.familygpstracker.models

data class Notification(
    val childId: String,
    val createdAt: String,
    val message: String,
    val notificationId: String,
    val parentId: String,
    val title: String
)