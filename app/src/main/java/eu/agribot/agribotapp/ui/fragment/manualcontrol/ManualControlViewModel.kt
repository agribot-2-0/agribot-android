package eu.agribot.agribotapp.ui.fragment.manualcontrol

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import eu.agribot.agribotapp.R
import eu.agribot.agribotapp.base.BaseViewModel
import eu.agribot.agribotapp.common.navigation.events.BluetoothConnectionNavigationEvent
import eu.agribot.agribotapp.common.navigation.events.TTControlNavigationEvent
import eu.agribot.agribotapp.common.navigation.events.ToolControlNavigationEvent
import eu.agribot.agribotapp.common.resources.ResourcesProvider
import eu.agribot.agribotapp.domain.service.ManualModeService
import eu.agribot.agribotapp.domain.service.RemoteControlService
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ManualControlViewModel(
    private val remoteControlService: RemoteControlService,
    private val manualModeService: ManualModeService,
    private val resourcesProvider: ResourcesProvider

) : BaseViewModel() {
    val accumulatorVoltage: MutableLiveData<String> = MutableLiveData()
    val engineRotations: MutableLiveData<String> = MutableLiveData()
    val torqueOfEngine: MutableLiveData<String> = MutableLiveData()
    val engineFuelRate: MutableLiveData<String> = MutableLiveData()
    val fuelLevel: MutableLiveData<String> = MutableLiveData()
    val fuelTemperature: MutableLiveData<String> = MutableLiveData()
    val REFRESH_DELAY = 100L

    fun onToolControlClicked() {
        navigationEvent.postValue(
            ToolControlNavigationEvent()
        )
    }

    private fun refreshDiagnosticData() = viewModelScope.launch {
        while (true) {
            delay(REFRESH_DELAY)
            if (manualModeService.getDiagnosticData() != null) {
                accumulatorVoltage.value = manualModeService.getDiagnosticData()?.batteryVoltage
                engineRotations.value =
                    manualModeService.getDiagnosticData()?.engineRotationsPerMinute.toString()
                torqueOfEngine.value =
                    manualModeService.getDiagnosticData()?.torqueOfEngine.toString()
                engineFuelRate.value =
                    manualModeService.getDiagnosticData()?.engineFuelRate.toString()
                fuelLevel.value = manualModeService.getDiagnosticData()?.fuelLevel.toString()
                fuelTemperature.value =
                    manualModeService.getDiagnosticData()?.fuelTemperature.toString()
            } else {
                accumulatorVoltage.value = resourcesProvider.getString(R.string.empty_data)
                engineRotations.value = resourcesProvider.getString(R.string.empty_data)
                torqueOfEngine.value = resourcesProvider.getString(R.string.empty_data)
                engineFuelRate.value = resourcesProvider.getString(R.string.empty_data)
                fuelLevel.value = resourcesProvider.getString(R.string.empty_data)
                fuelTemperature.value = resourcesProvider.getString(R.string.empty_data)
            }
        }
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
        refreshDiagnosticData()
    }

    fun startStopEngine() {
        manualModeService.startStopEngine()
    }

    fun onTTControlButtonClicked() {
        navigationEvent.postValue(
            TTControlNavigationEvent()
        )
    }
}
