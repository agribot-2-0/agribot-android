package eu.agribot.agribotapp.domain.model.cdb

import com.google.firebase.Timestamp

data class RobotLocalizationPose(
    val altitude: Double,
    val latitude: Double,
    val longitude: Double,
    val heading: Float,
    val timestamp: Timestamp
)
