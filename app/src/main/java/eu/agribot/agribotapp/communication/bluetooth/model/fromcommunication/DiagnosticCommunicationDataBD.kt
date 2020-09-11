package eu.agribot.agribotapp.communication.bluetooth.model.fromcommunication

import eu.agribot.agribotapp.domain.model.todomain.DiagnosticData

class DiagnosticCommunicationDataBD(
    val engineRotationsPerMinute: Int = 1,
    val torqueOfEngine: Int = 1,
    val engineFuelRate: String = " ",
    val fuelLevel: Int = 1,
    val fuelTemperature: Int = 1,
    val batteryVoltage: String = " "

) : BaseCommunicationDataModel<DiagnosticData> {
    override fun toDomainModel() = DiagnosticData(
        engineRotationsPerMinute = this.engineRotationsPerMinute,
        torqueOfEngine = this.torqueOfEngine,
        engineFuelRate = this.engineFuelRate,
        fuelLevel = this.fuelLevel,
        fuelTemperature = this.fuelTemperature,
        batteryVoltage = this.batteryVoltage
    )
}
