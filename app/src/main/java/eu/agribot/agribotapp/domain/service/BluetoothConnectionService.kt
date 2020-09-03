package eu.agribot.agribotapp.domain.service

import android.util.Log
import eu.agribot.agribotapp.communication.bluetooth.connection.BluetoothConnectionManager

/**
 * Domain Service used to basic bluetooth operations
 */

class BluetoothConnectionService(
    val bluetoothConnectionManager: BluetoothConnectionManager
) {

    fun checkBluetoothEnable(): Boolean {
        return bluetoothConnectionManager.checkIsBluetoothEnable()
    }

    fun enableBluetooth() {
        bluetoothConnectionManager.enableBluetooth()
    }

    suspend fun runServer(isSecurityConnection: Boolean) {
        bluetoothConnectionManager.registerBluetoothServer(isSecurityConnection)
    }

    suspend fun connect(bluetoothDeviceAddress: String) {
        bluetoothConnectionManager.connectBluetoothWithAgribot(
            bluetoothDeviceAddress
        )
    }


    fun disconnect() {
        bluetoothConnectionManager.disconnect()
    }

}
