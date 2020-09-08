package eu.agribot.agribotapp.ui.fragment.manualcontrol

import androidx.lifecycle.viewModelScope
import eu.agribot.agribotapp.base.BaseViewModel
import eu.agribot.agribotapp.common.navigation.events.BluetoothConnectionNavigationEvent
import eu.agribot.agribotapp.common.navigation.events.OverrideNavigationEvent
import eu.agribot.agribotapp.common.navigation.events.ToolControlNavigationEvent
import eu.agribot.agribotapp.domain.service.ManualModeService
import eu.agribot.agribotapp.domain.service.RemoteControlService
import kotlinx.coroutines.launch

class ManualControlViewModel(
    private val remoteControlService: RemoteControlService,
    private val manualModeService: ManualModeService
) : BaseViewModel() {
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

    fun onJoystickParametrChange(angel: Int, strength: Int) {
        manualModeService.onManualSteeringJoystickParametrChanged(angel, strength)
    }

    fun runManualSteeringMode() = viewModelScope.launch {
        manualModeService.runManualMode()
    }

    fun startStopEngine() {
        manualModeService.startStopEngine()
    }

    fun onTTControlButtonClicked() {
        navigationEvent.postValue(
            OverrideNavigationEvent()
        )
    }


}
