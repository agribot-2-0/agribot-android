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
        bluetoothDeviceAddress: String,
        onError: () -> Unit
    )

    suspend fun receiveBluetoothMessage(handleMessage: (message: String) -> Unit)
    fun writeBluetoothMessage(message: ByteArray)
}
