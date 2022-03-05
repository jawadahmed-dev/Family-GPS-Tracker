package com.example.familygpstracker.models

data class Child(
    val childId: String,
    val code: String,
    val geofences: List<Geofence>,
    val locations: List<Location>,
    val notifications: List<Notification>,
    val parent: Parent,
    val parentId: String,
    val users: List<Any>
)