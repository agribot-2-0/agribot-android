package eu.agribot.agribotapp.communication.bluetooth.model.fromcommunication

import eu.agribot.agribotapp.domain.model.todomain.DiagnosticData

data class DiagnosticBluetoothDataBD(
    val engineRotationsPerMinute: Int = -1,
    val torqueOfEngine: Int = -1,
    val engineFuelRate: String = "",
    val fuelLevel: Int = -1,
    val fuelTemperature: Int = -1,
    val batteryVoltage: String = "",
    val hydraulicOilTemperature: String = ""

) : BaseBluetoothDataModel<DiagnosticData> {
    override fun toDomainModel() = DiagnosticData(
        engineRotationsPerMinute = this.engineRotationsPerMinute,
        torqueOfEngine = this.torqueOfEngine,
        engineFuelRate = this.engineFuelRate,
        fuelLevel = this.fuelLevel,
        fuelTemperature = this.fuelTemperature,
        batteryVoltage = this.batteryVoltage,
        hydraulicOilTemperature = this.hydraulicOilTemperature
    )
}
