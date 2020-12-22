package eu.agribot.agribotapp.ui.fragment.manualcontrol

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import eu.agribot.agribotapp.R
import eu.agribot.agribotapp.base.BaseViewModel
import eu.agribot.agribotapp.common.navigation.events.TTControlNavigationEvent
import eu.agribot.agribotapp.common.navigation.events.ToolControlNavigationEvent
import eu.agribot.agribotapp.common.resources.ResourcesProvider
import eu.agribot.agribotapp.domain.service.ManualModeService
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ManualControlViewModel(
    private val manualModeService: ManualModeService,
    private val resourcesProvider: ResourcesProvider

) : BaseViewModel() {

    companion object {
        const val REFRESH_DIAGNOSTIC_DATA_DELAY = 100L
    }

    val accumulatorVoltage: MutableLiveData<String> = MutableLiveData()
    val engineRotations: MutableLiveData<String> = MutableLiveData()
    val torqueOfEngine: MutableLiveData<String> = MutableLiveData()
    val engineFuelRate: MutableLiveData<String> = MutableLiveData()
    val fuelLevel: MutableLiveData<String> = MutableLiveData()
    val fuelTemperature: MutableLiveData<String> = MutableLiveData()
    private val joystickMessageCounter: MutableLiveData<Int> = MutableLiveData()

    fun onToolControlClicked() {
        navigationEvent.postValue(
            ToolControlNavigationEvent()
        )
    }

    private fun refreshDiagnosticData() = viewModelScope.launch {
        while (true) {
            delay(REFRESH_DIAGNOSTIC_DATA_DELAY)
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

    fun onJoystickParametrChange(angel: Int, strength: Int) = viewModelScope.launch {
        if (getJoystickMessageCounter() % 5 == 0) {
            manualModeService.onManualSteeringJoystickParametrChanged(angel, strength)
        }
    }

    private fun getJoystickMessageCounter(): Int {
        if (joystickMessageCounter.value == null || joystickMessageCounter.value == 4) {
            joystickMessageCounter.value = 0
        } else {
            joystickMessageCounter.value = joystickMessageCounter.value!! + 1
        }
        return joystickMessageCounter.value!!
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
