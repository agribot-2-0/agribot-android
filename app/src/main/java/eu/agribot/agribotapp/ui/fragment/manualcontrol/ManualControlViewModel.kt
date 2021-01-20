package eu.agribot.agribotapp.ui.fragment.manualcontrol

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import eu.agribot.agribotapp.R
import eu.agribot.agribotapp.base.BaseViewModel
import eu.agribot.agribotapp.common.navigation.events.TTControlNavigationEvent
import eu.agribot.agribotapp.common.navigation.events.ToolControlNavigationEvent
import eu.agribot.agribotapp.common.resources.ResourcesProvider
import eu.agribot.agribotapp.domain.service.AppStorageService
import eu.agribot.agribotapp.domain.service.ManualModeService
import eu.agribot.agribotapp.domain.service.joystick.JoysticFilter
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class ManualControlViewModel(
    private val manualModeService: ManualModeService,
    private val resourcesProvider: ResourcesProvider,
    private val appStorageService: AppStorageService

) : BaseViewModel() {

    companion object {
        const val REFRESH_DIAGNOSTIC_DATA_DELAY = 100L
        const val SEND_JOYSTICK_MESSAGE_DELAY = 150L
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

    fun getStreamUrl(): String = appStorageService.getStreamUrl()


    fun onJoystickParametrChange(angel: Int, strength: Int) = viewModelScope.launch {
        if (getJoystickMessageCounter() % JoysticFilter.JOYSTICK_FILTER_VALUE.value == 0) {
            manualModeService.onManualSteeringJoystickParametrChanged(angel, strength)
        }
    }

    @ExperimentalCoroutinesApi
    fun setDiagnosticData() = viewModelScope.launch {
        manualModeService.observeDiagnosticData().collect { diagnosticData ->
            if (diagnosticData != null) {
                accumulatorVoltage.value = diagnosticData.batteryVoltage
                engineRotations.value = diagnosticData.engineRotationsPerMinute.toString()
                torqueOfEngine.value = diagnosticData.torqueOfEngine.toString()
                engineFuelRate.value = diagnosticData.engineFuelRate
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

    private fun getJoystickMessageCounter(): Int {
        if (joystickMessageCounter.value == null || joystickMessageCounter.value == JoysticFilter.JOYSTICK_COUNTER_MAX_VALUE.value) {
            joystickMessageCounter.value = 0
        } else {
            joystickMessageCounter.value = joystickMessageCounter.value!! + 1
        }
        return joystickMessageCounter.value!!
    }

    fun startStopEngine() = viewModelScope.launch {
        manualModeService.startStopEngine()
    }

    fun onTTControlButtonClicked() {
        navigationEvent.postValue(
            TTControlNavigationEvent()
        )
    }
}
