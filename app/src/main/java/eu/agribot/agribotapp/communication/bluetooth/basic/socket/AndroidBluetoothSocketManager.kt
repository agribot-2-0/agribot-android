package eu.agribot.agribotapp.communication.bluetooth.basic.connection.socket

import android.bluetooth.BluetoothSocket

class AndroidBluetoothSocketManager : BluetoothSocketManager {
    private var bluetoothSocket: BluetoothSocket? = null

    override fun setBluetoothSocket(bluetoothSocket: BluetoothSocket) {
        this.bluetoothSocket = bluetoothSocket
    }

    override fun getBluetoothSocket(): BluetoothSocket? {
        return bluetoothSocket
    }
}
