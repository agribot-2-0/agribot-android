package eu.agribot.agribotapp.communication.database.clouddatabase.model.state

import com.google.firebase.Timestamp
import com.google.gson.annotations.SerializedName
import eu.agribot.agribotapp.communication.database.clouddatabase.model.BaseCloudDatabaseModel
import eu.agribot.agribotapp.domain.model.cdb.state.RobotEngine

data class RobotEngineCDB(
    @SerializedName("accumulator_voltage")
    val accumulatorVoltage: Double = 0.0,

    @SerializedName("engine_protection_lamp")
    val engineProtectionLamp: String = "",

    @SerializedName("engine_stop_lamp")
    val engineStopLamp: String = "",

    @SerializedName("engine_warning_lamp")
    val engineWarningLamp: String = "",

    @SerializedName("error_number")
    val errorNumber: Int = 0,

    @SerializedName("error_state")
    val errorState: String = "",

    @SerializedName("error_total_count")
    val errorTotalCount: Int = 0,

    @SerializedName("fmi")
    val fmi: Int = 0,

    @SerializedName("fuel_level")
    val fuelLevel: Int = 0,

    @SerializedName("fuel_rate")
    val fuelRate: Int = 0,

    @SerializedName("fuel_temperature")
    val fuelTemperature: Int = 0,

    @SerializedName("occ")
    val occ: Int = 0,

    @SerializedName("rpm")
    val rpm: Int = 0,

    @SerializedName("spn")
    val spn: Int = 0,

    @SerializedName("timestamp")
    val timestamp: Timestamp = Timestamp(0L, 0),

    @SerializedName("torque_of_engine")
    val torqueOfEngine: Int = 0

) : BaseCloudDatabaseModel<RobotEngine> {

    override fun toDomainModel() = RobotEngine(
        accumulatorVoltage = this.accumulatorVoltage,
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
