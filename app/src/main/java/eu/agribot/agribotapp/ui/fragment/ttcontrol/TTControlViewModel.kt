package eu.agribot.agribotapp.ui.fragment.ttcontrol

import androidx.lifecycle.MutableLiveData
import eu.agribot.agribotapp.base.BaseViewModel
import eu.agribot.agribotapp.common.livedata.MutableLiveEvent
import eu.agribot.agribotapp.domain.service.ManualModeService
import eu.agribot.agribotapp.domain.service.TTControlService

class TTControlViewModel(
    private val ttControlService: TTControlService,
    private val manualModeService: ManualModeService
) : BaseViewModel() {

    val frontKart: MutableLiveData<Boolean> = MutableLiveEvent()
    val rearKart: MutableLiveData<Boolean> = MutableLiveEvent()
    private val verticalJoystickMessageCounter: MutableLiveData<Int> = MutableLiveData()
    private val horizontalJoystickMessageCounter: MutableLiveData<Int> = MutableLiveData()

    fun setDefaultKartState() {
        frontKart.value = false
        rearKart.value = false
    }

    fun onFrontKartClicked() {
        frontKart.value = frontKart.value != true
    }

    fun onRearKartClicked() {
        rearKart.value = rearKart.value != true
    }

    fun onVerticalJoystickParametersChanged(verticalAngle: Int) {
        if (getJoysticVerticalMessageCounter() % 5 == 0) {
            ttControlService.onVerticalJoystickParametrsChanged(
                    verticalAngle,
                    frontKart.value!!, rearKart.value!!
            )
        }
    }

    fun onHorizontalJoystickParametersChanged(horizontalAngle: Int) {
        if (getJoystickHorizontalMessageCounter() % 5 == 0) {
            ttControlService.onHorizontalJoystickParametrsChanged(
                    horizontalAngle,
                    frontKart.value!!, rearKart.value!!
            )
        }
    }

    private fun getJoysticVerticalMessageCounter(): Int {
        if (verticalJoystickMessageCounter.value == null || verticalJoystickMessageCounter.value == 10) {
            verticalJoystickMessageCounter.value = 0
        } else {
            verticalJoystickMessageCounter.value = verticalJoystickMessageCounter.value!! + 1
        }
        return verticalJoystickMessageCounter.value!!
    }

    private fun getJoystickHorizontalMessageCounter(): Int {
        if (horizontalJoystickMessageCounter.value == null || horizontalJoystickMessageCounter.value == 10) {
            horizontalJoystickMessageCounter.value = 0
        } else {
            horizontalJoystickMessageCounter.value =
                    horizontalJoystickMessageCounter.value!! + 1
        }
        return horizontalJoystickMessageCounter.value!!
    }
}
