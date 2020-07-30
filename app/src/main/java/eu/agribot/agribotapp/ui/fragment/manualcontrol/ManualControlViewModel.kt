package eu.agribot.agribotapp.ui.fragment.manualcontrol

import android.os.Handler
import android.util.Log
import androidx.lifecycle.MutableLiveData
import eu.agribot.agribotapp.base.BaseViewModel
import eu.agribot.agribotapp.common.navigation.events.BluetoothConnectionNavigationEvent
import eu.agribot.agribotapp.common.navigation.events.ToolControlNavigationEvent
import eu.agribot.agribotapp.domain.service.BluetoothUtils
import eu.agribot.agribotapp.domain.service.ManualModeService
import eu.agribot.agribotapp.domain.service.RemoteControlService
import timber.log.Timber

class ManualControlViewModel(
    private val remoteControlService: RemoteControlService,
    private val bluetoothUtils: BluetoothUtils,
    private val manualModeService: ManualModeService
) : BaseViewModel() {

    val heartBeatState: MutableLiveData<Boolean> = MutableLiveData()

    var connectionStatus: String = if (bluetoothUtils.connectionState) {
        "Agribot  jest połączony"
    } else {
        "Agribot nie jest połączony - Kliknij, aby połączyć"
    }

    fun onJoystickParametrsChanged(angle: Int, strength: Int) {
        manualModeService.onJoystickConfigurationChanged(angle, strength)
    }

    fun onToolControlClicked() {
        navigationEvent.postValue(
            ToolControlNavigationEvent()
        )
    }

    fun manualControlNavEvent() {
        if (!bluetoothUtils.connectionState) {
            navigationEvent.postValue(
                BluetoothConnectionNavigationEvent()
            )
        } else {
            Timber.e("Couldn't manualControlNavEvent")
        }
    }

    fun sendEngineStopCommand() {
        if (bluetoothUtils.isAgribotConnect) {
            manualModeService.stopAgribotEngine()
            manualModeService.runAgribotEngine()
        } else {
            Timber.e("Agribot Disconnected")
        }
    }

    fun sendManualSteeringInitRequest() {
        if (bluetoothUtils.isAgribotConnect) {
            manualModeService.sendManualControlInitRequest()
        } else {
            Timber.e("Agribot Disconnected")
        }
    }

    fun sendManualSteeringStartFrame() {
        if (bluetoothUtils.isAgribotConnect) {
            manualModeService.sendStartManualControlFrame()
        } else {
            Timber.e("Agribot Disconnected")
        }
    }

    fun startWrongHeartbeatIcon() {
        bluetoothUtils.setWrongHeartbeat()
    }

    fun setHeartbeatPositiveColor() {
        val handler = Handler()
        handler.postDelayed(object : Runnable {
            override fun run() {
                Log.e("color", bluetoothUtils.heartbeatState.toString())
                if (bluetoothUtils.heartbeatState) {
                    heartBeatState.postValue(true)
                } else if (!bluetoothUtils.heartbeatState) {
                    heartBeatState.postValue(false)
                }
                handler.postDelayed(this, 50) // 1 sec delay
            }
        }, 0)
    }
}
