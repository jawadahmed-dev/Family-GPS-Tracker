package com.example.familygpstracker.models

data class Parent(
    val children: List<Any>,
    val deviceToken: String,
    val notifications: List<Notification>,
    val parentId: String,
    val phoneNumber: String,
    val users: List<Any>
)