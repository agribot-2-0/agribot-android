package eu.agribot.agribotapp.communication.bluetooth.model.fromcommunication

import eu.agribot.agribotapp.domain.model.todomain.FoundBluetoothDevice

data class FoundBluetoothDeviceBD(
    val foundDeviceName: String,
    val foundDeviceAdress: String
) : BaseBluetoothDataModel<FoundBluetoothDevice> {
    override fun toDomainModel() = FoundBluetoothDevice(
        deviceName = this.foundDeviceName,
        deviceAdress = this.foundDeviceAdress
    )
}
