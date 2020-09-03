package eu.agribot.agribotapp.ui.activity.main

import androidx.lifecycle.viewModelScope
import eu.agribot.agribotapp.base.BaseViewModel
import eu.agribot.agribotapp.domain.service.BluetoothConnectionService
import kotlinx.coroutines.launch

class MainViewModel(private val bluetoothConnectionService: BluetoothConnectionService) :
    BaseViewModel() {

    fun registerServer() {
        viewModelScope.launch {
            bluetoothConnectionService.runServer(true)
            bluetoothConnectionService.runServer(false)
        }
    }
}
