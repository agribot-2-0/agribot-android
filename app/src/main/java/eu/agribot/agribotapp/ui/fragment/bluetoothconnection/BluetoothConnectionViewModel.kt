package eu.agribot.agribotapp.ui.fragment.bluetoothconnection

import android.bluetooth.BluetoothDevice
import android.content.Intent
import android.os.Handler
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import eu.agribot.agribotapp.base.BaseViewModel
import eu.agribot.agribotapp.common.livedata.LiveEvent
import eu.agribot.agribotapp.common.livedata.MutableLiveEvent
import eu.agribot.agribotapp.common.navigation.events.BluetoothConnectionNavigationEvent
import eu.agribot.agribotapp.domain.service.BluetoothUtils
import eu.agribot.agribotapp.domain.service.ManualModeService
import eu.agribot.agribotapp.ui.fragment.bluetoothconnection.recycler.BluetoothNearbyListItemViewModel
import kotlinx.coroutines.launch

class BluetoothConnectionViewModel(
    private val bluetoothUtils: BluetoothUtils,
    private val manualModeService: ManualModeService
) :
    BaseViewModel() {

    companion object {
        private const val HEARTBEAT_DELAY: Long = 300
    }

    val showSnackEvent: LiveEvent<String> = MutableLiveEvent()
    val discoveredDevices: MutableLiveData<List<BluetoothNearbyListItemViewModel>> =
        MutableLiveData(listOf())

    private fun onBluetoothDeviceFound(deviceName: String, deviceAdress: String) {
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

    fun handleBluetoothAction(intent: Intent) {
        val action = intent.action
        if (BluetoothDevice.ACTION_FOUND == action) {
            val device = intent.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)
            if (device != null) {
                if (device.name != null) {
                    onBluetoothDeviceFound(device.name, device.address)
                } else {
                    onBluetoothDeviceFound("No name", device.address)
                }
            } else {
                showSnackEvent.postValue("No Bluetooth Device Avaliable")
            }
        }
    }

    fun connectWithSelectedDevice(
        bluetoothDeviceAddress: String
    ) {
        viewModelScope.launch {
            bluetoothUtils.connect(
                bluetoothDeviceAddress
            )
        }
    }

    fun registerMessageListener() {
        viewModelScope.launch {
            bluetoothUtils.getMessage()
        }
    }

    fun initializeHeartbeatThread() {
        bluetoothUtils.initHeartbeat()
    }

    fun sendHandshake() {
        bluetoothUtils.sendHandshake()
        Handler().postDelayed({
            startSendingHeartBeat()
        }, HEARTBEAT_DELAY)
        manualControlNavEvent()
    }

    private fun startSendingHeartBeat() {
        bluetoothUtils.startSendingHeartBeat()
        setConnected()
    }

    private fun manualControlNavEvent() {
        navigationEvent.postValue(
            BluetoothConnectionNavigationEvent()
        )
        sendManualInit()
    }

    fun sendManualInit() {
        Handler().postDelayed({
            manualModeService.sendManualControlInitRequest()
        }, HEARTBEAT_DELAY)
    }

    fun setConnected() {
        bluetoothUtils.isAgribotConnect = true
    }

    fun setDisconnected() {
        bluetoothUtils.isAgribotConnect = false
    }
}
