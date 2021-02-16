package eu.agribot.agribotapp.communication.database.clouddatabase.model.state

import com.google.firebase.Timestamp
import eu.agribot.agribotapp.communication.database.clouddatabase.model.BaseCloudDatabaseModel
import eu.agribot.agribotapp.domain.model.cdb.state.RobotEngine

data class RobotEngineCDB(
    val accumulator_voltage: Double = 0.0,
    val engine_protection_lamp: String = "",
    val engine_stop_lamp: String = "",
    val engine_warning_lamp: String = "",
    val error_number: Int = 0,
    val error_state: String = "",
    val error_total_count: Int = 0,
    val fmi: Int = 0,
    val fuel_level: Int = 0,
    val fuel_rate: Int = 0,
    val fuel_temperature: Int = 0,
    val occ: Int = 0,
    val rpm: Int = 0,
    val spn: Int = 0,
    val timestamp: Timestamp = Timestamp(0L, 0),
    val torque_of_engine: Int = 0

) : BaseCloudDatabaseModel<RobotEngine> {

    override fun toDomainModel() = RobotEngine(
        accumulator_voltage = this.accumulator_voltage,
        engine_protection_lamp = this.engine_protection_lamp,
        engine_stop_lamp = this.engine_stop_lamp,
        engine_warning_lamp = this.engine_warning_lamp,
        error_number = this.error_number,
        error_state = this.error_state,
        error_total_count = this.error_total_count,
        fmi = this.fmi,
        fuel_level = this.fuel_level,
        fuel_rate = this.fuel_rate,
        fuel_temperature = this.fuel_temperature,
        occ = this.occ,
        rpm = this.rpm,
        spn = this.spn,
        timestamp = this.timestamp,
        torque_of_engine = this.torque_of_engine
    )
}
