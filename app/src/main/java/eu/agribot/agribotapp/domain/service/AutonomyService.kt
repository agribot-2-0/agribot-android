package eu.agribot.agribotapp.domain.service

import eu.agribot.agribotapp.communication.bluetooth.basic.connection.message.writemessage.WriteBluetoothMessageManager

class AutonomyService(private val bluetoothMessageManager: WriteBluetoothMessageManager) {

    fun sendInitAutonomyMode() {
        bluetoothMessageManager.sendAutonomusModeInitMessage()
    }
}
