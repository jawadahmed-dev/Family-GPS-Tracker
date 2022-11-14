package com.example.familygpstracker.models

data class Geofence(
    val category: String,
    val child: Child,
    val geofenceId: String,
    val latitude: Double,
    val longitude: Double,
    val radius: Double
)