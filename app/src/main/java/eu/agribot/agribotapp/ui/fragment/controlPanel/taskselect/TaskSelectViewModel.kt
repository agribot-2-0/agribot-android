package eu.agribot.agribotapp.ui.fragment.controlPanel.taskselect

import androidx.lifecycle.MutableLiveData
import eu.agribot.agribotapp.base.BaseViewModel
import eu.agribot.agribotapp.common.livedata.MutableLiveEvent
import eu.agribot.agribotapp.common.navigation.events.DashboardNavigationEvent
import eu.agribot.agribotapp.domain.model.cdb.ActualTask
import eu.agribot.agribotapp.domain.model.cdb.TaskItem
import eu.agribot.agribotapp.domain.service.cloud.CloudDatabaseService
import eu.agribot.agribotapp.ui.fragment.taskcreator.mainscreen.recycler.TaskListItemViewModel

class TaskSelectViewModel(
    private val cloudDatabaseService: CloudDatabaseService
) : BaseViewModel() {
    val taskList: MutableLiveData<List<TaskListItemViewModel>> = MutableLiveData(listOf())
    val toastEvent: MutableLiveEvent<String> = MutableLiveEvent()
    val cancelTaskButtonEvent: MutableLiveEvent<Boolean> = MutableLiveEvent()
    private val currentTaskName: MutableLiveData<String> = MutableLiveData()
    val adapterEvent: MutableLiveEvent<Boolean> = MutableLiveEvent()

    fun onCancelTaskButtonClicked() {
        cancelTaskButtonEvent.postValue(true)
    }

    fun getAvailableTask() {
        cloudDatabaseService.getTaskItem(
            onGetTaskItem = this::onAvailableTaskFound,
            onGetTaskItemError = this::postToastErrorMessageEvent
        )
    }

    fun setCurrentTask(taskName: String) {
        cloudDatabaseService.setCurrentTask(
            ActualTask(
                actualTask = taskName
            )
        )
        postDashboardNavigationEvent()
    }

    private fun onAvailableTaskFound(taskItem: TaskItem) {
        val isTaskAlreadyAdded = taskList.value!!.any { foundTask ->
            foundTask.taskName == taskItem.taskName
        }
        if (!isTaskAlreadyAdded) {
            taskList.value = listOf(
                TaskListItemViewModel(
                    taskItem.taskName,
                    taskItem.pathName,
                    checkIsTaskIsActual(taskItem.taskName)
                ), *taskList.value!!.toTypedArray()
            )
        }
    }

    fun getCurrentTask() {
        cloudDatabaseService.getCurrentTaskName(onGotCurrentTaskName = this::setCurrentTaskLiveData)
    }

    private fun setCurrentTaskLiveData(currentTask: String) {
        currentTaskName.value = currentTask
        if (currentTask == EMPTY_TASK_SYMBOL) {
            distableTaskOnList()
        }
    }

    private fun checkIsTaskIsActual(taskName: String): Boolean {
        return taskName == currentTaskName.value
    }

    private fun postToastErrorMessageEvent(toastText: String) {
        toastEvent.postValue(toastText)
    }

    private fun postDashboardNavigationEvent() {
        navigationEvent.postValue(DashboardNavigationEvent())
    }

    fun sendCancelTask() {
        cloudDatabaseService.setCurrentTask(ActualTask())
        distableTaskOnList()
    }

    private fun distableTaskOnList() {
        taskList.value?.forEach {
            if (it.isActiveTask) {
                it.isActiveTask = false
            }
        }
        adapterEvent.postValue(true)
    }

    companion object {
        const val EMPTY_TASK_SYMBOL = ""
    }
}
