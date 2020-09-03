package eu.agribot.agribotapp.ui.fragment.toolcontrol

import androidx.lifecycle.MutableLiveData
import eu.agribot.agribotapp.base.BaseViewModel
import eu.agribot.agribotapp.common.livedata.MutableLiveEvent

class ToolControlViewModel(
) : BaseViewModel() {

    // Tools bar LiveData
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

    val showToastEvent: MutableLiveEvent<String> = MutableLiveEvent()



    fun setDefaultToolsValue() {
        onSliderParametrChange(1, 0)
        onSliderParametrChange(2, 0)
        onSliderParametrChange(3, 0)
        onSliderParametrChange(4, 0)
        onSliderParametrChange(5, 0)
        onSliderParametrChange(6, 0)
        onSliderParametrChange(7, 0)
        onSliderParametrChange(8, 0)
    }

    fun onSliderParametrChange(sliderNumber: Int, sliderValue: Int) {
        when (sliderNumber) {
            1 -> {
                leftPTO.value = "$sliderValue rpm"
                onToolsParametrValueChange(1, sliderValue, leftPTOSwitch)
            }
            2 -> {
                leftBlue.value = "$sliderValue rpm"
                onToolsParametrValueChange(4, sliderValue, leftBlueSwitch)
            }
            3 -> {
                leftYellow.value = "$sliderValue rpm"
                onToolsParametrValueChange(3, sliderValue, leftYellowSwitch)
            }
            4 -> {
                leftGreen.value = "$sliderValue rpm"
                onToolsParametrValueChange(2, sliderValue, leftGreenSwitch)
            }
            5 -> {
                rightPTO.value = "$sliderValue rpm"
                onToolsParametrValueChange(7, sliderValue, rightPTOSwitch)
            }
            6 -> {
                rightBlue.value = "$sliderValue rpm"
                onToolsParametrValueChange(10, sliderValue, rightBlueSwitch)
            }
            7 -> {
                rightYellow.value = "$sliderValue rpm"
                onToolsParametrValueChange(9, sliderValue, rightYellowSwitch)
            }
            8 -> {
                rightGreen.value = "$sliderValue rpm"
                onToolsParametrValueChange(8, sliderValue, rightGreenSwitch)
            }
        }
    }

    private fun onToolsParametrValueChange(
        switchNumber: Int,
        progres: Int,
        isChecked: Boolean
    ) {

    }
}
