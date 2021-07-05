package eu.agribot.agribotapp.ui.fragment.ttcontrol

import androidx.lifecycle.MutableLiveData
import eu.agribot.agribotapp.base.BaseViewModel
import eu.agribot.agribotapp.common.livedata.MutableLiveEvent
import eu.agribot.agribotapp.common.navigation.events.TTControlToolNavigationEvent
import eu.agribot.agribotapp.domain.service.TTControlService
import eu.agribot.agribotapp.domain.service.joystick.JoysticFilter

class TTControlViewModel(
    private val ttControlService: TTControlService
) : BaseViewModel() {
    val frontCart: MutableLiveData<Boolean> = MutableLiveEvent()
    val rearCart: MutableLiveData<Boolean> = MutableLiveEvent()
    private val verticalJoystickMessageCounter: MutableLiveData<Int> = MutableLiveData()
    private val horizontalJoystickMessageCounter: MutableLiveData<Int> = MutableLiveData()

    fun setDefaultCartState() {
        frontCart.value = false
        rearCart.value = false
    }

    fun onFrontCartClicked() {
        frontCart.value = frontCart.value != true
    }

    fun onRearCartClicked() {
        rearCart.value = rearCart.value != true
    }

    fun onVerticalJoystickParametersChanged(verticalAngle: Int) {
        if (getJoysticVerticalMessageCounter() % JoysticFilter.JOYSTICK_FILTER_VALUE.value == 0) {
            ttControlService.onVerticalJoystickParametrsChanged(
                verticalAngle,
                frontCart.value!!, rearCart.value!!
            )
        }
    }

    fun onHorizontalJoystickParametersChanged(horizontalAngle: Int) {
        if (getJoystickHorizontalMessageCounter() % JoysticFilter.JOYSTICK_FILTER_VALUE.value == 0) {
            ttControlService.onHorizontalJoystickParametrsChanged(
                horizontalAngle,
                frontCart.value!!, rearCart.value!!
            )
        }
    }

    private fun getJoysticVerticalMessageCounter(): Int {
        if (verticalJoystickMessageCounter.value == null || verticalJoystickMessageCounter.value == JoysticFilter.JOYSTICK_COUNTER_MAX_VALUE.value) {
            verticalJoystickMessageCounter.value = 0
        } else {
            verticalJoystickMessageCounter.value = verticalJoystickMessageCounter.value!! + 1
        }
        return verticalJoystickMessageCounter.value!!
    }

    private fun getJoystickHorizontalMessageCounter(): Int {
        if (horizontalJoystickMessageCounter.value == null || horizontalJoystickMessageCounter.value == JoysticFilter.JOYSTICK_COUNTER_MAX_VALUE.value) {
            horizontalJoystickMessageCounter.value = 0
        } else {
            horizontalJoystickMessageCounter.value =
                horizontalJoystickMessageCounter.value!! + 1
        }
        return horizontalJoystickMessageCounter.value!!
    }

    fun toolControlClicked() {
        navigationEvent.postValue(TTControlToolNavigationEvent())
    }
}
