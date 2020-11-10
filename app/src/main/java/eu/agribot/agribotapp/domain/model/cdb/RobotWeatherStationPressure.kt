package eu.agribot.agribotapp.domain.model.cdb

import com.google.firebase.Timestamp

data class RobotWeatherStationPressure(
    val accuracy: Int,
    val timestamp: Timestamp,
    val value: Double
)
