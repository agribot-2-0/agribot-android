package eu.agribot.agribotapp.communication.bluetooth.basic.connection

import android.bluetooth.BluetoothAdapter

/**
 * Interface used to communicate with bluetooth
 * @see AndroidBluetoothConnectionManager
 */
interface BluetoothConnectionManager {
    val bluetoothAdapter: BluetoothAdapter
        get() = BluetoothAdapter.getDefaultAdapter()

    fun checkIsBluetoothEnable(): Boolean

    fun enableBluetooth()

    suspend fun registerBluetoothServer(isSecureConnection: Boolean)

    suspend fun connectBluetoothWithAgribot(bluetoothDeviceAddress: String)

    fun disconnect()
}
