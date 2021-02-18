package eu.agribot.agribotapp.ui.fragment.manualcontrol

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import eu.agribot.agribotapp.R
import eu.agribot.agribotapp.base.BaseViewModel
import eu.agribot.agribotapp.common.livedata.MutableLiveEvent
import eu.agribot.agribotapp.common.navigation.events.TTControlNavigationEvent
import eu.agribot.agribotapp.common.navigation.events.ToolControlNavigationEvent
import eu.agribot.agribotapp.common.resources.ResourcesProvider
import eu.agribot.agribotapp.domain.service.AppStorageService
import eu.agribot.agribotapp.domain.service.InternetConnectionService
import eu.agribot.agribotapp.domain.service.ManualModeService
import eu.agribot.agribotapp.domain.service.joystick.JoysticFilter
import eu.agribot.agribotapp.ui.fragment.diagnosticdatarecycler.RobotStateViewModel
import eu.agribot.agribotapp.ui.fragment.diagnosticdatarecycler.items.RobotStateItems
import eu.agribot.agribotapp.ui.fragment.manualcontrol.recycler.actionbottombar.ActionID
import eu.agribot.agribotapp.ui.fragment.manualcontrol.recycler.actionbottombar.ManualActionsViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class ManualControlViewModel(
    private val manualModeService: ManualModeService
) : BaseViewModel() {

    companion object {
        const val COUNTER_INITIALIZATION_VALUE = 0
        const val COUNTER_INCREMENT_VALUE = 1
    }

    private val joystickMessageCounter: MutableLiveData<Int> = MutableLiveData()


    fun onToolControlClicked() {
        navigationEvent.postValue(
            ToolControlNavigationEvent()
        )
    }

    fun onJoystickParametrChange(angel: Int, strength: Int) = viewModelScope.launch {
        if (getJoystickMessageCounter() % JoysticFilter.JOYSTICK_FILTER_VALUE.value == COUNTER_INITIALIZATION_VALUE) {
            manualModeService.onManualSteeringJoystickParametrChanged(angel, strength)
        }
    }


    private fun getJoystickMessageCounter(): Int {
        if (joystickMessageCounter.value == null || joystickMessageCounter.value == JoysticFilter.JOYSTICK_COUNTER_MAX_VALUE.value) {
            joystickMessageCounter.value = COUNTER_INITIALIZATION_VALUE
        } else {
            joystickMessageCounter.value = joystickMessageCounter.value!! + COUNTER_INCREMENT_VALUE
        }
        return joystickMessageCounter.value!!
    }



}
