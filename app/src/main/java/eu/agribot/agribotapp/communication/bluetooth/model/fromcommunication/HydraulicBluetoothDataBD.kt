package eu.agribot.agribotapp.communication.bluetooth.model.fromcommunication

import eu.agribot.agribotapp.domain.model.todomain.HydraulicData

data class HydraulicBluetoothDataBD(
    val oilTemperature: String
) : BaseBluetoothDataModel<HydraulicData> {
    override fun toDomainModel() = HydraulicData(
        oilTemperature = this.oilTemperature
    )
}
