package eu.agribot.agribotapp.communication.bluetooth.model.fromcommunication

import eu.agribot.agribotapp.domain.model.todomain.BluetoothErrorInfo

class BluetoothErrorInfoBD(
    val bluetoothErrorCode: Int = -1
) : BaseCommunicationDataModel<BluetoothErrorInfo> {
    override fun toDomainModel() = BluetoothErrorInfo(
        bluetoothErrorCode = this.bluetoothErrorCode
    )
}
