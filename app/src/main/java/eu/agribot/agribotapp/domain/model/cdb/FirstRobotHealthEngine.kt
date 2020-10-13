package eu.agribot.agribotapp.domain.model

import com.google.firebase.Timestamp

data class FirstRobotHealthEngine(
    val errorCode: Int,
    val errorName: String,
    val timestamp: Timestamp
)
