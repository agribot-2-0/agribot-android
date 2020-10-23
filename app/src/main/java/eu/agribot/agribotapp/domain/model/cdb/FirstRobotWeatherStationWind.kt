package eu.agribot.agribotapp.domain.model.cdb

import com.google.firebase.Timestamp

data class FirstRobotWeatherStationWind(
    val speed_accuracy: Int,
    val speed_value: Double,
    val timestamp: Timestamp
)
