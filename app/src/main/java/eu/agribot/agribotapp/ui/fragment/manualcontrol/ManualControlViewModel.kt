package eu.agribot.agribotapp.ui.fragment.manualcontrol

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import eu.agribot.agribotapp.R
import eu.agribot.agribotapp.base.BaseViewModel
import eu.agribot.agribotapp.common.navigation.events.TTControlNavigationEvent
import eu.agribot.agribotapp.common.navigation.events.ToolControlNavigationEvent
import eu.agribot.agribotapp.common.resources.ResourcesProvider
import eu.agribot.agribotapp.domain.service.ManualModeService
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class ManualControlViewModel(
    private val manualModeService: ManualModeService,
    private val resourcesProvider: ResourcesProvider

) : BaseViewModel() {

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

    fun setDiagnosticData() = viewModelScope.launch {
        manualModeService.observeDiagnosticData().collect { diagnosticData ->
            if (diagnosticData != null) {
                accumulatorVoltage.value = diagnosticData.batteryVoltage
                engineRotations.value = diagnosticData.engineRotationsPerMinute.toString()
                torqueOfEngine.value = diagnosticData.torqueOfEngine.toString()
                engineFuelRate.value = diagnosticData.fuelLevel.toString()
                fuelLevel.value = diagnosticData.fuelLevel.toString()
                fuelTemperature.value = diagnosticData.fuelTemperature.toString()
            } else {
                resourcesProvider.getString(R.string.no_data_status).let { noData ->
                    accumulatorVoltage.value = noData
                    engineRotations.value = noData
                    torqueOfEngine.value = noData
                    engineFuelRate.value = noData
                    fuelLevel.value = noData
                    fuelTemperature.value = noData
                }
            }
        }
    }

    fun onJoystickParametrChange(angel: Int, strength: Int) = viewModelScope.launch {
        if (getJoystickMessageCounter() % 5 == 0) {
            manualModeService.onManualSteeringJoystickParametrChanged(angel, strength)
        }
    }

    private fun getJoystickMessageCounter(): Int {
        if (joystickMessageCounter.value == null || joystickMessageCounter.value == 10) {
            joystickMessageCounter.value = 0
        } else {
            joystickMessageCounter.value = joystickMessageCounter.value!! + 1
        }
        return joystickMessageCounter.value!!
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
