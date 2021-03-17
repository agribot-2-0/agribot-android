package eu.agribot.agribotapp.domain.model.todomain

data class DiagnosticData(
    val engineRotationsPerMinute: Int,
    val torqueOfEngine: Int,
    val engineFuelRate: Double,
    val fuelLevel: Int,
    val fuelTemperature: Int,
    val batteryVoltage: Double,
    val hydraulicOilTemperature: Int

)
