package eu.agribot.agribotapp.communication.bluetooth.message.receivedmessage

import android.bluetooth.BluetoothSocket
import eu.agribot.agribotapp.communication.bluetooth.model.fromcommunication.DiagnosticCommunicationDataBD

interface ReceivedBluetoothMessageManager {
    fun registerMessageReceiver(bluetoothSocket: BluetoothSocket?)
    fun getDiagnosticData(): DiagnosticCommunicationDataBD?
}
