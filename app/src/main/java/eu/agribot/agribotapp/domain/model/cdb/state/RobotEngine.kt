package eu.agribot.agribotapp.domain.model.cdb.state

import com.google.firebase.Timestamp

data class RobotEngine(
    val accumulator_voltage: Double,
    val engine_protection_lamp: String,
    val engine_stop_lamp: String,
    val engine_warning_lamp: String,
    val error_number: Int,
    val error_state: String,
    val error_total_count: Int,
    val fmi: Int,
    val fuel_level: Int,
    val fuel_rate: Int,
    val fuel_temperature: Int,
    val occ: Int,
    val rpm: Int,
    val spn: Int,
    val timestamp: Timestamp,
    val torque_of_engine: Int
)
