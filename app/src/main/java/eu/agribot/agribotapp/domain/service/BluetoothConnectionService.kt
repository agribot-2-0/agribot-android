package eu.agribot.agribotapp.domain.service

import eu.agribot.agribotapp.communication.bluetooth.BluetoothService

class BluetoothConnectionService(val bluetoothService: BluetoothService) {
    val bluetoothAdapter = bluetoothService.bluetoothAdapter
    fun checkIsBluetoothAvaliable() = bluetoothService.checkIsBluetoothEnable()


    fun enableBluetooth() {
        bluetoothService.enableBluetooth()
    }
}