package eu.agribot.agribotapp.domain.model.cdb

import com.google.firebase.Timestamp

data class RobotWeatherStationWind(
    val speed_accuracy: Double,
    val speed_value: Double,
    val timestamp: Timestamp
)
