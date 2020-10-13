package eu.agribot.agribotapp.domain.model

import com.google.firebase.Timestamp

data class FirstRobotWeatherStationTemperature(
    val accuracy: Int,
    val temperature: Int,
    val timestamp: Timestamp
)
