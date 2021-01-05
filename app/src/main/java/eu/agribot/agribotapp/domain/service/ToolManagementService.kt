package eu.agribot.agribotapp.domain.service

import eu.agribot.agribotapp.communication.bluetooth.basic.message.writemessage.messagecreator.BluetoothMessageCreator
import eu.agribot.agribotapp.domain.model.fromdomain.RfidParameters

class ToolManagementService(private val bluetoothMessageCreator: BluetoothMessageCreator) {

    fun scanRfidTags(rfidParameters: RfidParameters) {
        bluetoothMessageCreator.createRfifRequestMessage(rfidParameters.toCommunicationModel())
    }
}