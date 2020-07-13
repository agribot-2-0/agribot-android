package eu.agribot.agribotapp.ui.fragment.bluetoothconnection

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import eu.agribot.agribotapp.base.BaseViewModel
import eu.agribot.agribotapp.common.livedata.LiveEvent
import eu.agribot.agribotapp.common.livedata.MutableLiveEvent
import eu.agribot.agribotapp.domain.service.BluetoothUtils
import eu.agribot.agribotapp.ui.fragment.bluetoothconnection.recycler.BluetoothNearbyListItemViewModel
import kotlinx.coroutines.launch

class BluetoothConnectionViewModel(
    private val bluetoothUtils: BluetoothUtils
) :
    BaseViewModel() {

    val showSnackEvent: LiveEvent<String> = MutableLiveEvent()
    val discoveredDevices: MutableLiveData<List<BluetoothNearbyListItemViewModel>> =
        MutableLiveData(listOf())
    val connectionState: MutableLiveData<Boolean> = MutableLiveData()

    fun onBluetoothDeviceFound(deviceName: String, deviceAdress: String) {
        val isDeviceAlreadyAdded = discoveredDevices.value!!.any {
            it.bluetoothAddress == deviceAdress
        }
        if (!isDeviceAlreadyAdded) {
            discoveredDevices.value = listOf(
                BluetoothNearbyListItemViewModel(
                    deviceName,
                    deviceAdress
                ),
                *discoveredDevices.value!!.toTypedArray()
            )
        }
    }

    fun connectWithSelectedDevice(
        bluetoothDeviceAddress: String
    ) {
        viewModelScope.launch {
            bluetoothUtils.connect(
                bluetoothDeviceAddress,
                showSnackEvent.postValue("ConnectionError")
            )
        }
    }

    fun registerMessageListener() {
        viewModelScope.launch {
            bluetoothUtils.getMessage()
        }
    }

    fun sendHandshake() {
        bluetoothUtils.sendHandshake()
    }

}
