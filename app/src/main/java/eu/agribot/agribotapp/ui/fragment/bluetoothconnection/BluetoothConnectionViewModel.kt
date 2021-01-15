package eu.agribot.agribotapp.ui.fragment.bluetoothconnection

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import eu.agribot.agribotapp.R
import eu.agribot.agribotapp.base.BaseViewModel
import eu.agribot.agribotapp.common.livedata.LiveEvent
import eu.agribot.agribotapp.common.livedata.MutableLiveEvent
import eu.agribot.agribotapp.common.navigation.events.ManualControlNavigationEvent
import eu.agribot.agribotapp.common.resources.ResourcesProvider
import eu.agribot.agribotapp.domain.model.todomain.FoundBluetoothDevice
import eu.agribot.agribotapp.domain.permission.PermissionError
import eu.agribot.agribotapp.domain.permission.PermissionManager
import eu.agribot.agribotapp.domain.service.BluetoothConnectionService
import eu.agribot.agribotapp.domain.service.ManualModeService
import eu.agribot.agribotapp.ui.fragment.bluetoothconnection.recycler.BluetoothNearbyListItemViewModel
import kotlinx.coroutines.launch

class BluetoothConnectionViewModel(
    private val bluetoothConnectionService: BluetoothConnectionService,
    private val resourcesProvider: ResourcesProvider,
    private val permissionManager: PermissionManager,
    private val manualModeService: ManualModeService
) :
        BaseViewModel() {

    val toastEvent: LiveEvent<String> = MutableLiveEvent()
    val discoveredDevices: MutableLiveData<List<BluetoothNearbyListItemViewModel>> =
            MutableLiveData(listOf())

    fun checkPermissions(onSucess: () -> Unit) {
        permissionManager.requestPermission(onSucess, onError = this::onPermissionError)
    }

    private fun onPermissionError(permissionError: PermissionError) {
        toastEvent.postValue(permissionError.name)
    }

    fun onBluetoothDeviceFound(bluetoothDevice: FoundBluetoothDevice) {
        val isDeviceAlreadyAdded = discoveredDevices.value!!.any {
            it.bluetoothAddress == bluetoothDevice.deviceAdress
        }
        if (!isDeviceAlreadyAdded) {
            discoveredDevices.value = listOf(
                    BluetoothNearbyListItemViewModel(
                            bluetoothDevice.deviceName,
                            bluetoothDevice.deviceAdress

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

    fun onBluetoothDeviceConnected() = viewModelScope.launch {
        toastEvent.postValue(resourcesProvider.getString(R.string.connected))
        postManualSteeringNavigationEvent()
    }

    fun onBluetoothDeviceDisconnected() {
        toastEvent.postValue(resourcesProvider.getString(R.string.disconnected))
    }

    private fun postManualSteeringNavigationEvent() {
        navigationEvent.postValue(
                ManualControlNavigationEvent()
        )
    }
}
