package eu.agribot.agribotapp.communication.bluetooth.basic.message.receivedmessage

import eu.agribot.agribotapp.communication.bluetooth.model.fromcommunication.DiagnosticBluetoothDataBD
import eu.agribot.agribotapp.communication.bluetooth.model.fromcommunication.RfidDataBD
import kotlinx.coroutines.flow.MutableStateFlow

interface ReceivedBluetoothMessageManager {
    fun registerMessageReceiver()
    fun getDiagnosticData(): DiagnosticBluetoothDataBD?
    fun observe(): MutableStateFlow<RfidDataBD?>
    fun clearRfidDataFlow()
}
