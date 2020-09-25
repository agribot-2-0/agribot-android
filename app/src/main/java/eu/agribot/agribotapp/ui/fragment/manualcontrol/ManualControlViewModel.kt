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
    private val refreshDiagnosticDataDelay = 100L
    val sendJoysticMessageDelay = 150L

    fun onToolControlClicked() {
        navigationEvent.postValue(
            ToolControlNavigationEvent()
        )
    }

    private fun refreshDiagnosticData() = viewModelScope.launch {
        while (true) {
            delay(refreshDiagnosticDataDelay)
            if (manualModeService.getDiagnosticData() != null) {
                manualModeService.getDiagnosticData()?.let { diagnosticData ->
                    accumulatorVoltage.value = diagnosticData.batteryVoltage
                    engineRotations.value = diagnosticData.engineRotationsPerMinute.toString()
                    torqueOfEngine.value = diagnosticData.torqueOfEngine.toString()
                    engineFuelRate.value = diagnosticData.fuelLevel.toString()
                    fuelLevel.value = diagnosticData.fuelLevel.toString()
                    fuelTemperature.value = diagnosticData.fuelTemperature.toString()
                }
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

    fun onJoystickParametrChange(angel: Int, strength: Int) = viewModelScope.launch {
        delay(sendJoysticMessageDelay)
        manualModeService.onManualSteeringJoystickParametrChanged(angel, strength)
    }

    fun sendStartManualModeInfo() = viewModelScope.launch {
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
