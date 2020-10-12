package eu.agribot.agribotapp.ui.activity.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import eu.agribot.agribotapp.base.BaseViewModel
import eu.agribot.agribotapp.domain.service.BluetoothConnectionService
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainViewModel(private val bluetoothConnectionService: BluetoothConnectionService) :
    BaseViewModel() {

    companion object {
        const val CHECK_BLUETOOTH_ERROR_DELAY = 10000L
    }

    val connectionErrors: MutableLiveData<String> =
        MutableLiveData()
    val writeMessageErrors: MutableLiveData<String> =
        MutableLiveData()
    val receiveMessageErrors: MutableLiveData<String> =
        MutableLiveData()

    fun registerServer() {
        viewModelScope.launch {
            bluetoothConnectionService.runServer(true)
            bluetoothConnectionService.runServer(false)
        }
    }

    fun refreshBluetoothErrorsList() = viewModelScope.launch {
        while (bluetoothConnectionService.checkBluetoothEnable()) {
            delay(CHECK_BLUETOOTH_ERROR_DELAY)
            when {
                bluetoothConnectionService.getBluetoothConnectionErrors() != null -> {
                    connectionErrors.value =
                        bluetoothConnectionService.getBluetoothConnectionErrors()
                }
                bluetoothConnectionService.getBluetoothWriteMessageErrors() != null -> {
                    writeMessageErrors.value =
                        bluetoothConnectionService.getBluetoothWriteMessageErrors()
                }
                bluetoothConnectionService.getBluetoothReceiveMessageErrors() != null -> {
                    receiveMessageErrors.value =
                        bluetoothConnectionService.getBluetoothReceiveMessageErrors()
                }
            }
        }
    }
}
