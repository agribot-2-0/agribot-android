package eu.agribot.agribotapp.domain.model.cdb

import com.google.firebase.Timestamp

data class RobotWeatherStationHumidity(
    val accuracy: Double,
    val value: Double,
    val timestamp: Timestamp
)
