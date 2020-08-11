package eu.agribot.agribotapp.domain.service

import eu.agribot.agribotapp.domain.model.BluetoothAdressCodeModel
import eu.agribot.agribotapp.domain.model.BluetoothMessageModel

/**
 * Service used to manage agribot tools
 */

class ToolControlService(private val bluetoothUtils: BluetoothUtils) {
    private var switchesArray = ByteArray(8)

    fun writeSwitchesStatus(switchNumber: Int, value: Int, isActive: Boolean) {
        switchesArray[0] = switchNumber.toByte()
        switchesArray[1] = if (isActive) 1.toByte() else 0.toByte()
        switchesArray[2] = value.toByte()

        val switchesMessageFrame = BluetoothMessageModel(
            BluetoothMessageModel.CMD_ONE,
            BluetoothAdressCodeModel.AGRIBOT_SET_SWITCH,
            switchesArray
        ).byteArray
        bluetoothUtils.writeMessage(switchesMessageFrame)
    }
}
