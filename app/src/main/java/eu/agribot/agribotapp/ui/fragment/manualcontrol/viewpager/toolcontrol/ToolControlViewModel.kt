package eu.agribot.agribotapp.ui.fragment.manualcontrol.viewpager.toolcontrol

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import eu.agribot.agribotapp.base.BaseViewModel
import eu.agribot.agribotapp.domain.model.fromdomain.SwitchSettings
import eu.agribot.agribotapp.domain.service.ManualModeService
import eu.agribot.agribotapp.ui.fragment.manualcontrol.viewpager.toolcontrol.data.ToolsList.ALL_SWITCHES
import eu.agribot.agribotapp.ui.fragment.manualcontrol.viewpager.toolcontrol.data.ToolsList.BACK_BLUE
import eu.agribot.agribotapp.ui.fragment.manualcontrol.viewpager.toolcontrol.data.ToolsList.BACK_DOWN_TPL
import eu.agribot.agribotapp.ui.fragment.manualcontrol.viewpager.toolcontrol.data.ToolsList.BACK_GREEN
import eu.agribot.agribotapp.ui.fragment.manualcontrol.viewpager.toolcontrol.data.ToolsList.BACK_PTO
import eu.agribot.agribotapp.ui.fragment.manualcontrol.viewpager.toolcontrol.data.ToolsList.BACK_UP_TPL
import eu.agribot.agribotapp.ui.fragment.manualcontrol.viewpager.toolcontrol.data.ToolsList.BACK_YELLOW
import eu.agribot.agribotapp.ui.fragment.manualcontrol.viewpager.toolcontrol.data.ToolsList.BLUE_TOOL
import eu.agribot.agribotapp.ui.fragment.manualcontrol.viewpager.toolcontrol.data.ToolsList.FRONT_BLUE
import eu.agribot.agribotapp.ui.fragment.manualcontrol.viewpager.toolcontrol.data.ToolsList.FRONT_DOWN_TPL
import eu.agribot.agribotapp.ui.fragment.manualcontrol.viewpager.toolcontrol.data.ToolsList.FRONT_GREEN
import eu.agribot.agribotapp.ui.fragment.manualcontrol.viewpager.toolcontrol.data.ToolsList.FRONT_PTO
import eu.agribot.agribotapp.ui.fragment.manualcontrol.viewpager.toolcontrol.data.ToolsList.FRONT_UP_TPL
import eu.agribot.agribotapp.ui.fragment.manualcontrol.viewpager.toolcontrol.data.ToolsList.FRONT_YELLOW
import eu.agribot.agribotapp.ui.fragment.manualcontrol.viewpager.toolcontrol.data.ToolsList.GREEN_TOOL
import eu.agribot.agribotapp.ui.fragment.manualcontrol.viewpager.toolcontrol.data.ToolsList.GREEN_TOOL_LIST_POSITION
import eu.agribot.agribotapp.ui.fragment.manualcontrol.viewpager.toolcontrol.data.ToolsList.PTO_TOOL
import eu.agribot.agribotapp.ui.fragment.manualcontrol.viewpager.toolcontrol.data.ToolsList.PTO_TOOL_LIST_POSITION
import eu.agribot.agribotapp.ui.fragment.manualcontrol.viewpager.toolcontrol.data.ToolsList.YELLOW_TOOL
import eu.agribot.agribotapp.ui.fragment.manualcontrol.viewpager.toolcontrol.recycler.HitchListItemViewModel
import eu.agribot.agribotapp.ui.fragment.toolcontrol.data.RecyclerConstant
import eu.agribot.agribotapp.ui.fragment.toolcontrol.data.TplSwitchesActive
import eu.agribot.agribotapp.ui.fragment.toolcontrol.data.TplSwitchesState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
class ToolControlViewModel(
    private val manualModeService: ManualModeService
) : BaseViewModel() {
    val frontToolsList: MutableLiveData<List<HitchListItemViewModel>> =
        MutableLiveData(listOf())
    val backToolsList: MutableLiveData<List<HitchListItemViewModel>> =
        MutableLiveData(listOf())
    private val activeTpl: MutableLiveData<TplSwitchesActive> = MutableLiveData()
    val switchesActive: MutableLiveData<TplSwitchesState> = MutableLiveData()
    val tplEncoderValue: MutableLiveData<ToolEncoderValue> = MutableLiveData()

    init {
        createToolsLists()
        fetchToolDataFeedback()
    }

    @ExperimentalCoroutinesApi
    private fun fetchToolDataFeedback() = viewModelScope.launch {
        manualModeService.observeToolFeedbackData().collect { toolFeedbackData ->
            toolFeedbackData?.let {
                when {
                    setOf(
                        FRONT_PTO,
                        FRONT_BLUE,
                        FRONT_YELLOW,
                        FRONT_GREEN
                    ).contains(it.toolId) -> {
                        addToFrontToolsList("", it.toolId, it.toolValue.toString())
                    }
                    setOf(
                        BACK_PTO,
                        BACK_BLUE,
                        BACK_YELLOW,
                        BACK_GREEN
                    ).contains(it.toolId) -> {
                        addToBackToolsList("", it.toolId, it.toolValue.toString())
                    }
                    setOf(
                        FRONT_UP_TPL,
                        FRONT_DOWN_TPL,
                        BACK_UP_TPL,
                        BACK_DOWN_TPL
                    ).contains(it.toolId) -> {
                        setEncoderToolValue(it.toolId, it.toolValue.toString())
                    }
                }
            }
        }
    }

    private fun setEncoderToolValue(toolID: Int, encoderValue: String) {
        tplEncoderValue.value = ToolEncoderValue(toolID, encoderValue)
    }

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

    fun onActivateTplParametrChanged(tplNumber: Int, isTplActive: Boolean) {
        onHitchParametersChanged(ALL_SWITCHES, RecyclerConstant.DEFAULT_SLIDER_VALUE_MIN, false)
        activeTpl.value = TplSwitchesActive(tplNumber, isTplActive)
        when (tplNumber) {
            FRONT_UP_TPL -> {
                setFrontUpTplActive(isTplActive)
            }
            FRONT_DOWN_TPL -> {
                setFrontDownTplActive(isTplActive)
            }
            BACK_UP_TPL -> {
                setBackUpTplActive(isTplActive)
            }
            BACK_DOWN_TPL -> {
                setBackDownTplActive(isTplActive)
            }
        }
    }

    private fun setFrontUpTplActive(activeStatus: Boolean) {
        switchesActive.value = TplSwitchesState(
            frontUpTplChecked = activeStatus,
            frontBottomTplChecked = false,
            rearUpTplChecked = false,
            rearBottomTplChecked = false
        )
    }

    private fun setFrontDownTplActive(activeStatus: Boolean) {
        switchesActive.value = TplSwitchesState(
            frontUpTplChecked = false,
            frontBottomTplChecked = activeStatus,
            rearUpTplChecked = false,
            rearBottomTplChecked = false
        )
    }

    private fun setBackUpTplActive(activeStatus: Boolean) {
        switchesActive.value = TplSwitchesState(
            frontUpTplChecked = false,
            frontBottomTplChecked = false,
            rearUpTplChecked = activeStatus,
            rearBottomTplChecked = false
        )
    }

    private fun setBackDownTplActive(activeStatus: Boolean) {
        switchesActive.value = TplSwitchesState(
            frontUpTplChecked = false,
            frontBottomTplChecked = false,
            rearUpTplChecked = false,
            rearBottomTplChecked = activeStatus
        )
    }

    fun onTplParametersChanged(progress: Int) {
        if (activeTpl.value != null) {
            onHitchParametersChanged(
                activeTpl.value!!.tuzNumber,
                progress,
                activeTpl.value!!.isTplActive
            )
        }
    }

    private fun addToFrontToolsList(toolName: String, switchNumber: Int, encoderValue: String) {
        val isAttributeAlreadyAdded = frontToolsList.value!!.any { arrtibute ->
            arrtibute.toolName == toolName
        }
        if (!isAttributeAlreadyAdded) {
            frontToolsList.value = listOf(
                HitchListItemViewModel(
                    toolName,
                    switchNumber,
                    encoderValue
                ),
                *frontToolsList.value!!.toTypedArray()
            )
        } else {
            frontToolsList.value = frontToolsList.value!!.map { oldAttribute ->
                if (oldAttribute.switchNumber == switchNumber) {
                    HitchListItemViewModel(
                        oldAttribute.toolName,
                        oldAttribute.switchNumber,
                        encoderValue
                    )
                } else {
                    oldAttribute
                }
            }
        }
    }

    private fun addToBackToolsList(toolName: String, switchNumber: Int, encoderValue: String) {
        val isAttributeAlreadyAdded = backToolsList.value!!.any { arrtibute ->
            arrtibute.toolName == toolName
        }
        if (!isAttributeAlreadyAdded) {
            backToolsList.value = listOf(
                HitchListItemViewModel(
                    toolName,
                    switchNumber,
                    encoderValue
                ),
                *backToolsList.value!!.toTypedArray()
            )
        } else {
            backToolsList.value = backToolsList.value!!.map { oldAttribute ->
                if (oldAttribute.switchNumber == switchNumber) {
                    HitchListItemViewModel(
                        oldAttribute.toolName,
                        oldAttribute.switchNumber,
                        encoderValue
                    )
                } else {
                    oldAttribute
                }
            }
        }
    }

    private fun createToolsLists() {
        createFrontToolsList()
        createBackToolsList()
    }

    private fun createFrontToolsList() {
        val toolList = listOf(GREEN_TOOL, YELLOW_TOOL, BLUE_TOOL, PTO_TOOL)
        val switchNumber = listOf(
            FRONT_GREEN, FRONT_YELLOW, FRONT_BLUE, FRONT_PTO
        )
        for (itemPosition in PTO_TOOL_LIST_POSITION..GREEN_TOOL_LIST_POSITION) {
            addToFrontToolsList(toolList[itemPosition], switchNumber[itemPosition], "")
        }
    }

    private fun createBackToolsList() {
        val toolList = listOf(GREEN_TOOL, YELLOW_TOOL, BLUE_TOOL, PTO_TOOL)
        val switchNumber = listOf(
            BACK_GREEN, BACK_YELLOW, BACK_BLUE, BACK_PTO
        )
        for (itemPosition in PTO_TOOL_LIST_POSITION..GREEN_TOOL_LIST_POSITION) {
            addToBackToolsList(toolList[itemPosition], switchNumber[itemPosition], "")
        }
    }

    data class ToolEncoderValue(
        val toolID: Int,
        val EncoderValue: String
    )
}
