package eu.agribot.agribotapp.ui.activity.main

import androidx.lifecycle.viewModelScope
import eu.agribot.agribotapp.base.BaseViewModel
import eu.agribot.agribotapp.common.livedata.MutableLiveEvent
import eu.agribot.agribotapp.domain.service.BluetoothConnectionService
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainViewModel(private val bluetoothConnectionService: BluetoothConnectionService) :
    BaseViewModel() {

    companion object {
        const val CHECK_BLUETOOTH_ERROR_DELAY = 10000L
    }

    val connectionErrors: MutableLiveEvent<String> =
        MutableLiveEvent()
    val writeMessageErrors: MutableLiveEvent<String> =
        MutableLiveEvent()
    val receiveMessageErrors: MutableLiveEvent<String> =
        MutableLiveEvent()

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
                    connectionErrors.postValue(bluetoothConnectionService.getBluetoothConnectionErrors())
                }
                bluetoothConnectionService.getBluetoothWriteMessageErrors() != null -> {
                    writeMessageErrors.postValue(bluetoothConnectionService.getBluetoothWriteMessageErrors())
                }
                bluetoothConnectionService.getBluetoothReceiveMessageErrors() != null -> {
                    receiveMessageErrors.postValue(bluetoothConnectionService.getBluetoothReceiveMessageErrors())
                }
            }
        }
    }
}
