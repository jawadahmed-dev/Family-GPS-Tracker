package com.example.familygpstracker.models

data class CreateGeofence(
    val latitude: Double,
    val longitude: Double,
    val radius: Double,
    val category: String
)
