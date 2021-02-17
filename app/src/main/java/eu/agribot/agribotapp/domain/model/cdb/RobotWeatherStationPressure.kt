package eu.agribot.agribotapp.domain.model.cdb

import com.google.firebase.Timestamp

data class RobotWeatherStationPressure(
    val accuracy: Double,
    val timestamp: Timestamp,
    val value: Double
)
