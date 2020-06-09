package eu.agribot.agribotapp.ui.fragment.bluetoothconnection

import androidx.lifecycle.MutableLiveData
import eu.agribot.agribotapp.base.BaseViewModel
import eu.agribot.agribotapp.ui.fragment.bluetoothconnection.recycler.BluetoothNearbyListItemViewModel

class BluetoothConnectionViewModel() :
    BaseViewModel() {

    val discoveredDevices: MutableLiveData<List<BluetoothNearbyListItemViewModel>> =
        MutableLiveData(listOf())

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
}
