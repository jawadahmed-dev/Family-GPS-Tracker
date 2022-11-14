package com.example.familygpstracker.models

data class NotificationDto(
    val body: String,
    val isAndroiodDevice: Boolean = true,
    val recieverId: String,
    val senderId: String,
    val title: String
)