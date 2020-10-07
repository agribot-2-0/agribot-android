package eu.agribot.agribotapp.communication.bluetooth.basic.connection.socket

import android.bluetooth.BluetoothSocket

interface BluetoothSocketManager {

    fun setBluetoothSocket(bluetoothSocket: BluetoothSocket)

    fun getBluetoothSocket(): BluetoothSocket?
}
