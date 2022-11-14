package com.example.familygpstracker.models

data class Notification(
    val createdAt: String,
    val message: String,
    val notificationId: String,
    val senderName: String,
    val title: String
)