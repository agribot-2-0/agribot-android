package eu.agribot.agribotapp.communication.bluetooth.message.receivedmessage

import android.bluetooth.BluetoothSocket
import eu.agribot.agribotapp.communication.bluetooth.model.DiagnosticDataBD

interface ReceivedBluetoothMessageManager {
    fun registerMessageReceiver(bluetoothSocket: BluetoothSocket?)
}