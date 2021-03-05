package eu.agribot.agribotapp.domain.model.todomain

data class DiagnosticData(
    val engineRotationsPerMinute: Int,
    val torqueOfEngine: Int,
    val engineFuelRate: String,
    val fuelLevel: Int,
    val fuelTemperature: Int,
    val batteryVoltage: String,
    val hydraulicOilTemperature: String

)
