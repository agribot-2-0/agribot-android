package eu.agribot.agribotapp.domain.model.cdb.state

import com.google.firebase.Timestamp

data class RobotHydraulics(
    val oil_cooler_temperature: Double,
    val oil_pressure_manifold_B: Double,
    val oil_pressure_manifold_F: Double,
    val oil_pressure_track_BL: Double,
    val oil_pressure_track_BR: Double,
    val oil_pressure_track_FL: Double,
    val oil_pressure_track_FR: Double,
    val oil_tank_temperature: Int,
    val timestamp: Timestamp
)
