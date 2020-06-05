package eu.agribot.agribotapp.ui.activity.main

import eu.agribot.agribotapp.base.BaseViewModel
import eu.agribot.agribotapp.domain.service.BluetoothConnectionService

class MainViewModel(private val bluetoothConnectionService: BluetoothConnectionService) : BaseViewModel(){

    fun checkBluetoothEnable(showDialog: () -> Unit) {
        when (bluetoothConnectionService.checkIsBluetoothAvaliable()) {
            false -> showDialog.invoke()
        }

    }


    fun enableBluetooth() {
        bluetoothConnectionService.enableBluetooth()
    }
}
