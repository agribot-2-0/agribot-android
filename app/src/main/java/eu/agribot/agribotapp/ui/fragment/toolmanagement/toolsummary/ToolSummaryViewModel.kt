package eu.agribot.agribotapp.ui.fragment.toolmanagement.toolsummary

import androidx.lifecycle.MutableLiveData
import eu.agribot.agribotapp.R
import eu.agribot.agribotapp.base.BaseViewModel
import eu.agribot.agribotapp.common.livedata.MutableLiveEvent
import eu.agribot.agribotapp.common.navigation.events.ToolNavigationEvent
import eu.agribot.agribotapp.common.resources.ResourcesProvider
import eu.agribot.agribotapp.domain.model.cdb.ToolDetails
import eu.agribot.agribotapp.domain.service.ToolManagementService
import eu.agribot.agribotapp.domain.service.cloud.CloudDatabaseService
import eu.agribot.agribotapp.domain.service.cloud.ToolDataManager
import eu.agribot.agribotapp.ui.fragment.manualcontrol.viewpager.toolcontrol.recycler.HitchListItemViewModel
import eu.agribot.agribotapp.ui.fragment.toolmanagement.findtag.data.TagRfidFormatter

class ToolSummaryViewModel(
    private val toolDataManager: ToolDataManager,
    private val cloudDatabaseService: CloudDatabaseService,
    private val resourcesProvider: ResourcesProvider,
    private val toolManagementService: ToolManagementService
) : BaseViewModel() {
    private val toastMessageEvent: MutableLiveEvent<String> = MutableLiveEvent()

    val toolName: MutableLiveData<String> = MutableLiveData()
    val toolRfidTag: MutableLiveData<String> = MutableLiveData()
    val paramsList: MutableLiveData<List<HitchListItemViewModel>> =
        MutableLiveData(listOf())
    val displayToolName: MutableLiveData<String> = MutableLiveData()

    init {
        displayToolName.value = String.format(
            resourcesProvider.getString(R.string.display_tool_name),
            toolDataManager.toolName
        )
    }

    fun getToolPhotoDirection(): String {
        return toolManagementService.getToolPhotoToDisplay(toolDataManager.getToolPhotoPathName()).absolutePath + toolDataManager.getToolPhotoPathName()
    }

    fun setSummaryParameters() {
        setToolName()
        setToolTag()
        setAllParameters()
    }

    private fun setToolName() {
        toolName.value = String.format(
            resourcesProvider.getString(R.string.summary_tool_name),
            toolDataManager.toolName
        )
    }

    private fun setToolTag() {
        toolRfidTag.value = String.format(
            resourcesProvider.getString(R.string.summary_tool_tag),
            getDisplayRfidTag(toolDataManager.rfid)
        )
    }

    private fun getDisplayRfidTag(tagIDArray: List<Int>): String {
        var rfidTagHexString = ""
        tagIDArray.forEach {
            rfidTagHexString += String.format(
                TagRfidFormatter.NUMBER_FORMAT.formatElement,
                it
            ) + TagRfidFormatter.NUMBER_SEPARATOR.formatElement
        }
        return rfidTagHexString
    }

    fun sendToolData() {
        cloudDatabaseService.addTool(
            toolName = toolDataManager.toolName,
            toolDetails = ToolDetails(
                rfid = toolDataManager.rfid,
                pto = toolDataManager.convertSliderValueToDatabaseValue(
                    toolDataManager.pto,
                    ToolDataManager.Companion.Value.PTO
                ),
                tplPushPull = toolDataManager.convertSliderValueToDatabaseValue(
                    toolDataManager.tplPushPull,
                    ToolDataManager.Companion.Value.TPL_PUSH_PULL
                ),
                tplUpDown = toolDataManager.convertSliderValueToDatabaseValue(
                    toolDataManager.tplUpDown,
                    ToolDataManager.Companion.Value.TPL_UP_DOWN
                ),
                blue = toolDataManager.convertSliderValueToDatabaseValue(
                    toolDataManager.blue,
                    ToolDataManager.Companion.Value.COLOUR
                ),
                yellow = toolDataManager.convertSliderValueToDatabaseValue(
                    toolDataManager.yellow,
                    ToolDataManager.Companion.Value.COLOUR
                ),
                green = toolDataManager.convertSliderValueToDatabaseValue(
                    toolDataManager.green,
                    ToolDataManager.Companion.Value.COLOUR
                ),
                vMax = toolDataManager.convertSliderValueToDatabaseValue(
                    toolDataManager.vMax,
                    ToolDataManager.Companion.Value.VELOCITY
                )
            ),
            onSucess = this::onAddNewToolSucess
        )
    }

    private fun onAddNewToolSucess() {
        navigationEvent.postValue(
            ToolNavigationEvent()
        )
    }

    private fun setAllParameters() {
        addToParamList(toolDataManager.getMaximumRobotVelocity())
        addToParamList(toolDataManager.getRobotGreenFlow())
        addToParamList(toolDataManager.getRobotYellowFlow())
        addToParamList(toolDataManager.getRobotBlueFlow())
        addToParamList(toolDataManager.getRobotPtoFlow())
        addToParamList(toolDataManager.getRobotTplPushPullFlow())
        addToParamList(toolDataManager.getRobotTplUpDownFlow())
    }

    fun onHitchParametersChanged(
        sliderNumber: Int,
        sliderValue: Int
    ) {
        toolDataManager.setSliderValue(sliderNumber, sliderValue)
    }

    private fun addToParamList(
        hitchParam: HitchListItemViewModel
    ) {
        val isToolAlreadyAdded = paramsList.value!!.any {
            it.toolName == hitchParam.toolName
        }

        if (!isToolAlreadyAdded) {
            paramsList.value = listOf(
                HitchListItemViewModel(
                    toolName = hitchParam.toolName,
                    switchNumber = hitchParam.switchNumber,
                    hitchListItemDetail = hitchParam.hitchListItemDetail
                ), *paramsList.value!!.toTypedArray()
            )
        } else {
            paramsList.value = paramsList.value!!.map { oldAttribute ->
                HitchListItemViewModel(
                    toolName = oldAttribute.toolName,
                    switchNumber = oldAttribute.switchNumber,
                    hitchListItemDetail = hitchParam.hitchListItemDetail
                )
            }
        }
    }

    fun getToolDetails() {
        cloudDatabaseService.getToolDetails(
            toolName = toolDataManager.toolName,
            onGetToolDetails = this::onToolUpdate,
            onGetToolDetailsError = this::postToastErrorMessageEvent
        )
    }

    private fun onToolUpdate(toolDetails: ToolDetails) {
        toolDataManager.updateAllToolParameters(toolDetails)
    }

    private fun postToastErrorMessageEvent(errorMessage: String) {
        toastMessageEvent.postValue(errorMessage)
    }
}
