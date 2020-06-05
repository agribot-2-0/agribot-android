package eu.agribot.agribotapp.communication.bluetooth

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice

class AndroidBluetoothService : BluetoothService{

    private var discoveredDevices: MutableList<BluetoothDevice> = mutableListOf()


    override fun checkIsBluetoothEnable(): Boolean = when {
        bluetoothAdapter == null -> false
        !bluetoothAdapter.isEnabled -> false
        else -> true
    }

    override fun enableBluetooth() {
        bluetoothAdapter.enable()
    }



}
