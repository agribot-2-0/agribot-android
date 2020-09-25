package eu.agribot.agribotapp.domain.service

class AutonomyService(private val bluetoothConnectionService: BluetoothConnectionService) {

    fun sendInitAutonomyMode() {
        bluetoothConnectionService.initAutonomyMode()
    }
}
