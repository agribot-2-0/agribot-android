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

    private val toolParamsList: Map<String, ToolParamDetails> = mapOf(
        "PTO" to ToolParamDetails(
            sliderNumber = 1,
            value = toolDataManager.getPto().toInt()
        )
    )


    fun sendToolData() {
        postToolManagementNavigationEvent()
        cloudDatabaseService.addTool(
            toolName = toolDataManager.getToolName(),
            addToolDetails = AddToolDetails(
                rfid = toolDataManager.getRfid(),
                pto = toolDataManager.getPto()
            )
        )
    }

    fun onHitchParametersChanged(
        sliderNumber: Int,
        sliderValue: Int,
        isActivated: Boolean
    ) {
        if (toolParamsList["PTO"]!!.sliderNumber == sliderNumber) {
            toolDataManager.onUpdatePto(sliderValue.toDouble())
        }
    }

    fun onStart() {
        cloudDatabaseService.getToolDetails(
            toolName = toolDataManager.getToolName(),
            onGetToolDetails = this::onToolUpdate,
            onGetToolDetailsError = this::postToastErrorMessageEvent
        )
    }

    fun createParamsList() {
        for ((toolName, toolParamDetails) in toolParamsList) {
            addToParamsList(
                toolName,
                toolParamDetails.sliderNumber,
                toolParamDetails.value
            )
        }
    }

    private fun addToParamsList(toolName: String, switchNumber: Int, toolValue: Int) {
        paramsList.value = listOf(
            HitchListItemViewModel(
                toolName,
                switchNumber,
                toolValue
            ),
            *paramsList.value!!.toTypedArray()
        )
    }

    private fun onToolUpdate(toolDetails: ToolDetails) {
        toolDataManager.onUpdatePto(toolDetails.pto)
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
