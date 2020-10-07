package eu.agribot.agribotapp.communication.bluetooth.basic.message.receivedmessage

import eu.agribot.agribotapp.communication.bluetooth.model.fromcommunication.DiagnosticBluetoothDataBD

interface ReceivedBluetoothMessageManager {
    fun registerMessageReceiver()
    fun getDiagnosticData(): DiagnosticBluetoothDataBD?
}
