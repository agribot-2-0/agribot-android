package eu.agribot.agribotapp.communication.database.clouddatabase.model.state

import com.google.firebase.Timestamp
import com.google.gson.annotations.SerializedName
import eu.agribot.agribotapp.communication.database.clouddatabase.model.BaseCloudDatabaseModel
import eu.agribot.agribotapp.domain.model.cdb.state.RobotHydraulics

data class RobotHydraulicsCDB(
    @SerializedName("oil_cooler_temperature")
    val oilCoolerTemperature: Double = 0.0,

    @SerializedName("oil_pressure_manifold_B")
    val oilPressureManifoldB: Double = 0.0,

    @SerializedName("oil_pressure_manifold_F")
    val oilPressureManifoldF: Double = 0.0,

    @SerializedName("oil_pressure_track_BL")
    val oilPressureTrackBL: Double = 0.0,

    @SerializedName("oil_pressure_track_BR")
    val oilPressureTrackBR: Double = 0.0,

    @SerializedName("oil_pressure_track_FL")
    val oilPressureTrackFL: Double = 0.0,

    @SerializedName("oil_pressure_track_FR")
    val oilPressureTrackFR: Double = 0.0,

    @SerializedName("oil_tank_temperature")
    val oilTankTemperature: Int = 0,

    @SerializedName("timestamp")
    val timestamp: Timestamp = Timestamp(0L, 0)

) : BaseCloudDatabaseModel<RobotHydraulics> {

    override fun toDomainModel() = RobotHydraulics(
        oilCoolerTemperature = this.oilCoolerTemperature,
        oilPressureManifoldB = this.oilPressureManifoldB,
        oilPressureManifoldF = this.oilPressureManifoldF,
        oilPressureTrackBL = this.oilPressureTrackBL,
        oilPressureTrackBR = this.oilPressureTrackBR,
        oilPressureTrackFL = this.oilPressureTrackFL,
        oilPressureTrackFR = this.oilPressureTrackFR,
        oilTankTemperature = this.oilTankTemperature,
        timestamp = this.timestamp
    )
}
