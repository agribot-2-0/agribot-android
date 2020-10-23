package eu.agribot.agribotapp.domain.model.cdb

import com.google.firebase.Timestamp

data class FirstRobotWeatherStationTemperature(
    val accuracy: Int,
    val value: Double,
    val timestamp: Timestamp
)
