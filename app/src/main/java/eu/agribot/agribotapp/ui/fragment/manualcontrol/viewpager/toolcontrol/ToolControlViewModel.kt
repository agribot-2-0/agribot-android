package eu.agribot.agribotapp.ui.fragment.manualcontrol.viewpager.toolcontrol

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import eu.agribot.agribotapp.R
import eu.agribot.agribotapp.base.BaseViewModel
import eu.agribot.agribotapp.common.resources.ResourcesProvider
import eu.agribot.agribotapp.communication.bluetooth.basic.message.toolcontrolmessage.Tools
import eu.agribot.agribotapp.domain.model.ToolControlModel
import eu.agribot.agribotapp.domain.service.ManualModeService
import eu.agribot.agribotapp.ui.fragment.manualcontrol.viewpager.toolcontrol.data.ToolsList.BLUE_TOOL
import eu.agribot.agribotapp.ui.fragment.manualcontrol.viewpager.toolcontrol.data.ToolsList.GREEN_TOOL
import eu.agribot.agribotapp.ui.fragment.manualcontrol.viewpager.toolcontrol.data.ToolsList.PTO_TOOL
import eu.agribot.agribotapp.ui.fragment.manualcontrol.viewpager.toolcontrol.data.ToolsList.YELLOW_TOOL
import eu.agribot.agribotapp.ui.fragment.manualcontrol.viewpager.toolcontrol.data.TplSwitchesState
import eu.agribot.agribotapp.ui.fragment.manualcontrol.viewpager.toolcontrol.recycler.HitchListItemViewModel
import eu.agribot.agribotapp.ui.fragment.toolcontrol.data.TplSwitchesActive
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
class ToolControlViewModel(
    private val manualModeService: ManualModeService,
    private val resourcesProvider: ResourcesProvider
) : BaseViewModel() {
    val toolsList: MutableLiveData<List<HitchListItemViewModel>> =
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
                        Tools.Blue.Front.toolId,
                        Tools.Pto.Front.toolId,
                        Tools.Yellow.Front.toolId,
                        Tools.Green.Front.toolId
                    ).contains(it.toolId) -> {
                        addToToolsList(
                            "",
                            it.toolId,
                            it.toolValue.toString()
                        )
                    }
                    setOf(
                        Tools.Blue.Rear.toolId,
                        Tools.Pto.Rear.toolId,
                        Tools.Yellow.Rear.toolId,
                        Tools.Green.Rear.toolId
                    ).contains(it.toolId) -> {
                        addToToolsList(
                            "",
                            it.toolId,
                            it.toolValue.toString()
                        )
                    }
                    setOf(
                        Tools.TplAngle.Front.toolId,
                        Tools.TplAngle.Rear.toolId,
                        Tools.TplHeight.Front.toolId,
                        Tools.TplHeight.Rear.toolId
                    ).contains(it.toolId) -> {
                        setEncoderTplValue(
                            it.toolId,
                            (it.toolValue.toDouble() / 1000).toString()
                        )
                    }
                }
            }
        }
    }

    private fun setEncoderTplValue(toolID: Int, encoderValue: String) {
        tplEncoderValue.value = ToolEncoderValue(
            toolID,
            encoderValue
        )
    }

    fun onActivateTplParametrChanged(tplNumber: Int, isTplActive: Boolean) {
        activeTpl.value = TplSwitchesActive(tplNumber, isTplActive)
        when (tplNumber) {
            Tools.TplHeight.Front.toolId -> {
                setFrontUpTplActive(isTplActive)
            }
            Tools.TplAngle.Front.toolId -> {
                setFrontDownTplActive(isTplActive)
            }
            Tools.TplHeight.Rear.toolId -> {
                setBackUpTplActive(isTplActive)
            }
            Tools.TplAngle.Rear.toolId -> {
                setBackDownTplActive(isTplActive)
            }
        }
    }

    private fun setFrontUpTplActive(activeStatus: Boolean) {
        switchesActive.value = TplSwitchesState(
            activeTplId = Tools.TplHeight.Front.toolId,
            frontUpTplChecked = activeStatus,
            frontBottomTplChecked = false,
            rearUpTplChecked = false,
            rearBottomTplChecked = false
        )
    }

    private fun setFrontDownTplActive(activeStatus: Boolean) {
        switchesActive.value = TplSwitchesState(
            activeTplId = Tools.TplAngle.Front.toolId,
            frontUpTplChecked = false,
            frontBottomTplChecked = activeStatus,
            rearUpTplChecked = false,
            rearBottomTplChecked = false
        )
    }

    private fun setBackUpTplActive(activeStatus: Boolean) {
        switchesActive.value = TplSwitchesState(
            activeTplId = Tools.TplHeight.Rear.toolId,
            frontUpTplChecked = false,
            frontBottomTplChecked = false,
            rearUpTplChecked = activeStatus,
            rearBottomTplChecked = false
        )
    }

    private fun setBackDownTplActive(activeStatus: Boolean) {
        switchesActive.value = TplSwitchesState(
            activeTplId = Tools.TplAngle.Rear.toolId,
            frontUpTplChecked = false,
            frontBottomTplChecked = false,
            rearUpTplChecked = false,
            rearBottomTplChecked = activeStatus
        )
    }

    private fun addToToolsList(toolName: String, switchNumber: Int, encoderValue: String) {
        val isAttributeAlreadyAdded = toolsList.value!!.any { arrtibute ->
            arrtibute.switchNumber == switchNumber
        }
        if (!isAttributeAlreadyAdded) {
            toolsList.value = listOf(
                HitchListItemViewModel(
                    toolName,
                    switchNumber,
                    isNewToolControl = true,
                    encoderValue
                ),
                *toolsList.value!!.toTypedArray()
            )
        } else {
            toolsList.value = toolsList.value!!.map { oldAttribute ->
                if (oldAttribute.switchNumber == switchNumber) {
                    HitchListItemViewModel(
                        oldAttribute.toolName,
                        oldAttribute.switchNumber,
                        encoderValue = encoderValue,
                        isNewToolControl = true
                    )
                } else {
                    oldAttribute
                }
            }
        }
    }

    private fun createToolsLists() {
        val toolList = listOf(
            GREEN_TOOL,
            GREEN_TOOL,
            YELLOW_TOOL,
            YELLOW_TOOL,
            BLUE_TOOL,
            BLUE_TOOL,
            PTO_TOOL,
            PTO_TOOL
        )
        val switchNumber = listOf(
            Tools.Green.Rear.toolId,
            Tools.Green.Front.toolId,
            Tools.Yellow.Rear.toolId,
            Tools.Yellow.Front.toolId,
            Tools.Blue.Rear.toolId,
            Tools.Blue.Front.toolId,
            Tools.Pto.Rear.toolId,
            Tools.Pto.Front.toolId
        )
        for (itemPosition in switchNumber.indices) {
            addToToolsList(
                toolList[itemPosition],
                switchNumber[itemPosition],
                ""
            )
        }
    }

    fun getSeekBarText(progress: Int): String {
        switchesActive.value?.activeTplId.let {
            return if (it != null) {
                getTplProgressValue(
                    it,
                    progress
                )
            } else ""
        }
    }

    fun onHitchParametersChanged(
        sliderNumber: Int,
        sliderValue: Int,
        isActivated: Boolean
    ) {
        if (isActivated) {
            manualModeService.createToolControlMessage(
                ToolControlModel(
                    toolId = sliderNumber,
                    toolValue = sliderValue
                )
            )
        }
    }

    fun onSeekbarSetted(progress: Int) {
        switchesActive.value?.activeTplId?.also {
            manualModeService.createToolControlMessage(
                ToolControlModel(
                    toolId = it,
                    toolValue = progress
                )
            )
        }
    }

    private fun getTplProgressValue(toolId: Int, toolProgress: Int): String {
        return if (toolId == Tools.TplHeight.Front.toolId || toolId == Tools.TplHeight.Rear.toolId) {
            String.format(TWO_DIGIT_AFTER_POINT, toolProgress.toDouble() * VALUE_RATIO) + resourcesProvider.getString(R.string.meters)
        } else return if (toolId == Tools.TplAngle.Front.toolId || toolId == Tools.TplAngle.Rear.toolId) {
            String.format(TWO_DIGIT_AFTER_POINT, toolProgress.toDouble() * VALUE_RATIO) + resourcesProvider.getString(R.string.radians)
        } else {
            return String.format(TWO_DIGIT_AFTER_POINT, toolProgress.toDouble() * VALUE_RATIO)
        }
    }

    data class ToolEncoderValue(
        val toolID: Int,
        val EncoderValue: String
    )

    companion object {
        const val VALUE_RATIO = 0.01
        const val TWO_DIGIT_AFTER_POINT = "%.2f"
    }
}
