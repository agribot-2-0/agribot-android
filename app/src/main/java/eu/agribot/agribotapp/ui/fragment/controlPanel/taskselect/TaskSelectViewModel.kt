package eu.agribot.agribotapp.ui.fragment.controlPanel.taskselect

import androidx.lifecycle.MutableLiveData
import eu.agribot.agribotapp.base.BaseViewModel
import eu.agribot.agribotapp.common.livedata.MutableLiveEvent
import eu.agribot.agribotapp.domain.model.ActualTask
import eu.agribot.agribotapp.domain.model.TaskItem
import eu.agribot.agribotapp.domain.service.cloud.CloudDatabaseService
import eu.agribot.agribotapp.ui.fragment.taskcreator.mainscreen.recycler.TaskListItemViewModel

class TaskSelectViewModel(
    private val cloudDatabaseService: CloudDatabaseService
) : BaseViewModel() {
    val taskList: MutableLiveData<List<TaskListItemViewModel>> = MutableLiveData(listOf())
    val toastEvent: MutableLiveEvent<String> = MutableLiveEvent()

    fun getAvailableTask() {
        cloudDatabaseService.getTaskItem(
            onGetTaskItem = this::onAvailableTaskFound,
            onGetTaskItemError = this::postToastErrorMessageEvent
        )
    }

    fun setCurrentTask(taskName: String) {
        cloudDatabaseService.setCurrentTask(
            ActualTask(
                actual_task = taskName
            )
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
                    taskItem.pathName
                ), *taskList.value!!.toTypedArray()
            )
        }
    }

    private fun postToastErrorMessageEvent(toastText: String) {
        toastEvent.postValue(toastText)
    }
}
