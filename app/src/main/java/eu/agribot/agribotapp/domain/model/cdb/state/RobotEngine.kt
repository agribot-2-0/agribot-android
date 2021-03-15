package eu.agribot.agribotapp.domain.model.cdb.state

import com.google.firebase.Timestamp

data class RobotEngine(
    val accumulatorVoltage: Double,
    val engineProtectionLamp: String,
    val engineStopLamp: String,
    val engineWarningLamp: String,
    val errorNumber: Int,
    val errorState: String,
    val errorTotalCount: Int,
    val fmi: Int,
    val fuelLevel: Int,
    val fuelRate: Int,
    val fuelTemperature: Int,
    val occ: Int,
    val rpm: Int,
    val spn: Int,
    val timestamp: Timestamp,
    val torqueOfEngine: Int
)
