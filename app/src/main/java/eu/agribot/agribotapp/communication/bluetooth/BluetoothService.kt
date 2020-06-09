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
}
