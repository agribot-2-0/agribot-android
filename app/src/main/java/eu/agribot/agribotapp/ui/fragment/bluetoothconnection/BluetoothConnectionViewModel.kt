package eu.agribot.agribotapp.ui.fragment.bluetoothconnection

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import eu.agribot.agribotapp.base.BaseViewModel
import eu.agribot.agribotapp.common.livedata.LiveEvent
import eu.agribot.agribotapp.common.livedata.MutableLiveEvent
import eu.agribot.agribotapp.communication.bluetooth.model.FoundBluetoothDevice
import eu.agribot.agribotapp.domain.service.BluetoothConnectionService
import eu.agribot.agribotapp.domain.service.ManualModeService
import eu.agribot.agribotapp.ui.fragment.bluetoothconnection.recycler.BluetoothNearbyListItemViewModel
import kotlinx.coroutines.launch

class BluetoothConnectionViewModel(
    private val bluetoothConnectionService: BluetoothConnectionService,
    private val manualModeService: ManualModeService
) :
    BaseViewModel() {

    val showSnackEvent: LiveEvent<String> = MutableLiveEvent()
    val discoveredDevices: MutableLiveData<List<BluetoothNearbyListItemViewModel>> =
        MutableLiveData(listOf())


    fun onBluetoothDeviceFound(bluetoothDevice: FoundBluetoothDevice) {
        val isDeviceAlreadyAdded = discoveredDevices.value!!.any {
            it.bluetoothAddress == bluetoothDevice.foundDeviceAdress
        }
        if (!isDeviceAlreadyAdded) {
            discoveredDevices.value = listOf(
                BluetoothNearbyListItemViewModel(
                    bluetoothDevice.foundDeviceName,
                    bluetoothDevice.foundDeviceAdress
                ),
                *discoveredDevices.value!!.toTypedArray()
            )
        }
    }

    fun connectWithSelectedDevice(
        bluetoothDeviceAddress: String
    ) {
        viewModelScope.launch {
            bluetoothConnectionService.connect(
                bluetoothDeviceAddress
            )
        }
    }

    fun onBluetoothDeviceConnected() {
        showSnackEvent.postValue("connected")

    }


    fun onBluetoothDeviceDisconnected() {
        Log.e("xd", "disconnected")
    }

}


