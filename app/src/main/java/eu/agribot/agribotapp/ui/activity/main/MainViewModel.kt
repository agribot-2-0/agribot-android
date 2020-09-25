package eu.agribot.agribotapp.ui.activity.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import eu.agribot.agribotapp.base.BaseViewModel
import eu.agribot.agribotapp.domain.service.BluetoothConnectionService
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainViewModel(private val bluetoothConnectionService: BluetoothConnectionService) :
    BaseViewModel() {
    val connectionErrors: MutableLiveData<String> =
        MutableLiveData()
    val writeMessageErrors: MutableLiveData<String> =
        MutableLiveData()
    val receiveMessageErrors: MutableLiveData<String> =
        MutableLiveData()
    val checkErrorDelay = 10000L

    fun registerServer() {
        viewModelScope.launch {
            bluetoothConnectionService.runServer(true)
            bluetoothConnectionService.runServer(false)
        }
    }

    fun refreshBluetoothErrorsList() = viewModelScope.launch {
        while (true) {
            delay(checkErrorDelay)
            if (bluetoothConnectionService.getBluetoothConnectionErrors() != null) {
                connectionErrors.value = bluetoothConnectionService.getBluetoothConnectionErrors()
            } else if (bluetoothConnectionService.getBluetoothWriteMessageErrors()
                != null
            ) {
                writeMessageErrors.value =
                    bluetoothConnectionService.getBluetoothWriteMessageErrors()
            } else if (bluetoothConnectionService.getBluetoothReceiveMessageErrors()
                != null
            ) {
                receiveMessageErrors.value =
                    bluetoothConnectionService.getBluetoothReceiveMessageErrors()
            }
        }
    }
}
