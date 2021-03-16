package eu.agribot.agribotapp.communication.database.clouddatabase.model.state

import com.google.firebase.Timestamp
import com.google.firebase.firestore.PropertyName
import eu.agribot.agribotapp.communication.database.clouddatabase.model.BaseCloudDatabaseModel
import eu.agribot.agribotapp.domain.model.cdb.state.RobotEngine

data class RobotEngineCDB(
    @get:PropertyName("accumulator_voltage")
    @set:PropertyName("accumulator_voltage")
    var xd: Double = 0.0,

    @get:PropertyName("engine_protection_lamp")
    @set:PropertyName("engine_protection_lamp")
    var engineProtectionLamp: String = "",

    @get:PropertyName("engine_stop_lamp")
    @set:PropertyName("engine_stop_lamp")
    var engineStopLamp: String = "",

    @get:PropertyName("engine_warning_lamp")
    @set:PropertyName("engine_warning_lamp")
    var engineWarningLamp: String = "",

    @get:PropertyName("error_number")
    @set:PropertyName("error_number")
    var errorNumber: Int = 0,

    @get:PropertyName("error_state")
    @set:PropertyName("error_state")
    var errorState: String = "",

    @get:PropertyName("error_total_count")
    @set:PropertyName("error_total_count")
    var errorTotalCount: Int = 0,

    @get:PropertyName("fmi")
    @set:PropertyName("fmi")
    var fmi: Int = 0,

    @get:PropertyName("fuel_level")
    @set:PropertyName("fuel_level")
    var fuelLevel: Int = 0,

    @get:PropertyName("fuel_rate")
    @set:PropertyName("fuel_rate")
    var fuelRate: Int = 0,

    @get:PropertyName("fuel_temperature")
    @set:PropertyName("fuel_temperature")
    var fuelTemperature: Int = 0,

    @get:PropertyName("occ")
    @set:PropertyName("occ")
    var occ: Int = 0,

    @get:PropertyName("rpm")
    @set:PropertyName("rpm")
    var rpm: Int = 0,

    @get:PropertyName("spn")
    @set:PropertyName("spn")
    var spn: Int = 0,

    @get:PropertyName("timestamp")
    @set:PropertyName("timestamp")
    var timestamp: Timestamp = Timestamp(0L, 0),

    @get:PropertyName("torque_of_engine")
    @set:PropertyName("torque_of_engine")
    var torqueOfEngine: Int = 0

) : BaseCloudDatabaseModel<RobotEngine> {

    override fun toDomainModel() = RobotEngine(
        accumulatorVoltage = this.xd,
        engineProtectionLamp = this.engineProtectionLamp,
        engineStopLamp = this.engineStopLamp,
        engineWarningLamp = this.engineWarningLamp,
        errorNumber = this.errorNumber,
        errorState = this.errorState,
        errorTotalCount = this.errorTotalCount,
        fmi = this.fmi,
        fuelLevel = this.fuelLevel,
        fuelRate = this.fuelRate,
        fuelTemperature = this.fuelTemperature,
        occ = this.occ,
        rpm = this.rpm,
        spn = this.spn,
        timestamp = this.timestamp,
        torqueOfEngine = this.torqueOfEngine
    )
}
