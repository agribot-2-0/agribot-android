package eu.agribot.agribotapp.ui.fragment.toolmanagement.mainscreen

import androidx.lifecycle.MutableLiveData
import eu.agribot.agribotapp.R
import eu.agribot.agribotapp.base.BaseViewModel
import eu.agribot.agribotapp.common.livedata.MutableLiveEvent
import eu.agribot.agribotapp.common.navigation.events.ToolManagementSummaryNavigationEvent
import eu.agribot.agribotapp.common.navigation.events.ToolNameNavigationEvent
import eu.agribot.agribotapp.common.resources.ResourcesProvider
import eu.agribot.agribotapp.domain.model.cdb.ToolDetails
import eu.agribot.agribotapp.domain.model.cdb.ToolItem
import eu.agribot.agribotapp.domain.service.ToolManagementService
import eu.agribot.agribotapp.domain.service.cloud.CloudDatabaseService
import eu.agribot.agribotapp.domain.service.cloud.ToolDataManager
import eu.agribot.agribotapp.domain.service.cloud.downloader.FilePath
import eu.agribot.agribotapp.ui.fragment.toolcontrol.data.RecyclerConstant
import eu.agribot.agribotapp.ui.fragment.toolmanagement.mainscreen.recycler.toollist.ToolListItemViewModel
import eu.agribot.agribotapp.ui.fragment.toolmanagement.mainscreen.recycler.toolparameters.ListToolParamItem
import eu.agribot.agribotapp.ui.fragment.toolmanagement.mainscreen.recycler.toolparameters.ListToolParamViewModel

