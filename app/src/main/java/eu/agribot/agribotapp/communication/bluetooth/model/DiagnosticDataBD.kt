package eu.agribot.agribotapp.communication.bluetooth.model

import eu.agribot.agribotapp.domain.model.DiagnosticData

class DiagnosticDataBD(
    var engineRotationsPerMinute: Int = -1,
    var torqueOfEngine: Int = -1,
    var engineFuelRate: Double = 0.0,
    var fuelLevel: Double = 0.0,
    var fuelTemperature: Int = -1,
    var batteryVoltage: String = "1"

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
