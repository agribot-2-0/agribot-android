package eu.agribot.agribotapp.ui.fragment.taskcreator.mainscreen

import androidx.lifecycle.MutableLiveData
import com.google.android.gms.maps.model.MarkerOptions
import eu.agribot.agribotapp.R
import eu.agribot.agribotapp.base.BaseViewModel
import eu.agribot.agribotapp.common.livedata.MutableLiveEvent
import eu.agribot.agribotapp.common.navigation.events.CreatorTaskNameNavigationEvent
import eu.agribot.agribotapp.common.resources.ResourcesProvider
import eu.agribot.agribotapp.domain.model.TaskDetails
import eu.agribot.agribotapp.domain.model.TaskItem
import eu.agribot.agribotapp.domain.service.cloud.CloudDatabaseService
import eu.agribot.agribotapp.domain.service.cloud.TaskCreatorDataManager
import eu.agribot.agribotapp.ui.fragment.taskcreator.mainscreen.recycler.TaskListItemViewModel

class TaskCreatorViewModel(
    private val resourcesProvider: ResourcesProvider,
    private val cloudDatabaseService: CloudDatabaseService,
    private val taskCreatorDataManager: TaskCreatorDataManager
) : BaseViewModel() {
    val markers: MutableLiveData<MarkerOptions> = MutableLiveData()
    val toastMessageEvent: MutableLiveEvent<String> = MutableLiveEvent()
    val taskList: MutableLiveData<List<TaskListItemViewModel>> = MutableLiveData(listOf())
    val pathName: MutableLiveData<String> = MutableLiveData()
    val temperature: MutableLiveData<String> = MutableLiveData()
    val humidity: MutableLiveData<String> = MutableLiveData()
    val pressure: MutableLiveData<String> = MutableLiveData()
    val wind: MutableLiveData<String> = MutableLiveData()

    fun getAvailableTask() {
        cloudDatabaseService.getTaskItem(
            onGetTaskItem = this::onAvailableTaskFound,
            onGetTaskItemError = this::postToastErrorMessageEvent
        )
    }

    private fun onAvailableTaskFound(taskItem: TaskItem) {
        val isTaskAlreadyAdded = taskList.value!!.any { foundTask ->
            foundTask.taskName == taskItem.taskName
        }
        if (!isTaskAlreadyAdded) {
            taskList.value = listOf(
                TaskListItemViewModel(
                    taskItem.taskName,
                    resourcesProvider.getString(R.string.path_name) + taskItem.pathName
                ), *taskList.value!!.toTypedArray()
            )
        }
    }

    fun onDeleteButtonClicked(positionOnList: Int) {
        cloudDatabaseService.deleteSelectedTask(
            taskList.value?.get(positionOnList)!!.taskName,
            onDeleteError = this::postToastErrorMessageEvent,
            onDeleteSucess = this::onDeleteSucess
        )
    }

    private fun onDeleteSucess() {
        postToastInformationMessageEvent(resourcesProvider.getString(R.string.delete_sucess_text))
    }

    fun onEditButtonClicked(positionOnList: Int) {
        taskList.value?.get(positionOnList)?.taskName?.let { selectedTaskName ->
            taskCreatorDataManager.onUpdateTaskName(
                selectedTaskName
            )
            cloudDatabaseService.getTaskDetails(
                selectedTaskName,
                this::updateTaskDetails,
                onGetTaskDetailsError = this::postToastErrorMessageEvent
            )
        }


    }

    private fun updateTaskDetails(taskDetails: TaskDetails) {
        taskCreatorDataManager.updateAllTaskParameters(taskDetails)
    }

    fun onTaskSelected(taskListItemViewModel: TaskListItemViewModel) {
        cloudDatabaseService.getTaskDetails(
            taskId = taskListItemViewModel.taskName,
            onGetTaskDetails = this::onGotTaskDetails,
            onGetTaskDetailsError = this::postToastErrorMessageEvent
        )
    }

    private fun onGotTaskDetails(taskDetails: TaskDetails) {
        taskCreatorDataManager.updateAllTaskParameters(taskDetails)
        pathName.value =
            resourcesProvider.getString(R.string.path_name) + taskDetails.path_name
        temperature.value =
            taskDetails.temperature_condition + TaskDetailsElements.SEPARATOR.detailsElement + taskDetails.temperature_value + TaskDetailsElements.TEMPERATURE_UNIT.detailsElement
        humidity.value =
            taskDetails.humidity_condition + TaskDetailsElements.SEPARATOR.detailsElement + taskDetails.humidity_value + TaskDetailsElements.PERCENT.detailsElement
        pressure.value =
            taskDetails.pressure_condition + TaskDetailsElements.SEPARATOR.detailsElement + taskDetails.pressure_value + TaskDetailsElements.PRESSURE_UNIT.detailsElement
        wind.value =
            taskDetails.wind_condition + TaskDetailsElements.SEPARATOR.detailsElement + taskDetails.wind_value + TaskDetailsElements.SPEED_UNIT.detailsElement
    }

    fun onNewTaskButtonClicked() {
        navigationEvent.postValue(
            CreatorTaskNameNavigationEvent()
        )
    }

    private fun postToastErrorMessageEvent(errorMessage: String) {
        toastMessageEvent.postValue(errorMessage)
    }

    private fun postToastInformationMessageEvent(informationMessage: String) {
        toastMessageEvent.postValue(informationMessage)
    }


}
