package eu.agribot.agribotapp.ui.fragment.toolmanagement.mainscreen

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.MutableLiveData
import eu.agribot.agribotapp.R
import eu.agribot.agribotapp.base.BaseViewModel
import eu.agribot.agribotapp.common.livedata.MutableLiveEvent
import eu.agribot.agribotapp.common.navigation.events.ToolNameNavigationEvent
import eu.agribot.agribotapp.common.navigation.events.ToolParamsNavigationEvent
import eu.agribot.agribotapp.common.resources.ResourcesProvider
import eu.agribot.agribotapp.domain.model.TaskDetails
import eu.agribot.agribotapp.domain.model.ToolDetails
import eu.agribot.agribotapp.domain.model.ToolItem
import eu.agribot.agribotapp.domain.service.cloud.CloudDatabaseService
import eu.agribot.agribotapp.domain.service.cloud.TaskCreatorDataManager
import eu.agribot.agribotapp.domain.service.cloud.ToolDataManager
import eu.agribot.agribotapp.ui.fragment.toolmanagement.mainscreen.recycler.ToolListItemViewModel

class ToolManagementViewModel(
    private val resourcesProvider: ResourcesProvider,
    private val cloudDatabaseService: CloudDatabaseService,
    private val toolDataManager: ToolDataManager
) : BaseViewModel() {
    val toolList: MutableLiveData<List<ToolListItemViewModel>> = MutableLiveData(listOf())
    val toastMessageEvent: MutableLiveEvent<String> = MutableLiveEvent()
    val ptoValue: MutableLiveData<Int> = MutableLiveData()
    val ptoString: MutableLiveData<String> = MutableLiveData()
    val tplUpDownValue: MutableLiveData<Int> = MutableLiveData()
    val tplUpDownString: MutableLiveData<String> = MutableLiveData()
    val tplPushPullValue: MutableLiveData<Int> = MutableLiveData()
    val tplPushPullString: MutableLiveData<String> = MutableLiveData()


    fun onNewToolButtonClicked() {
        navigationEvent.postValue(
            ToolNameNavigationEvent()
        )
    }

    fun onToolSelected(toolListItemViewModel: ToolListItemViewModel) {
        cloudDatabaseService.getToolDetails(
            toolName = toolListItemViewModel.toolName,
            onGetToolDetails = this::onGetToolDetails,
            onGetToolDetailsError = this::postToastErrorMessageEvent
        )
    }

    @SuppressLint("SimpleDateFormat")
    private fun onGetToolDetails(toolDetails: ToolDetails) {
        toolDataManager.updateAllToolParameters(toolDetails)
        ptoValue.value = toolDetails.pto.toInt()
        ptoString.value = toolDetails.pto.toString() + " rpm"

        tplUpDownValue.value = toolDetails.tpl_up_down.toInt()
        tplPushPullValue.value = toolDetails.tpl_push_pull.toInt()
    }

    fun onDeleteButtonClicked(positionOnList: Int) {
        cloudDatabaseService.deleteTool(
            toolList.value?.get(positionOnList)!!.toolName,
            onDeleteError = this::postToastErrorMessageEvent,
            onDeleteSucess = this::onDeleteSucess
        )
        toolList.value = toolList.value!!.minus(
            ToolListItemViewModel(
                toolList.value?.get(positionOnList)!!.toolName)
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
            Log.w("ToolManagementViewModel", "onAvailableToolFound")
            toolList.value = listOf(
                ToolListItemViewModel(
                    toolItem.toolName,
                ), *toolList.value!!.toTypedArray()
            )
        }
    }

    fun onEditButtonClicked(positionOnList: Int) {
        toolList.value?.get(positionOnList)?.toolName?.let { toolName ->
            toolDataManager.onUpdateToolName(
                toolName
            )
            cloudDatabaseService.getToolDetails(
                toolName,
                onGetToolDetails = this::updateToolDetails,
                onGetToolDetailsError = this::postToastErrorMessageEvent
            )
        }
        navigationEvent.postValue(ToolParamsNavigationEvent())
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
