package eu.agribot.agribotapp.ui.fragment.toolcontrol

import androidx.lifecycle.MutableLiveData
import eu.agribot.agribotapp.base.BaseViewModel
import eu.agribot.agribotapp.ui.fragment.toolcontrol.data.TuzSwitchesState
import eu.agribot.agribotapp.ui.fragment.toolcontrol.data.TuzSwitchessActive
import eu.agribot.agribotapp.domain.model.fromdomain.SwitchSettings
import eu.agribot.agribotapp.domain.service.ManualModeService
import eu.agribot.agribotapp.ui.fragment.toolcontrol.data.ToolsList.BACK_BLUE
import eu.agribot.agribotapp.ui.fragment.toolcontrol.data.ToolsList.BACK_DOWN_TUZ
import eu.agribot.agribotapp.ui.fragment.toolcontrol.data.ToolsList.BACK_GREEN
import eu.agribot.agribotapp.ui.fragment.toolcontrol.data.ToolsList.BACK_PTO
import eu.agribot.agribotapp.ui.fragment.toolcontrol.data.ToolsList.BACK_UP_TUZ
import eu.agribot.agribotapp.ui.fragment.toolcontrol.data.ToolsList.BACK_YELLOW
import eu.agribot.agribotapp.ui.fragment.toolcontrol.data.ToolsList.BLUE_TOOL
import eu.agribot.agribotapp.ui.fragment.toolcontrol.data.ToolsList.FRONT_BLUE
import eu.agribot.agribotapp.ui.fragment.toolcontrol.data.ToolsList.FRONT_DOWN_TUZ
import eu.agribot.agribotapp.ui.fragment.toolcontrol.data.ToolsList.FRONT_GREEN
import eu.agribot.agribotapp.ui.fragment.toolcontrol.data.ToolsList.FRONT_PTO
import eu.agribot.agribotapp.ui.fragment.toolcontrol.data.ToolsList.FRONT_UP_TUZ
import eu.agribot.agribotapp.ui.fragment.toolcontrol.data.ToolsList.FRONT_YELLOW
import eu.agribot.agribotapp.ui.fragment.toolcontrol.data.ToolsList.GREEN_TOOL
import eu.agribot.agribotapp.ui.fragment.toolcontrol.data.ToolsList.GREEN_TOOL_LIST_POSITION
import eu.agribot.agribotapp.ui.fragment.toolcontrol.data.ToolsList.PTO_TOOL
import eu.agribot.agribotapp.ui.fragment.toolcontrol.data.ToolsList.PTO_TOOL_LIST_POSITION
import eu.agribot.agribotapp.ui.fragment.toolcontrol.data.ToolsList.YELLOW_TOOL
import eu.agribot.agribotapp.ui.fragment.toolcontrol.recycler.HitchListItemViewModel

class ToolControlViewModel(
    private val manualModeService: ManualModeService
) : BaseViewModel() {
    val frontToolsList: MutableLiveData<List<HitchListItemViewModel>> =
        MutableLiveData(listOf())
    val backToolsList: MutableLiveData<List<HitchListItemViewModel>> =
        MutableLiveData(listOf())
    private val activeTuz: MutableLiveData<TuzSwitchessActive> = MutableLiveData()
    val switchessActive: MutableLiveData<TuzSwitchesState> = MutableLiveData()

    fun onHitchParametersChanged(
        sliderNumber: Int,
        sliderValue: Int,
        isActivated: Boolean
    ) {
        manualModeService.onSwitchParametrChanged(
            SwitchSettings(
                sliderNumber,
                isActivated,
                sliderValue
            )
        )
    }

    fun onActivateTuzParametrChanged(tuzNumber: Int, isTuzActive: Boolean) {
        activeTuz.value = TuzSwitchessActive(tuzNumber, isTuzActive)
        when (tuzNumber) {
            FRONT_UP_TUZ -> {
                if (isTuzActive) {
                    switchessActive.value = TuzSwitchesState(
                        frontUpTuzChecked = true,
                        frontBottomTuzChecked = false,
                        rearUpTuzChecked = false,
                        rearBottomTuzChecked = false
                    )
                }
            }
            FRONT_DOWN_TUZ -> {
                if (isTuzActive) {
                    switchessActive.value = TuzSwitchesState(
                        frontUpTuzChecked = false,
                        frontBottomTuzChecked = true,
                        rearUpTuzChecked = false,
                        rearBottomTuzChecked = false
                    )
                }
            }
            BACK_UP_TUZ -> {
                if (isTuzActive) {
                    switchessActive.value = TuzSwitchesState(
                        frontUpTuzChecked = false,
                        frontBottomTuzChecked = false,
                        rearUpTuzChecked = true,
                        rearBottomTuzChecked = false
                    )
                }
            }
            BACK_DOWN_TUZ -> {
                if (isTuzActive) {
                    switchessActive.value = TuzSwitchesState(
                        frontUpTuzChecked = false,
                        frontBottomTuzChecked = false,
                        rearUpTuzChecked = false,
                        rearBottomTuzChecked = true
                    )
                }
            }
        }
    }

    fun onTuzParametersChanged(progress: Int) {
        if (activeTuz.value != null) {
            onHitchParametersChanged(
                activeTuz.value!!.tuzNumber,
                progress,
                activeTuz.value!!.isTuzActive
            )
        }
    }

    private fun addToFrontToolsList(toolName: String, switchNumber: Int) {
        frontToolsList.value = listOf(
            HitchListItemViewModel(
                toolName,
                switchNumber
            ), *frontToolsList.value!!.toTypedArray()
        )
    }

    private fun addToBackToolsList(toolName: String, switchNumber: Int) {
        backToolsList.value = listOf(
            HitchListItemViewModel(
                toolName,
                switchNumber
            ), *backToolsList.value!!.toTypedArray()
        )
    }

    fun createToolsLists() {
        createFrontToolsList()
        createBackToolsList()
    }

    private fun createFrontToolsList() {
        val toolList = mutableListOf(GREEN_TOOL, YELLOW_TOOL, BLUE_TOOL, PTO_TOOL)
        val switchNumber = mutableListOf(
            FRONT_GREEN, FRONT_YELLOW, FRONT_BLUE, FRONT_PTO
        )
        for (itemPosition in PTO_TOOL_LIST_POSITION..GREEN_TOOL_LIST_POSITION) {
            addToFrontToolsList(toolList[itemPosition], switchNumber[itemPosition])
        }
    }

    private fun createBackToolsList() {
        val toolList = mutableListOf(GREEN_TOOL, YELLOW_TOOL, BLUE_TOOL, PTO_TOOL)
        val switchNumber = mutableListOf(
            BACK_GREEN, BACK_YELLOW, BACK_BLUE, BACK_PTO
        )
        for (itemPosition in PTO_TOOL_LIST_POSITION..GREEN_TOOL_LIST_POSITION) {
            addToBackToolsList(toolList[itemPosition], switchNumber[itemPosition])
        }
    }
}
