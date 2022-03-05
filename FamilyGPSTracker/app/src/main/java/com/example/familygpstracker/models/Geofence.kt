package com.example.familygpstracker.models

data class Geofence(
    val category: String,
    val childId: String,
    val geofenceId: String,
    val latitude: Int,
    val longitude: Int,
    val radius: Int
)