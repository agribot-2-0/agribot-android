package eu.agribot.agribotapp.communication.bluetooth.basic.message.receivedmessage.rfid

import eu.agribot.agribotapp.communication.bluetooth.model.fromcommunication.RfidDataBD
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow

interface RfidMessageManager {

    fun handleRfidMessage(rfidMessage: ByteArray)

    @ExperimentalCoroutinesApi
    fun observeRfidMessage(): MutableStateFlow<RfidDataBD?>

    fun clearRfidDataFlow()
}
