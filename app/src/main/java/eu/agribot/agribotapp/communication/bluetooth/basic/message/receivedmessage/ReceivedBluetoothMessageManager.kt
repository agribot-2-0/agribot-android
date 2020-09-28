package eu.agribot.agribotapp.communication.bluetooth.basic.message.receivedmessage

import eu.agribot.agribotapp.communication.bluetooth.model.fromcommunication.DiagnosticCommunicationDataBD

interface ReceivedBluetoothMessageManager {
    fun registerMessageReceiver()
    fun getDiagnosticData(): DiagnosticCommunicationDataBD?
}
