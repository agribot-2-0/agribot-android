package eu.agribot.agribotapp.domain.service

import eu.agribot.agribotapp.communication.bluetooth.message.writemessage.WriteBluetoothMessageManager

class AutonomySevice(private val bluetoothMessageManager: WriteBluetoothMessageManager) {

    fun sendInitAutonomyMode() {
        bluetoothMessageManager.sendAutonomusModeInitMessage()
    }
}
