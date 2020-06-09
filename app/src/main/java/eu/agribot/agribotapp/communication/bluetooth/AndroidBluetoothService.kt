package eu.agribot.agribotapp.communication.bluetooth

class AndroidBluetoothService : BluetoothService {

    override fun checkIsBluetoothEnable(): Boolean = when {
        bluetoothAdapter.isEnabled -> true
        else -> false
    }

    override fun enableBluetooth() {
        bluetoothAdapter.enable()
    }
}
