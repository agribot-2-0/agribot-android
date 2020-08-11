package eu.agribot.agribotapp.domain.service

import eu.agribot.agribotapp.domain.model.BluetoothAdressCodeModel
import eu.agribot.agribotapp.domain.model.BluetoothMessageModel

class AutonomusModeService(private val bluetoothUtils: BluetoothUtils) {

    fun runAutonomusMode(onDisconnected: () -> Unit) {
        if (bluetoothUtils.isAgribotConnect) {
            sendAutonomusInitMessage()
        } else {
            onDisconnected.invoke()
        }
    }

    private fun sendAutonomusInitMessage() {
        val initializationMessage = BluetoothMessageModel(
            BluetoothMessageModel.CMD_ONE,
            BluetoothAdressCodeModel.AUTO_START,
            ByteArray(1)
        ).byteArray

        bluetoothUtils.writeMessage(initializationMessage)
    }
}
