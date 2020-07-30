package eu.agribot.agribotapp.communication.bluetooth

import android.bluetooth.BluetoothAdapter

/**
 * Interface used to communicate with bluetooth
 * @see AndroidBluetoothService
 */
interface BluetoothService {
    val bluetoothAdapter: BluetoothAdapter
        get() = BluetoothAdapter.getDefaultAdapter()

    fun checkIsBluetoothEnable(): Boolean
    fun enableBluetooth()

    suspend fun registerBluetoothServer(isSecurityConnection: Boolean)
    suspend fun connectBluetoothWithAgribot(
        bluetoothDeviceAddress: String
    )

    suspend fun receiveBluetoothMessage(handleMessage: (message: ByteArray) -> Unit)
    fun writeBluetoothMessage(message: ByteArray)

    fun setConnectionState(isConnected: Boolean)
}
