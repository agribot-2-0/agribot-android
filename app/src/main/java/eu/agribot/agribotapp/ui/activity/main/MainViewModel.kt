package eu.agribot.agribotapp.ui.activity.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import eu.agribot.agribotapp.base.BaseViewModel
import eu.agribot.agribotapp.domain.service.BluetoothConnectionService
import kotlinx.coroutines.launch

class MainViewModel(private val bluetoothConnectionService: BluetoothConnectionService) :
    BaseViewModel() {
    val connectionErrors: MutableLiveData<String> =
        bluetoothConnectionService.observeBluetoothConnectionErrors()
    val writeMessageErrors: MutableLiveData<String> =
        bluetoothConnectionService.observeBluetoothWriteMessageErrors()
    val receiveMessageErrors: MutableLiveData<String> =
        bluetoothConnectionService.observeBluetoothReceiveMessageErrors()

    fun registerServer() {
        viewModelScope.launch {
            bluetoothConnectionService.runServer(true)
            bluetoothConnectionService.runServer(false)
        }
    }
}
