package eu.agribot.agribotapp.ui.fragment.toolmanagement.mainscreen

import android.util.Log
import androidx.lifecycle.MutableLiveData
import eu.agribot.agribotapp.R
import eu.agribot.agribotapp.base.BaseViewModel
import eu.agribot.agribotapp.common.livedata.MutableLiveEvent
import eu.agribot.agribotapp.common.navigation.events.ToolNameNavigationEvent
import eu.agribot.agribotapp.common.resources.ResourcesProvider
import eu.agribot.agribotapp.domain.model.ToolItem
import eu.agribot.agribotapp.domain.service.cloud.CloudDatabaseService
import eu.agribot.agribotapp.ui.fragment.toolmanagement.mainscreen.recycler.ToolListItemViewModel

class ToolManagementViewModel(
    private val resourcesProvider: ResourcesProvider,
    private val cloudDatabaseService: CloudDatabaseService
) : BaseViewModel() {
    val toolList: MutableLiveData<List<ToolListItemViewModel>> = MutableLiveData(listOf())
    val toastMessageEvent: MutableLiveEvent<String> = MutableLiveEvent()

    fun onNewToolButtonClicked() {
        navigationEvent.postValue(
            ToolNameNavigationEvent()
        )
    }

    fun onToolSelected(toolListItemViewModel: ToolListItemViewModel) {
//        cloudDatabaseService.getTaskDetails(
//            taskId = taskListItemViewModel.taskName,
//            onGetTaskDetails = this::onGotTaskDetails,
//            onGetTaskDetailsError = this::postToastErrorMessageEvent
//        )
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
//            toolList.postValue(listOf(ToolListItemViewModel("test")))
        }
    }

    fun onEditButtonClicked(positionOnList: Int) {
//        toolList.value?.get(positionOnList)?.taskName?.let { selectedTaskName ->
//            taskCreatorDataManager.onUpdateTaskName(
//                selectedTaskName
//            )
//            cloudDatabaseService.getTaskDetails(
//                selectedTaskName,
//                this::updateTaskDetails,
//                onGetTaskDetailsError = this::postToastErrorMessageEvent
//            )
//        }
//        navigationEvent.postValue(CreatorTaskPathNavigationEvent())
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
