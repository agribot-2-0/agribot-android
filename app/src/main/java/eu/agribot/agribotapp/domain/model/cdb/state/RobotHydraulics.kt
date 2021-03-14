package eu.agribot.agribotapp.domain.model.cdb.state

import com.google.firebase.Timestamp

data class RobotHydraulics(
    val oilCoolerTemperature: Double,
    val oilPressureManifoldB: Double,
    val oilPressureManifoldF: Double,
    val oilPressureTrackBL: Double,
    val oilPressureTrackBR: Double,
    val oilPressureTrackFL: Double,
    val oilPressureTrackFR: Double,
    val oilTankTemperature: Int,
    val timestamp: Timestamp
)
