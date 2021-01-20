package eu.agribot.agribotapp.communication.bluetooth.basic.message.receivedmessage

import eu.agribot.agribotapp.communication.bluetooth.model.fromcommunication.DiagnosticBluetoothDataBD
import kotlinx.coroutines.flow.MutableStateFlow

interface ReceivedBluetoothMessageManager {
    fun registerMessageReceiver()
    fun observeDiagnosticData(): MutableStateFlow<DiagnosticBluetoothDataBD?>?
}
