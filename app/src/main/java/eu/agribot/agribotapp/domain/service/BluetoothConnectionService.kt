package eu.agribot.agribotapp.domain.service

import eu.agribot.agribotapp.communication.bluetooth.BluetoothService

/**
 * Domain Service used to basic bluetooth operations
 */

class BluetoothConnectionService(val bluetoothService: BluetoothService) {

    fun checkBluetoothEnable(): Boolean {
        return bluetoothService.checkIsBluetoothEnable()
    }

    fun enableBluetooth() {
        bluetoothService.enableBluetooth()
    }

    suspend fun runServer(isSecurityConnection: Boolean) {
        bluetoothService.registerBluetoothServer(isSecurityConnection)
    }

    suspend fun connect(bluetoothDeviceAddress: String) {
        bluetoothService.connectBluetoothWithAgribot(bluetoothDeviceAddress)
    }

    fun disconnect() {
        bluetoothService.disconnect()
    }

    fun writeMessage(byteMessage: ByteArray) {
        bluetoothService.writeBluetoothMessage(byteMessage)
    }

    suspend fun getMessage(handleMessage: (ByteArray) -> Unit) {
        bluetoothService.receiveBluetoothMessage(handleMessage)
    }
}
