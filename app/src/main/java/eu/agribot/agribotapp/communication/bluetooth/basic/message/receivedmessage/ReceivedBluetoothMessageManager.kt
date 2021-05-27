package eu.agribot.agribotapp.communication.bluetooth.basic.message.receivedmessage

import eu.agribot.agribotapp.communication.bluetooth.model.fromcommunication.DiagnosticBluetoothDataBD
import eu.agribot.agribotapp.communication.bluetooth.model.fromcommunication.RfidDataBD
import eu.agribot.agribotapp.communication.bluetooth.model.fromcommunication.ToolFeedbackDataBD
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow

interface ReceivedBluetoothMessageManager {

    fun registerMessageReceiver()

    @ExperimentalCoroutinesApi
    fun observeRfidData(): MutableStateFlow<RfidDataBD?>

    fun clearRfidDataFlow()

    @ExperimentalCoroutinesApi
    fun observeDiagnosticData(): MutableStateFlow<DiagnosticBluetoothDataBD?>?

    @ExperimentalCoroutinesApi
    fun observeToolFeedback(): MutableStateFlow<ToolFeedbackDataBD?>
}
