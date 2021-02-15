package eu.agribot.agribotapp.communication.database.clouddatabase.model.state

import com.google.firebase.Timestamp
import eu.agribot.agribotapp.communication.database.clouddatabase.model.BaseCloudDatabaseModel
import eu.agribot.agribotapp.domain.model.cdb.state.RobotHydraulics

data class RobotHydraulicsCDB(
    val oil_cooler_temperature: Double = 0.0,
    val oil_pressure_manifold_B: Double = 0.0,
    val oil_pressure_manifold_F: Double = 0.0,
    val oil_pressure_track_BL: Double = 0.0,
    val oil_pressure_track_BR: Double = 0.0,
    val oil_pressure_track_FL: Double = 0.0,
    val oil_pressure_track_FR: Double = 0.0,
    val oil_tank_temperature: Int = 0,
    val timestamp: Timestamp = Timestamp(0L, 0)

) : BaseCloudDatabaseModel<RobotHydraulics> {

    override fun toDomainModel() = RobotHydraulics(
        oil_cooler_temperature = this.oil_cooler_temperature,
        oil_pressure_manifold_B = this.oil_pressure_manifold_B,
        oil_pressure_manifold_F = this.oil_pressure_manifold_F,
        oil_pressure_track_BL = this.oil_pressure_track_BL,
        oil_pressure_track_BR = this.oil_pressure_track_BR,
        oil_pressure_track_FL = this.oil_pressure_track_FL,
        oil_pressure_track_FR = this.oil_pressure_track_FR,
        oil_tank_temperature = this.oil_tank_temperature,
        timestamp = this.timestamp
    )
}
