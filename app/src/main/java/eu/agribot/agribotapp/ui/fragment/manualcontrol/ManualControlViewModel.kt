package eu.agribot.agribotapp.ui.fragment.manualcontrol

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import eu.agribot.agribotapp.R
import eu.agribot.agribotapp.base.BaseViewModel
import eu.agribot.agribotapp.common.navigation.events.TTControlNavigationEvent
import eu.agribot.agribotapp.common.resources.ResourcesProvider
import eu.agribot.agribotapp.domain.service.ManualModeService
import eu.agribot.agribotapp.domain.service.SteeringModeConstant
import eu.agribot.agribotapp.domain.service.joystick.JoysticFilter
import eu.agribot.agribotapp.ui.fragment.manualcontrol.viewpager.PagerPostion
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class ManualControlViewModel(
    private val manualModeService: ManualModeService,
    private val resourcesProvider: ResourcesProvider
) : BaseViewModel() {
    private val joystickMessageCounter: MutableLiveData<Int> = MutableLiveData()
    val buttonText: MutableLiveData<String> =
        MutableLiveData(resourcesProvider.getString(R.string.title_tool_control))
    val pagerState: MutableLiveData<Int> = MutableLiveData(PagerPostion.DISPLAY_STATE.number)

    companion object {
        const val COUNTER_INITIALIZATION_VALUE = 0
        const val COUNTER_INCREMENT_VALUE = 1
    }

    fun onToolControlClicked() {
        if (pagerState.value == PagerPostion.DISPLAY_STATE.number) {
            pagerState.value = PagerPostion.TOOL_CONTROL.number
            buttonText.value = resourcesProvider.getString(R.string.overview_mode)
        } else {
            pagerState.value = PagerPostion.DISPLAY_STATE.number
            buttonText.value = resourcesProvider.getString(R.string.title_tool_control)
        }
    }

    fun onJoystickParametrChange(angle: Int, strength: Int) = viewModelScope.launch {
        if (getJoystickMessageCounter() % JoysticFilter.JOYSTICK_FILTER_VALUE.value == COUNTER_INITIALIZATION_VALUE) {
            manualModeService.onManualSteeringJoystickParametrChanged(angle, strength)
        }
    }

    fun onSteeringModeFrontWheelClicked() {
        manualModeService.sendSteeringModeFrame(SteeringModeConstant.FrontWheel)
    }

    fun onSteeringModeFourWheelClicked() {
        manualModeService.sendSteeringModeFrame(SteeringModeConstant.FourWheel)
    }

    fun onSteeringModeCrabWalkClicked() {
        manualModeService.sendSteeringModeFrame(SteeringModeConstant.CrabWalk)
    }

    private fun getJoystickMessageCounter(): Int {
        if (joystickMessageCounter.value == null || joystickMessageCounter.value == JoysticFilter.JOYSTICK_COUNTER_MAX_VALUE.value) {
            joystickMessageCounter.value = COUNTER_INITIALIZATION_VALUE
        } else {
            joystickMessageCounter.value = joystickMessageCounter.value!! + COUNTER_INCREMENT_VALUE
        }
        return joystickMessageCounter.value!!
    }

    @InternalCoroutinesApi
    fun observeoverrideMode() = viewModelScope.launch {
        manualModeService.observeTTControlFlow().collect {
            if (it) {
                navigationEvent.postValue(TTControlNavigationEvent())
                manualModeService.onTTControlEvent(false)
            }
        }
    }

    fun initManualMode() {
        manualModeService.sendManualModeInitMessage()
    }

    fun initSafeMode() {
        manualModeService.sendSafeModeMessage()
    }
}
