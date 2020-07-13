package eu.agribot.agribotapp.domain.service

import android.util.Log
import eu.agribot.agribotapp.communication.bluetooth.BluetoothService
import eu.agribot.agribotapp.domain.model.BluetoothCommunicationModel
import eu.agribot.agribotapp.domain.model.BluetoothCommunicationModel.Companion.MOBILE_HANDSHAKE

class BluetoothUtils(private val bluetoothService: BluetoothService) {

    fun checkBluetoothEnable() : Boolean {
        return bluetoothService.checkIsBluetoothEnable()
    }

    fun enableBluetooth() {
        bluetoothService.enableBluetooth()
    }

    suspend fun runServer(isSecurityConnection: Boolean) {
        bluetoothService.registerBluetoothServer(isSecurityConnection)
    }

    suspend fun connect(bluetoothDeviceAddress: String, onError: Unit) {
        bluetoothService.connectBluetoothWithAgribot(bluetoothDeviceAddress, onError)
    }

    suspend fun getMessage() {
        bluetoothService.receiveBluetoothMessage(handleMessage = this::handleMessage)
    }

    private fun handleMessage(message: String) {
        Log.e("xd", message)
    }

    fun writeMessage(byteMessage: ByteArray) {
        bluetoothService.writeBluetoothMessage(byteMessage)
    }


    fun sendHandshake() {
        val frame =
            BluetoothCommunicationModel(
                MOBILE_HANDSHAKE,
                ByteArray(1, { _ -> 1 })
            )
        frame.logBtFrame()
        Log.e("xd", "send handshake " + frame)
        writeMessage(frame.byteArray)
    }

}
