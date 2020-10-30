package eu.agribot.agribotapp.ui.fragment.toolmanagement.toolparams

import androidx.lifecycle.MutableLiveData
import eu.agribot.agribotapp.base.BaseViewModel
import eu.agribot.agribotapp.common.livedata.MutableLiveEvent
import eu.agribot.agribotapp.common.navigation.events.ToolNavigationEvent
import eu.agribot.agribotapp.domain.model.cdb.ToolDetails
import eu.agribot.agribotapp.domain.model.cdb.taskprogram.taskmodel.AddToolDetails
import eu.agribot.agribotapp.domain.service.cloud.CloudDatabaseService
import eu.agribot.agribotapp.domain.service.cloud.ToolDataManager
import eu.agribot.agribotapp.ui.fragment.toolcontrol.data.HitchListItemDetail
import eu.agribot.agribotapp.ui.fragment.toolcontrol.recycler.HitchListItemViewModel

class ToolParamsViewModel(
    private val toolDataManager: ToolDataManager,
    private val cloudDatabaseService: CloudDatabaseService
) : BaseViewModel() {
    private val toastMessageEvent: MutableLiveEvent<String> = MutableLiveEvent()
    val paramsList: MutableLiveData<List<HitchListItemViewModel>> =
        MutableLiveData(listOf())

    private fun addToolToToolParamList(
        toolName: String,
        switchNumber: Int,
        hitchListItemDetail: HitchListItemDetail
    ) {
        val isToolAlreadyAdded = paramsList.value!!.any() { it ->
            it.toolName == toolName
        }

        if (!isToolAlreadyAdded) {
            paramsList.value = listOf(
                HitchListItemViewModel(
                    toolName = toolName,
                    switchNumber = switchNumber,
                    hitchListItemDetail = hitchListItemDetail
                ), *paramsList.value!!.toTypedArray()
            )
        } else {
            paramsList.value = paramsList.value!!.map { oldAttribute ->
                if (oldAttribute.toolName == toolName) {
                    HitchListItemViewModel(
                        toolName = toolName,
                        switchNumber = switchNumber,
                        hitchListItemDetail = hitchListItemDetail
                    )
                } else {
                    oldAttribute
                }
            }
        }
    }

    fun createTools() {
        addToolToToolParamList(
            toolName = "PTO",
            switchNumber = 0,
            hitchListItemDetail = HitchListItemDetail(
                toolStartValue = toolDataManager.getPto().toInt()
            )
        )
        addToolToToolParamList(
            toolName = "TPL UP/DOWN",
            switchNumber = 1,
            hitchListItemDetail = HitchListItemDetail(
                toolStartValue = toolDataManager.getTplUpDown().toInt()
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
        createTools()
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
