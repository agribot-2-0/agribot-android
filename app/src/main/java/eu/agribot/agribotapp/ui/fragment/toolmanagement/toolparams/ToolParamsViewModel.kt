package eu.agribot.agribotapp.ui.fragment.toolmanagement.toolparams

import androidx.lifecycle.MutableLiveData
import eu.agribot.agribotapp.base.BaseViewModel
import eu.agribot.agribotapp.common.livedata.MutableLiveEvent
import eu.agribot.agribotapp.common.navigation.events.ToolNavigationEvent
import eu.agribot.agribotapp.domain.model.cdb.ToolDetails
import eu.agribot.agribotapp.domain.model.cdb.ToolParamDetails
import eu.agribot.agribotapp.domain.model.cdb.taskprogram.taskmodel.AddToolDetails
import eu.agribot.agribotapp.domain.service.cloud.CloudDatabaseService
import eu.agribot.agribotapp.domain.service.cloud.ToolDataManager
import eu.agribot.agribotapp.ui.fragment.toolcontrol.recycler.HitchListItemViewModel


class ToolParamsViewModel(
    private val toolDataManager: ToolDataManager,
    private val cloudDatabaseService: CloudDatabaseService
) : BaseViewModel() {
    private val toastMessageEvent: MutableLiveEvent<String> = MutableLiveEvent()
    val paramsList: MutableLiveData<List<HitchListItemViewModel>> =
        MutableLiveData(listOf())

    private fun addToolToToolParamList(toolParamListDetails: ToolParamListDetails) {
        val isToolAlreadyAdded = paramsList.value!!.any() { it ->
            it.toolName == toolParamListDetails.toolName
        }

        if (!isToolAlreadyAdded) {
            paramsList.value = listOf(
                HitchListItemViewModel(
                    toolName = toolParamListDetails.toolName,
                    switchNumber = toolParamListDetails.toolParametrDetails.sliderNumber,
                    toolStartValue = toolParamListDetails.toolParametrDetails.value
                ), *paramsList.value!!.toTypedArray()
            )
        } else {
            paramsList.value = paramsList.value!!.map { oldAttribute ->
                if (oldAttribute.toolName == toolParamListDetails.toolName) {
                    HitchListItemViewModel(
                        toolName = toolParamListDetails.toolName,
                        switchNumber = toolParamListDetails.toolParametrDetails.sliderNumber,
                        toolStartValue = toolParamListDetails.toolParametrDetails.value
                    )
                } else {
                    oldAttribute
                }
            }
        }

    }

    fun createExampleTool() {
        addToolToToolParamList(
            ToolParamListDetails(
                toolName = "PTO",
                toolParametrDetails = ToolParamDetails(
                    sliderNumber = 0,
                    value = toolDataManager.getPto().toInt()
                )
            )
        )
        addToolToToolParamList(
            ToolParamListDetails(
                toolName = "TPL UP/DOWN",
                toolParametrDetails = ToolParamDetails(
                    sliderNumber = 1,
                    value = toolDataManager.getTplUpDown().toInt()
                )
            )
        )
    }

    fun sendToolData() {
        postToolManagementNavigationEvent()
        cloudDatabaseService.addTool(
            toolName = toolDataManager.getToolName(),
            addToolDetails = AddToolDetails(
                rfid = toolDataManager.getRfid(),
                pto = toolDataManager.getPto(),
                tpl_push_pull = toolDataManager.getTplUpDown()
            )
        )
    }

    fun onHitchParametersChanged(
        sliderNumber: Int,
        sliderValue: Int,
        isActivated: Boolean
    ) {
        if (sliderNumber == 0) {
            toolDataManager.onUpdatePto(sliderValue.toDouble())
        }
        if (sliderNumber == 1) {
            toolDataManager.onUpdateTplUpDown(sliderValue.toDouble())
        }
    }

    fun onStart() {
        cloudDatabaseService.getToolDetails(
            toolName = toolDataManager.getToolName(),
            onGetToolDetails = this::onToolUpdate,
            onGetToolDetailsError = this::postToastErrorMessageEvent
        )
    }

    private fun onToolUpdate(toolDetails: ToolDetails) {
        toolDataManager.updateAllToolParameters(toolDetails)
        createExampleTool()
    }

    private fun postToolManagementNavigationEvent() {
        navigationEvent.postValue(
            ToolNavigationEvent()
        )
    }

    private fun postToastErrorMessageEvent(errorMessage: String) {
        toastMessageEvent.postValue(errorMessage)
    }
}
