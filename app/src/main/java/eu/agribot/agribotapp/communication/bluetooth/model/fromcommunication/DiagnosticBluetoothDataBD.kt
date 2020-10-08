package eu.agribot.agribotapp.communication.bluetooth.model.fromcommunication

import eu.agribot.agribotapp.domain.model.todomain.DiagnosticData

data class DiagnosticBluetoothDataBD(
    val engineRotationsPerMinute: Int,
    val torqueOfEngine: Int,
    val engineFuelRate: String,
    val fuelLevel: Int,
    val fuelTemperature: Int,
    val batteryVoltage: String

) : BaseBluetoothDataModel<DiagnosticData> {
    override fun toDomainModel() = DiagnosticData(
        engineRotationsPerMinute = this.engineRotationsPerMinute,
        torqueOfEngine = this.torqueOfEngine,
        engineFuelRate = this.engineFuelRate,
        fuelLevel = this.fuelLevel,
        fuelTemperature = this.fuelTemperature,
        batteryVoltage = this.batteryVoltage
    )
}
