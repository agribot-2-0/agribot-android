package eu.agribot.agribotapp.domain.model.cdb

import com.google.firebase.Timestamp

data class RobotHealthEngine(
    val errorCode: Int,
    val errorName: String,
    val timestamp: Timestamp
)
