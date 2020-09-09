package eu.agribot.agribotapp.ui.fragment.toolcontrol

import androidx.lifecycle.MutableLiveData
import eu.agribot.agribotapp.base.BaseViewModel
import eu.agribot.agribotapp.domain.model.fromdomain.SwitchSettings
import eu.agribot.agribotapp.domain.service.ManualModeService
import eu.agribot.agribotapp.ui.fragment.toolcontrol.ToolsList.LEFT_BLUE
import eu.agribot.agribotapp.ui.fragment.toolcontrol.ToolsList.LEFT_DOWN_TUZ
import eu.agribot.agribotapp.ui.fragment.toolcontrol.ToolsList.LEFT_GREEN
import eu.agribot.agribotapp.ui.fragment.toolcontrol.ToolsList.LEFT_PTO
import eu.agribot.agribotapp.ui.fragment.toolcontrol.ToolsList.LEFT_UP_TUZ
import eu.agribot.agribotapp.ui.fragment.toolcontrol.ToolsList.LEFT_YELLOW
import eu.agribot.agribotapp.ui.fragment.toolcontrol.ToolsList.RIGHT_BLUE
import eu.agribot.agribotapp.ui.fragment.toolcontrol.ToolsList.RIGHT_DOWN_TUZ
import eu.agribot.agribotapp.ui.fragment.toolcontrol.ToolsList.RIGHT_GREEN
import eu.agribot.agribotapp.ui.fragment.toolcontrol.ToolsList.RIGHT_PTO
import eu.agribot.agribotapp.ui.fragment.toolcontrol.ToolsList.RIGHT_UP_TUZ
import eu.agribot.agribotapp.ui.fragment.toolcontrol.ToolsList.RIGHT_YELLOW

class ToolControlViewModel(
    private val manualModeService: ManualModeService
) : BaseViewModel() {

    val leftPTO: MutableLiveData<String> = MutableLiveData()
    val leftBlue: MutableLiveData<String> = MutableLiveData()
    val leftYellow: MutableLiveData<String> = MutableLiveData()
    val leftGreen: MutableLiveData<String> = MutableLiveData()
    val rightPTO: MutableLiveData<String> = MutableLiveData()
    val rightBlue: MutableLiveData<String> = MutableLiveData()
    val rightYellow: MutableLiveData<String> = MutableLiveData()
    val rightGreen: MutableLiveData<String> = MutableLiveData()

    // Tools Switch
    var leftPTOSwitch: Boolean = false
    var leftBlueSwitch: Boolean = false
    var leftYellowSwitch: Boolean = false
    var leftGreenSwitch: Boolean = false
    var rightPTOSwitch: Boolean = false
    var rightBlueSwitch: Boolean = false
    var rightYellowSwitch: Boolean = false
    var rightGreenSwitch: Boolean = false

    fun onLeftUpTuzChange(tuzProgress: Int) {
        manualModeService.onSwitchParametrChanged(SwitchSettings(LEFT_UP_TUZ, true, tuzProgress))
    }

    fun onLeftDownTuzChange(tuzProgress: Int) {
        manualModeService.onSwitchParametrChanged(SwitchSettings(LEFT_DOWN_TUZ, true, tuzProgress))
    }

    fun onRightUpTuzChange(tuzProgress: Int) {
        manualModeService.onSwitchParametrChanged(SwitchSettings(RIGHT_UP_TUZ, true, tuzProgress))
    }

    fun onRightDownTuzChange(tuzProgress: Int) {
        manualModeService.onSwitchParametrChanged(SwitchSettings(RIGHT_DOWN_TUZ, true, tuzProgress))
    }

    fun onLeftPTOChanged(toolProgress: Int) {
        leftPTO.value = "$toolProgress rpm"
        manualModeService.onSwitchParametrChanged(
            SwitchSettings(
                LEFT_PTO,
                leftPTOSwitch,
                toolProgress
            )
        )
    }

    fun onLeftBlueChanged(toolProgress: Int) {
        leftBlue.value = "$toolProgress rpm"
        manualModeService.onSwitchParametrChanged(
            SwitchSettings(
                LEFT_BLUE,
                leftBlueSwitch,
                toolProgress
            )
        )
    }

    fun onLeftYellowChanged(toolProgress: Int) {
        leftYellow.value = "$toolProgress rpm"
        manualModeService.onSwitchParametrChanged(
            SwitchSettings(
                LEFT_YELLOW,
                leftYellowSwitch,
                toolProgress
            )
        )
    }

    fun onLeftGreenChanged(toolProgress: Int) {
        leftGreen.value = "$toolProgress rpm"
        manualModeService.onSwitchParametrChanged(
            SwitchSettings(
                LEFT_GREEN,
                leftGreenSwitch,
                toolProgress
            )
        )
    }

    fun onRightPTOChanged(toolProgress: Int) {
        rightPTO.value = "$toolProgress rpm"
        manualModeService.onSwitchParametrChanged(
            SwitchSettings(
                RIGHT_PTO,
                rightPTOSwitch,
                toolProgress
            )
        )
    }

    fun onRightBlueChanged(toolProgress: Int) {
        rightBlue.value = "$toolProgress rpm"
        manualModeService.onSwitchParametrChanged(
            SwitchSettings(
                RIGHT_BLUE,
                rightBlueSwitch,
                toolProgress
            )
        )
    }

    fun onRightYellowChanged(toolProgress: Int) {
        rightYellow.value = "$toolProgress rpm"
        manualModeService.onSwitchParametrChanged(
            SwitchSettings(
                RIGHT_YELLOW,
                rightYellowSwitch,
                toolProgress
            )
        )
    }

    fun onRightGreenChanged(toolProgress: Int) {
        rightGreen.value = "$toolProgress rpm"
        manualModeService.onSwitchParametrChanged(
            SwitchSettings(
                RIGHT_GREEN,
                rightGreenSwitch,
                toolProgress
            )
        )
    }
}
