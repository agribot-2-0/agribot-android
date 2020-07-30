package eu.agribot.agribotapp.domain.service

import eu.agribot.agribotapp.domain.model.BluetoothAdressCodeModel
import eu.agribot.agribotapp.domain.model.BluetoothMessageModel

class ManualModeService(private val bluetoothUtils: BluetoothUtils) {

    private val singleArray = ByteArray(1) { 255.toByte() }

    fun sendManualControlInitRequest() {
        bluetoothUtils.writeMessage(
            BluetoothMessageModel(
                BluetoothMessageModel.CMD_ONE, BluetoothAdressCodeModel.MANUAL_STARTING_INIT,
                ByteArray(1)
            ).byteArray
        )
    }

    fun sendStartManualControlFrame() {
        bluetoothUtils.writeMessage(
            BluetoothMessageModel(
                BluetoothMessageModel.CMD_ONE, BluetoothAdressCodeModel.MANUAL_START,
                ByteArray(1)
            ).byteArray
        )
    }

    fun runAgribotEngine() {
        bluetoothUtils.writeMessage(
            BluetoothMessageModel(
                BluetoothMessageModel.CMD_ONE, BluetoothAdressCodeModel.START_ENGINE, singleArray
            ).byteArray
        )
    }

    fun stopAgribotEngine() {
        bluetoothUtils.writeMessage(
            BluetoothMessageModel(
                BluetoothMessageModel.CMD_ONE, BluetoothAdressCodeModel.STOP_SIGNAL, singleArray
            ).byteArray
        )
    }
}
