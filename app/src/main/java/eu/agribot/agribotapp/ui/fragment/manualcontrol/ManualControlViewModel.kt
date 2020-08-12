package eu.agribot.agribotapp.ui.fragment.manualcontrol

import android.os.Handler
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import eu.agribot.agribotapp.base.BaseViewModel
import eu.agribot.agribotapp.common.livedata.LiveEvent
import eu.agribot.agribotapp.common.livedata.MutableLiveEvent
import eu.agribot.agribotapp.common.navigation.events.BluetoothConnectionNavigationEvent
import eu.agribot.agribotapp.common.navigation.events.OverrideNavigationEvent
import eu.agribot.agribotapp.common.navigation.events.ToolControlNavigationEvent
import eu.agribot.agribotapp.domain.service.BluetoothUtils
import eu.agribot.agribotapp.domain.service.DiagnosticDataService
import eu.agribot.agribotapp.domain.service.ManualModeService
import eu.agribot.agribotapp.domain.service.RemoteControlService
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber

class ManualControlViewModel(
    private val remoteControlService: RemoteControlService,
    private val manualModeService: ManualModeService,
    private val bluetoothUtils: BluetoothUtils,
    diagnosticDataService: DiagnosticDataService
) : BaseViewModel() {
    companion object {
        private const val CHANGE_HEARTBEAT_COLOR_DEALAY = 50L
        private const val RESTART_HEARTBEAT_DELAY = 500L
    }

    // Status Info Live Data
    val engineRotationsPerMinute: MutableLiveData<String> =
        diagnosticDataService.engineRotationsPerMinute
    val torqueOfEngine: MutableLiveData<String> = diagnosticDataService.torqueOfEngine
    val engineFuelRate: MutableLiveData<String> = diagnosticDataService.engineFuelRate
    val fuelLevel: MutableLiveData<String> = diagnosticDataService.fuelLevel
    val fuelTemperature: MutableLiveData<String> = diagnosticDataService.fuelTemperature
    val batteryVoltage: MutableLiveData<String> = diagnosticDataService.batteryVoltage

    // ManualVm varriable
    val showToastEvent: LiveEvent<String> = MutableLiveEvent()
    val heartBeatState: MutableLiveData<Boolean> = MutableLiveData()
    var connectionStatus: String = if (bluetoothUtils.isAgribotConnect) {
        "Agribot Connected"
    } else {
        "Agribot Disconnected - tap to connect"
    }

    fun onJoystickParametrsChanged(angle: Int, strength: Int) {
        if (bluetoothUtils.isAgribotConnect) {
            manualModeService.onJoystickConfigurationChanged(angle, strength)
        } else {
            postValueAgribotDisconnected()
        }
    }

    private fun postValueAgribotDisconnected() {
        showToastEvent.postValue("Agribot Disconnected")
    }

    fun onToolControlClicked() {
        navigationEvent.postValue(
            ToolControlNavigationEvent()
        )
    }

    fun manualControlNavEvent() {
        navigationEvent.postValue(
            BluetoothConnectionNavigationEvent()
        )
    }

    fun sendEngineStopCommand() {
        if (bluetoothUtils.isAgribotConnect) {
            manualModeService.stopAgribotEngine()
            manualModeService.runAgribotEngine()
        } else {
            postValueAgribotDisconnected()
        }
    }

    fun sendManualSteeringInitRequest() {
        if (bluetoothUtils.isAgribotConnect) {
            manualModeService.sendManualControlInitRequest()
        } else {
            postValueAgribotDisconnected()
        }
    }

    fun sendManualSteeringStartFrame() {
        if (bluetoothUtils.isAgribotConnect) {
            manualModeService.sendStartManualControlFrame()
        } else {
            postValueAgribotDisconnected()
        }
    }

    fun startWrongHeartbeatIcon() {
        manualModeService.sendHeartbeatError()
    }

    fun setHeartbeatPositiveColor() {
        val handler = Handler()
        handler.postDelayed(object : Runnable {
            override fun run() {
                if (bluetoothUtils.heartbeatState) {
                    heartBeatState.postValue(true)
                } else if (!bluetoothUtils.heartbeatState) {
                    heartBeatState.postValue(false)
                }
                handler.postDelayed(this, CHANGE_HEARTBEAT_COLOR_DEALAY)
            }
        }, 0)
    }

    private fun sendHandshakeAndHeartbeat() = viewModelScope.launch {
        delay(RESTART_HEARTBEAT_DELAY)
        manualModeService.sendHanshakeAndHeartbeat()
    }

    fun stopAndRunHeartBeat() {
        if (bluetoothUtils.isAgribotConnect) {
            manualModeService.stopHeartbeat()
            sendHandshakeAndHeartbeat()
        } else {
            Timber.e("No connection")
        }
    }

    fun postValueOverrideMode() {
        if (bluetoothUtils.isAgribotConnect) {
            navigationEvent.postValue(
                OverrideNavigationEvent()
            )
        } else {
            showToastEvent.postValue("Agribot Disconnected")
        }
    }
}
