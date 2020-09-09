package eu.agribot.agribotapp.ui.fragment.ttcontrol

import androidx.lifecycle.MutableLiveData
import eu.agribot.agribotapp.base.BaseViewModel
import eu.agribot.agribotapp.common.livedata.MutableLiveEvent
import eu.agribot.agribotapp.domain.service.TTControlService

class TTControlViewModel(private val ttControlService: TTControlService) : BaseViewModel() {

    val frontKart: MutableLiveData<Boolean> = MutableLiveEvent()
    val rearKart: MutableLiveData<Boolean> = MutableLiveEvent()

    fun onFrontKartClicked() {
        frontKart.value = frontKart.value != true
    }

    fun onRearKartClicked() {
        rearKart.value = rearKart.value != true
    }

    fun onVerticalJoystickParametrsChanged(verticalAngle: Int) {
        ttControlService.onVerticalJoystickParametrsChanged(
            verticalAngle,
            frontKart.value!!, rearKart.value!!
        )
    }

    fun onHorizontalJoystickParametrsChanged(horizontalAngle: Int) {
        ttControlService.onHorizontalJoystickParametrsChanged(
            horizontalAngle,
            frontKart.value!!, rearKart.value!!
        )
    }
}
