package eu.agribot.agribotapp.domain.model

import com.google.firebase.Timestamp

data class FirstRobotWeatherStationHumidity(
    val accuracy: Int,
    val value: Int,
    val timestamp: Timestamp
)
