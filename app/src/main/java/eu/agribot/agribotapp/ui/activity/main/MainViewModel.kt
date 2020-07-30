package eu.agribot.agribotapp.ui.activity.main

import androidx.lifecycle.viewModelScope
import eu.agribot.agribotapp.base.BaseViewModel
import eu.agribot.agribotapp.domain.service.BluetoothUtils
import kotlinx.coroutines.launch

class MainViewModel(private val bluetoothUtils: BluetoothUtils) : BaseViewModel() {

    fun checkBluetoothEnable(showDialog: () -> Unit) {
        when (bluetoothUtils.checkBluetoothEnable()) {
            false -> showDialog.invoke()
        }
    }

    fun enableBluetooth() {
        bluetoothUtils.enableBluetooth()
    }

    fun registerServer() {
        viewModelScope.launch {
            bluetoothUtils.runServer(true)
            bluetoothUtils.runServer(false)
        }
    }
}
