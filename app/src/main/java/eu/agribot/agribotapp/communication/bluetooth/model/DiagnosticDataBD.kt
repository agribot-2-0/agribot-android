package eu.agribot.agribotapp.communication.bluetooth.model

import eu.agribot.agribotapp.domain.model.DiagnosticData

class DiagnosticDataBD(
    val engineRotationsPerMinute: Int = 1,
    val torqueOfEngine: Int = 1,
    val engineFuelRate: Double = 1.0,
    val fuelLevel: Double = 1.0,
    val fuelTemperature: Int = 1,
    val batteryVoltage: String = " "

) : BaseDataModel<DiagnosticData> {
    override fun toDomainModel() = DiagnosticData(
        engineRotationsPerMinute = this.engineRotationsPerMinute,
        torqueOfEngine = this.torqueOfEngine,
        engineFuelRate = this.engineFuelRate,
        fuelLevel = this.fuelLevel,
        fuelTemperature = this.fuelTemperature,
        batteryVoltage = this.batteryVoltage
    )


}

