package eu.agribot.agribotapp.communication.bluetooth.message.receivedmessage

import android.bluetooth.BluetoothSocket

interface ReceivedBluetoothMessageManager {

     fun registerMessageReceiver(bluetoothSocket : BluetoothSocket?)
}