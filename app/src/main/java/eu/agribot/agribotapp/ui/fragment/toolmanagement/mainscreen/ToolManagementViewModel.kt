package eu.agribot.agribotapp.ui.fragment.toolmanagement.mainscreen

import androidx.lifecycle.MutableLiveData
import eu.agribot.agribotapp.R
import eu.agribot.agribotapp.base.BaseViewModel
import eu.agribot.agribotapp.common.livedata.MutableLiveEvent
import eu.agribot.agribotapp.common.navigation.events.ToolNameNavigationEvent
import eu.agribot.agribotapp.common.resources.ResourcesProvider
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

    fun onTaskSelected(toolListItemViewModel: ToolListItemViewModel) {
//        cloudDatabaseService.getTaskDetails(
//            taskId = taskListItemViewModel.taskName,
//            onGetTaskDetails = this::onGotTaskDetails,
//            onGetTaskDetailsError = this::postToastErrorMessageEvent
//        )
    }

    fun onDeleteButtonClicked(positionOnList: Int) {
        cloudDatabaseService.deleteSelectedTask(
            toolList.value?.get(positionOnList)!!.toolName,
            onDeleteError = this::postToastErrorMessageEvent,
            onDeleteSucess = this::onDeleteSucess
        )
//        toolList.value = toolList.value!!.minus(
//            TaskListItemViewModel(
//                toolList.value?.get(positionOnList)!!.toolName)
//        )
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
