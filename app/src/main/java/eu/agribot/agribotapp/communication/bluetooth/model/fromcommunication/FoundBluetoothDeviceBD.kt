package eu.agribot.agribotapp.communication.bluetooth.model.fromcommunication

import eu.agribot.agribotapp.domain.model.todomain.FoundBluetoothDevice

class FoundBluetoothDeviceBD(
    val foundDeviceName: String,
    val foundDeviceAdress: String
) : BaseCommunicationDataModel<FoundBluetoothDevice> {
    override fun toDomainModel() = FoundBluetoothDevice(
        deviceName = this.foundDeviceName,
        deviceAdress = this.foundDeviceAdress
    )
}
