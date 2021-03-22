package eu.agribot.agribotapp.communication.bluetooth.basic.socket

import android.bluetooth.BluetoothSocket
import eu.agribot.agribotapp.communication.bluetooth.basic.connection.socket.BluetoothSocketManager

class AndroidBluetoothSocketManager : BluetoothSocketManager {
    private var bluetoothSocket: BluetoothSocket? = null

    override fun setBluetoothSocket(bluetoothSocket: BluetoothSocket) {
        this.bluetoothSocket = bluetoothSocket
    }

    override fun getBluetoothSocket(): BluetoothSocket? {
        return bluetoothSocket
    }
}
