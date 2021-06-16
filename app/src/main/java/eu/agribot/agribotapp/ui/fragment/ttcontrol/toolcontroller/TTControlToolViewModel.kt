package eu.agribot.agribotapp.ui.fragment.ttcontrol.toolcontroller

import androidx.lifecycle.MutableLiveData
import eu.agribot.agribotapp.base.BaseViewModel
import eu.agribot.agribotapp.common.resources.ResourcesProvider
import eu.agribot.agribotapp.communication.bluetooth.basic.message.toolcontrolmessage.Tools
import eu.agribot.agribotapp.domain.model.fromdomain.SwitchSettings
import eu.agribot.agribotapp.domain.service.ManualModeService
import eu.agribot.agribotapp.ui.fragment.manualcontrol.viewpager.toolcontrol.data.ToolsList
import eu.agribot.agribotapp.ui.fragment.manualcontrol.viewpager.toolcontrol.data.TplSwitchesState
import eu.agribot.agribotapp.ui.fragment.manualcontrol.viewpager.toolcontrol.recycler.HitchListItemViewModel
import eu.agribot.agribotapp.ui.fragment.toolcontrol.data.RecyclerConstant
import eu.agribot.agribotapp.ui.fragment.toolcontrol.data.TplSwitchesActive

class TTControlToolViewModel(
    private val resourcesProvider: ResourcesProvider,
    private val manualModeService: ManualModeService
) : BaseViewModel() {
    val toolsSwitchList: MutableLiveData<List<HitchListItemViewModel>> = MutableLiveData(listOf())
    val switchesActive: MutableLiveData<TplSwitchesState> = MutableLiveData()

    private val activeTpl: MutableLiveData<TplSwitchesActive> = MutableLiveData()

    init {
        toolsSwitchList.value = createInitializationToolsList()
    }

    private fun createInitializationToolsList(): List<HitchListItemViewModel> {
        return listOf(
            HitchListItemViewModel(
                switchNumber = Tools.Pto.Front.toolId,
                toolName = ToolsList.PTO_TOOL,
                encoderValue = "",
                isNewToolControl = false
            ),
            HitchListItemViewModel(
                switchNumber = Tools.Pto.Rear.toolId,
                toolName = ToolsList.PTO_TOOL,
                encoderValue = "",
                isNewToolControl = false
            ),

            HitchListItemViewModel(
                switchNumber = Tools.Blue.Front.toolId,
                toolName = ToolsList.BLUE_TOOL,
                encoderValue = "",
                isNewToolControl = false
            ),
            HitchListItemViewModel(
                switchNumber = Tools.Blue.Rear.toolId,
                toolName = ToolsList.BLUE_TOOL,
                encoderValue = "",
                isNewToolControl = false
            ),

            HitchListItemViewModel(
                switchNumber = Tools.Yellow.Front.toolId,
                toolName = ToolsList.YELLOW_TOOL,
                encoderValue = "",
                isNewToolControl = false
            ),
            HitchListItemViewModel(
                switchNumber = Tools.Yellow.Rear.toolId,
                toolName = ToolsList.YELLOW_TOOL,
                encoderValue = "",
                isNewToolControl = false
            ),

            HitchListItemViewModel(
                switchNumber = Tools.Green.Front.toolId,
                toolName = ToolsList.GREEN_TOOL,
                encoderValue = "",
                isNewToolControl = false
            ),
            HitchListItemViewModel(
                switchNumber = Tools.Green.Rear.toolId,
                toolName = ToolsList.GREEN_TOOL,
                encoderValue = "",
                isNewToolControl = false
            )
        )
    }

    fun onHitchParametersChanged(
        sliderNumber: Int,
        sliderValue: Int,
        isActivated: Boolean
    ) {
        manualModeService.createTTControllToolMessage(
            SwitchSettings(
                sliderNumber,
                isActivated,
                sliderValue
            )
        )
    }

    fun onActivateTplParametrChanged(tplNumber: Int, isTplActive: Boolean) {

        onHitchParametersChanged(
            ToolsList.ALL_SWITCHES,
            RecyclerConstant.DEFAULT_SLIDER_VALUE_MIN,
            false
        )
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

    fun onTplValueChanged(progress: Int) {
        switchesActive.value.let {
            if (it != null) {
                manualModeService.createTTControllToolMessage(
                    SwitchSettings(
                        it.activeTplId,
                        true,
                        progress
                    )
                )
            }
        }
    }
}
