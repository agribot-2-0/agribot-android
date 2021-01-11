package eu.agribot.agribotapp.ui.fragment.ttcontrol

import androidx.lifecycle.MutableLiveData
import eu.agribot.agribotapp.base.BaseViewModel
import eu.agribot.agribotapp.common.livedata.MutableLiveEvent
import eu.agribot.agribotapp.domain.service.ManualModeService
import eu.agribot.agribotapp.domain.service.TTControlService
import eu.agribot.agribotapp.ui.fragment.toolcontrol.data.RecyclerConstant.FIFTH_ELEMENT
import eu.agribot.agribotapp.ui.fragment.toolcontrol.data.RecyclerConstant.NINTH_ELEMENT

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
        if (getJoysticVerticalMessageCounter() % FIFTH_ELEMENT == 0) {
            ttControlService.onVerticalJoystickParametrsChanged(
                    verticalAngle,
                    frontKart.value!!, rearKart.value!!
            )
        }
    }

    fun onHorizontalJoystickParametersChanged(horizontalAngle: Int) {
        if (getJoystickHorizontalMessageCounter() % FIFTH_ELEMENT == 0) {
            ttControlService.onHorizontalJoystickParametrsChanged(
                    horizontalAngle,
                    frontKart.value!!, rearKart.value!!
            )
        }
    }

    private fun getJoysticVerticalMessageCounter(): Int {
        if (verticalJoystickMessageCounter.value == null || verticalJoystickMessageCounter.value == NINTH_ELEMENT) {
            verticalJoystickMessageCounter.value = 0
        } else {
            verticalJoystickMessageCounter.value = verticalJoystickMessageCounter.value!! + 1
        }
        return verticalJoystickMessageCounter.value!!
    }

    private fun getJoystickHorizontalMessageCounter(): Int {
        if (horizontalJoystickMessageCounter.value == null || horizontalJoystickMessageCounter.value == NINTH_ELEMENT) {
            horizontalJoystickMessageCounter.value = 0
        } else {
            horizontalJoystickMessageCounter.value =
                    horizontalJoystickMessageCounter.value!! + 1
        }
        return horizontalJoystickMessageCounter.value!!
    }
}
