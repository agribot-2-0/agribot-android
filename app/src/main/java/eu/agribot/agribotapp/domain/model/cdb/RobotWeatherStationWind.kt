package eu.agribot.agribotapp.domain.model.cdb

import com.google.firebase.Timestamp

data class RobotWeatherStationWind(
    val speedAccuracy: Double,
    val speedValue: Double,
    val timestamp: Timestamp
)
