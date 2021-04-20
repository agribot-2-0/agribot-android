package eu.agribot.agribotapp.ui.fragment.toolmanagement.toolparameters

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
import eu.agribot.agribotapp.ui.fragment.toolcontrol.recycler.HitchListItemViewModel
import eu.agribot.agribotapp.ui.fragment.toolmanagement.findtag.data.TagRfidFormatter

class ToolParamsViewModel(
    private val toolDataManager: ToolDataManager,
    private val cloudDatabaseService: CloudDatabaseService,
    private val resourcesProvider: ResourcesProvider,
    private val toolManagementService: ToolManagementService

) : BaseViewModel() {
    val toolName: MutableLiveData<String> = MutableLiveData()
    private val toastMessageEvent: MutableLiveEvent<String> = MutableLiveEvent()
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

    private fun createHitchParametersList() {
        addToolToToolParamList(toolDataManager.getMaximumRobotVelocity())
        addToolToToolParamList(toolDataManager.getRobotGreenFlow())
        addToolToToolParamList(toolDataManager.getRobotYellowFlow())
        addToolToToolParamList(toolDataManager.getRobotBlueFlow())
        addToolToToolParamList(toolDataManager.getRobotPtoFlow())
        addToolToToolParamList(toolDataManager.getRobotTplPushPullFlow())
        addToolToToolParamList(toolDataManager.getRobotTplUpDownFlow())
    }

    fun onHitchParametersChanged(
        sliderNumber: Int,
        sliderValue: Int
    ) {
        toolDataManager.setSliderValue(sliderNumber, sliderValue)
    }

    private fun addToolToToolParamList(
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

    fun createToolsList() {
        cloudDatabaseService.getToolDetails(
            toolName = toolDataManager.toolName,
            onGetToolDetails = this::onToolUpdate,
            onGetToolDetailsError = this::postToastErrorMessageEvent
        )
        createHitchParametersList()
        setToolName()
        setToolTag()
    }

    private fun onToolUpdate(toolDetails: ToolDetails) {
        toolDataManager.updateAllToolParameters(toolDetails)
        toolRfidTag.value = toolDataManager.rfid.toString()
        createHitchParametersList()
    }

    private fun postToastErrorMessageEvent(errorMessage: String) {
        toastMessageEvent.postValue(errorMessage)
    }

    fun sendToolData() {
        cloudDatabaseService.addTool(
            toolName = toolDataManager.toolName,
            toolDetails = ToolDetails(
                rfid = toolDataManager.rfid,
                pto = toolDataManager.pto,
                tplPushPull = toolDataManager.tplPushPull,
                tplUpDown = toolDataManager.tplUpDown,
                blue = toolDataManager.blue,
                yellow = toolDataManager.yellow,
                green = toolDataManager.green,
                vMax = toolDataManager.vMax
            ),
            onSucess = this::onAddNewToolSucess
        )
    }

    private fun onAddNewToolSucess() {
        navigationEvent.postValue(
            ToolNavigationEvent()
        )
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

    fun getToolPhotoDirection(): String {
        return toolManagementService.getPhotoDirectory().absolutePath + toolDataManager.getToolPhotoPathName()
    }
}