class ToolManagementViewModel(
    private val resourcesProvider: ResourcesProvider,
    private val cloudDatabaseService: CloudDatabaseService,
    private val toolDataManager: ToolDataManager,
    private val toolManagementService: ToolManagementService
) : BaseViewModel() {
    val setPhotoEvent: MutableLiveEvent<String> = MutableLiveEvent()
    val toolList: MutableLiveData<List<ToolListItemViewModel>> = MutableLiveData(listOf())
    val listToolParamList: MutableLiveData<List<ListToolParamViewModel>> = MutableLiveData(listOf())
    val toastMessageEvent: MutableLiveEvent<String> = MutableLiveEvent()
    val toolTag: MutableLiveData<String> = MutableLiveData()

    private fun setAllParameters(toolDetails: ToolDetails) {
        toolDetails.let {
            addToParamList(getTplUpDownItem(it))
            addToParamList(getTplPushItem(it))
            addToParamList(getPtoItemItem(it))
            addToParamList(getBlueFlowItem(it))
            addToParamList(getYellowFlowItem(it))
            addToParamList(getGreenFlowItem(it))
            addToParamList(getVelocityItem(it))
        }
    }

    fun getToolPhotoDirection(toolName: String): String {
        return toolManagementService.getToolPhotoToDisplay(toolName).absolutePath + toolName
    }

    private fun getTplUpDownItem(toolDetails: ToolDetails): ListToolParamViewModel {
        return ListToolParamViewModel(
            ListToolParamItem.TPL_UP_DOWN.toolParamId,
            resourcesProvider.getString(R.string.tpl_up_down),
            toolDataManager.convertDatabaseValueToSliderValue(
                toolDetails.tplUpDown,
                ToolDataManager.Companion.Value.TPL_UP_DOWN
            ),
            resourcesProvider.getString(R.string.degree),
            toolDataManager.convertDatabaseValueToSliderValue(
                ToolDataManager.MIN_TPL_UP_DOWN,
                ToolDataManager.Companion.Value.TPL_UP_DOWN
            ),
            toolDataManager.convertDatabaseValueToSliderValue(
                ToolDataManager.MAX_TPL_UP_DOWN,
                ToolDataManager.Companion.Value.TPL_UP_DOWN
            ),
            ToolDataManager.TPL_UP_DOWN_PRECISION
        )
    }

    private fun getTplPushItem(toolDetails: ToolDetails): ListToolParamViewModel {
        return ListToolParamViewModel(
            ListToolParamItem.TPL_PUSH_PULL.toolParamId,
            resourcesProvider.getString(R.string.tpl_push_pull),
            toolDataManager.convertDatabaseValueToSliderValue(
                toolDetails.tplPushPull,
                ToolDataManager.Companion.Value.TPL_PUSH_PULL
            ),
            resourcesProvider.getString(R.string.meters),
            toolDataManager.convertDatabaseValueToSliderValue(
                ToolDataManager.MIN_TPL_PUSH_PULL,
                ToolDataManager.Companion.Value.TPL_PUSH_PULL
            ),
            toolDataManager.convertDatabaseValueToSliderValue(
                ToolDataManager.MAX_TPL_PUSH_PULL,
                ToolDataManager.Companion.Value.TPL_PUSH_PULL
            ),
            ToolDataManager.TPL_PUSH_PULL_PRECISION
        )
    }

    private fun getPtoItemItem(toolDetails: ToolDetails): ListToolParamViewModel {
        return ListToolParamViewModel(
            ListToolParamItem.PTO.toolParamId,
            resourcesProvider.getString(R.string.pto),
            toolDataManager.convertDatabaseValueToSliderValue(
                toolDetails.pto,
                ToolDataManager.Companion.Value.PTO
            ),
            resourcesProvider.getString(R.string.rpm),
            toolDataManager.convertDatabaseValueToSliderValue(
                ToolDataManager.MIN_PTO,
                ToolDataManager.Companion.Value.PTO
            ),
            toolDataManager.convertDatabaseValueToSliderValue(
                ToolDataManager.MAX_PTO,
                ToolDataManager.Companion.Value.PTO
            ),
            ToolDataManager.PTO_PRECISION
        )
    }

    private fun getBlueFlowItem(toolDetails: ToolDetails): ListToolParamViewModel {
        return ListToolParamViewModel(
            ListToolParamItem.BLUE_FLOW.toolParamId,
            resourcesProvider.getString(R.string.blue_flow),
            toolDataManager.convertDatabaseValueToSliderValue(
                toolDetails.blue,
                ToolDataManager.Companion.Value.VALVE_FLOW
            ),
            resourcesProvider.getString(R.string.percent),
            toolDataManager.convertDatabaseValueToSliderValue(
                ToolDataManager.MIN_VALVE_FLOW,
                ToolDataManager.Companion.Value.VALVE_FLOW
            ),
            toolDataManager.convertDatabaseValueToSliderValue(
                ToolDataManager.MAX_VALVE_FLOW,
                ToolDataManager.Companion.Value.VALVE_FLOW
            ),
            ToolDataManager.VALVE_FLOW_PRECISION
        )
    }

    private fun getYellowFlowItem(toolDetails: ToolDetails): ListToolParamViewModel {
        return ListToolParamViewModel(
            ListToolParamItem.YELLOW_FLOW.toolParamId,
            resourcesProvider.getString(R.string.yellow_flow),
            toolDataManager.convertDatabaseValueToSliderValue(
                toolDetails.yellow,
                ToolDataManager.Companion.Value.VALVE_FLOW
            ),
            resourcesProvider.getString(R.string.percent),
            toolDataManager.convertDatabaseValueToSliderValue(
                ToolDataManager.MIN_VALVE_FLOW,
                ToolDataManager.Companion.Value.VALVE_FLOW
            ),
            toolDataManager.convertDatabaseValueToSliderValue(
                ToolDataManager.MAX_VALVE_FLOW,
                ToolDataManager.Companion.Value.VALVE_FLOW
            ),
            ToolDataManager.VALVE_FLOW_PRECISION
        )
    }

    private fun getGreenFlowItem(toolDetails: ToolDetails): ListToolParamViewModel {
        return ListToolParamViewModel(
            ListToolParamItem.GREEN_FLOW.toolParamId,
            resourcesProvider.getString(R.string.green_flow),
            toolDataManager.convertDatabaseValueToSliderValue(
                toolDetails.green,
                ToolDataManager.Companion.Value.VALVE_FLOW
            ),
            resourcesProvider.getString(R.string.percent),
            toolDataManager.convertDatabaseValueToSliderValue(
                ToolDataManager.MIN_VALVE_FLOW,
                ToolDataManager.Companion.Value.VALVE_FLOW
            ),
            toolDataManager.convertDatabaseValueToSliderValue(
                ToolDataManager.MAX_VALVE_FLOW,
                ToolDataManager.Companion.Value.VALVE_FLOW
            ),
            ToolDataManager.VALVE_FLOW_PRECISION
        )
    }

    private fun getVelocityItem(toolDetails: ToolDetails): ListToolParamViewModel {
        return ListToolParamViewModel(
            ListToolParamItem.VELOCITY.toolParamId,
            resourcesProvider.getString(R.string.v_max),
            toolDataManager.convertDatabaseValueToSliderValue(
                toolDetails.vMax,
                ToolDataManager.Companion.Value.VELOCITY
            ),
            resourcesProvider.getString(R.string.kilometer_per_hour),
            toolDataManager.convertDatabaseValueToSliderValue(
                ToolDataManager.MIN_VELOCITY,
                ToolDataManager.Companion.Value.VELOCITY
            ),
            toolDataManager.convertDatabaseValueToSliderValue(
                ToolDataManager.MAX_VELOCITY,
                ToolDataManager.Companion.Value.VELOCITY
            ),
            ToolDataManager.VELOCITY_PRECISION
        )
    }

    private fun addToParamList(listToolParam: ListToolParamViewModel) {
        val isAttributeAlreadyAdded = listToolParamList.value!!.any { arrtibute ->
            arrtibute.toolParamID == listToolParam.toolParamID
        }
        if (!isAttributeAlreadyAdded)
            listToolParamList.value = listOf(
                *listToolParamList.value!!.toTypedArray(),
                ListToolParamViewModel(
                    listToolParam.toolParamID,
                    listToolParam.paramName,
                    listToolParam.paramValue,
                    listToolParam.paramUnit,
                    listToolParam.minValue,
                    listToolParam.maxValue,
                    listToolParam.precision
                )
            )
        else {
            listToolParamList.value = listToolParamList.value!!.map { oldAttribute ->
                if (oldAttribute.toolParamID == listToolParam.toolParamID) {
                    ListToolParamViewModel(
                        oldAttribute.toolParamID,
                        oldAttribute.paramName,
                        listToolParam.paramValue,
                        oldAttribute.paramUnit,
                        oldAttribute.minValue,
                        oldAttribute.maxValue,
                        oldAttribute.precision
                    )
                } else {
                    oldAttribute
                }
            }
        }
    }

    fun onNewToolButtonClicked() {
        navigationEvent.postValue(
            ToolNameNavigationEvent()
        )
    }

    fun onToolSelected(toolListItemViewModel: ToolListItemViewModel) {
        cloudDatabaseService.getToolDetails(
            toolName = toolListItemViewModel.toolName,
            onGetToolDetails = this::onGotToolDetails,
            onGetToolDetailsError = this::postToastErrorMessageEvent
        )
        setPhotoEvent.postValue(getToolPhotoPathName(toolListItemViewModel.toolName))
    }

    private fun getToolPhotoPathName(toolName: String): String {
        return "/$toolName${FilePath.TOOL_PHOTO_FILE_TYPE}"
    }

    private fun onGotToolDetails(toolDetails: ToolDetails) {
        toolDataManager.updateAllToolParameters(toolDetails)
        setAllParameters(toolDetails)
        toolTag.value = String.format(
            resourcesProvider.getString(R.string.displayed_tool_tag_text),
            toolDetails.rfid
        )
    }

    fun onDeleteButtonClicked(positionOnList: Int) {
        cloudDatabaseService.deleteTool(
            toolList.value?.get(positionOnList)!!.toolName,
            onDeleteError = this::postToastErrorMessageEvent,
            onDeleteSucess = this::onDeleteSucess
        )
        toolList.value = toolList.value!!.minus(
            ToolListItemViewModel(
                toolList.value?.get(positionOnList)!!.toolName
            )
        )
    }

    fun getAvailableTool() {
        cloudDatabaseService.getToolItem(
            onGetToolItem = this::onAvailableToolFound,
            onGetToolItemError = this::postToastErrorMessageEvent
        )
    }

    private fun onAvailableToolFound(toolItem: ToolItem) {
        val isTaskAlreadyAdded = toolList.value!!.any { foundTool ->
            foundTool.toolName == toolItem.toolName
        }
        if (!isTaskAlreadyAdded) {
            toolList.value = listOf(
                ToolListItemViewModel(
                    toolItem.toolName
                ), *toolList.value!!.toTypedArray()
            )
        }
    }

    fun onEditButtonClicked(positionOnList: Int) {
        toolList.value?.get(positionOnList)?.toolName?.let { toolName ->
            toolDataManager.toolName = toolName
            cloudDatabaseService.getToolDetails(
                toolName,
                onGetToolDetails = this::updateToolDetails,
                onGetToolDetailsError = this::postToastErrorMessageEvent
            )
        }
        navigationEvent.postValue(ToolManagementSummaryNavigationEvent())
    }

    private fun updateToolDetails(toolDetails: ToolDetails) {
        toolDataManager.updateAllToolParameters(toolDetails)
    }

    private fun postToastErrorMessageEvent(errorMessage: String) {
        toastMessageEvent.postValue(errorMessage)
    }

    private fun onDeleteSucess() {
        postToastInformationMessageEvent(resourcesProvider.getString(R.string.delete_sucess_text))
    }

    private fun postToastInformationMessageEvent(informationMessage: String) {
        toastMessageEvent.postValue(informationMessage)
    }
}
