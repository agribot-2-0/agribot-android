package eu.agribot.agribotapp.domain.model.cdb

import com.google.firebase.Timestamp

data class RobotWeatherStationHumidity(
    val accuracy: Int,
    val value: Int,
    val timestamp: Timestamp
)
