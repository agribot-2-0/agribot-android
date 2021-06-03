package eu.agribot.agribotapp.ui.fragment.ttcontrol.toolcontroller

import androidx.lifecycle.MutableLiveData
import eu.agribot.agribotapp.R
import eu.agribot.agribotapp.base.BaseViewModel
import eu.agribot.agribotapp.common.resources.ResourcesProvider
import eu.agribot.agribotapp.communication.bluetooth.basic.message.receivedmessage.encoder.Tools
import eu.agribot.agribotapp.domain.service.ManualModeService
import eu.agribot.agribotapp.ui.fragment.manualcontrol.viewpager.toolcontrol.data.ToolsList
import eu.agribot.agribotapp.ui.fragment.manualcontrol.viewpager.toolcontrol.recycler.HitchListItemViewModel

class TTControlToolViewModel(private val resourcesProvider: ResourcesProvider,    private val manualModeService: ManualModeService
) : BaseViewModel() {
    val toolsSwitchList: MutableLiveData<List<HitchListItemViewModel>> = MutableLiveData(listOf())

    init {
        toolsSwitchList.value = createInitializationToolsList()
    }

    private fun createInitializationToolsList(): List<HitchListItemViewModel> {
        return listOf(
            HitchListItemViewModel(switchNumber = Tools.Pto.Front.toolId,toolName = ToolsList.PTO_TOOL, encoderValue = ""),
            HitchListItemViewModel(switchNumber = Tools.Pto.Rear.toolId,toolName = ToolsList.PTO_TOOL, encoderValue = ""),

            HitchListItemViewModel(switchNumber = Tools.Blue.Front.toolId,toolName = ToolsList.BLUE_TOOL, encoderValue = ""),
            HitchListItemViewModel(switchNumber = Tools.Blue.Rear.toolId,toolName = ToolsList.BLUE_TOOL, encoderValue = ""),

            HitchListItemViewModel(switchNumber = Tools.Yellow.Front.toolId,toolName = ToolsList.YELLOW_TOOL, encoderValue = ""),
            HitchListItemViewModel(switchNumber = Tools.Yellow.Rear.toolId,toolName = ToolsList.YELLOW_TOOL, encoderValue = ""),

            HitchListItemViewModel(switchNumber = Tools.Green.Front.toolId,toolName = ToolsList.GREEN_TOOL, encoderValue = ""),
            HitchListItemViewModel(switchNumber = Tools.Green.Rear.toolId,toolName = ToolsList.GREEN_TOOL, encoderValue = "")
        )
    }





}