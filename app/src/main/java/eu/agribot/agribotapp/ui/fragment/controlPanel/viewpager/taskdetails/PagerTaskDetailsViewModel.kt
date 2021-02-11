package eu.agribot.agribotapp.ui.fragment.controlPanel.viewpager.taskdetails

import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import eu.agribot.agribotapp.R
import eu.agribot.agribotapp.base.BaseViewModel
import eu.agribot.agribotapp.common.livedata.LiveEvent
import eu.agribot.agribotapp.common.livedata.MutableLiveEvent
import eu.agribot.agribotapp.common.resources.ResourcesProvider
import eu.agribot.agribotapp.domain.model.cdb.ActualTask
import eu.agribot.agribotapp.domain.model.cdb.TaskDetails
import eu.agribot.agribotapp.domain.service.cloud.CloudDatabaseService
import eu.agribot.agribotapp.ui.fragment.taskcreator.TaskDetailsElements
import java.text.SimpleDateFormat
import java.util.TimeZone

class PagerTaskDetailsViewModel(
    private val cloudDatabaseService: CloudDatabaseService,
    private val resourcesProvider: ResourcesProvider
) : BaseViewModel() {
    private val emptyTaskName = ""
    val currentTask: MutableLiveData<String> = MutableLiveData()
    val pathName: MutableLiveData<String> = MutableLiveData()
    val temperature: MutableLiveData<String> = MutableLiveData()
    val humidity: MutableLiveData<String> = MutableLiveData()
    val pressure: MutableLiveData<String> = MutableLiveData()
    val wind: MutableLiveData<String> = MutableLiveData()
    val taskDate: MutableLiveData<String> = MutableLiveData()
    val toastMessageEvent: LiveEvent<String> = MutableLiveEvent()
    val cancelTaskButtonEvent: MutableLiveEvent<Boolean> = MutableLiveEvent()

    fun onCancelTaskButtonClicked() {
        cancelTaskButtonEvent.postValue(true)
    }

    private fun setEmptyActualTask() {
        onGotCurrentTaskName(emptyTaskName)
        onGotCurrentTaskDetails(TaskDetails())
    }

    fun getActualTask() {
        setEmptyActualTask()
        cloudDatabaseService.getActualTaskDetails(
            onGetCurrentTaskDetailsSucess = this::onGotCurrentTaskDetails,
            onGetCurrentTaskNameSucess = this::onGotCurrentTaskName,
            onError = this::postToastErrorMessageEvent
        )
    }

    private fun onGotCurrentTaskName(taskName: String) {
        currentTask.value = taskName
    }

    @SuppressLint("SimpleDateFormat")
    private fun onGotCurrentTaskDetails(taskDetails: TaskDetails) {
        if (!taskDetails.start_time_estimated) {
            val dateFormat = SimpleDateFormat(TaskDetailsElements.DATE_FORMAT.detailsElement)
            dateFormat.timeZone = TimeZone.getTimeZone(TaskDetailsElements.TIMEZONE.detailsElement)
            taskDate.value =
                resourcesProvider.getString(R.string.start_time) + dateFormat.format(taskDetails.start_time!!.toDate())
        } else {
            taskDate.value = resourcesProvider.getString(R.string.no_start_date)
        }
        if (taskDetails.path_name != TaskDetailsElements.EMPTY_DETAIL_VALUE.detailsElement) {
            pathName.value = taskDetails.path_name
        } else {
            pathName.value = resourcesProvider.getString(R.string.no_path_text)
            currentTask.value = resourcesProvider.getString(R.string.empty_task_name)
        }

        if (taskDetails.temperature_value != TaskDetailsElements.EMPTY_DETAIL_VALUE.detailsElement) {
            temperature.value =
                changeTextToSymbol(taskDetails.temperature_condition) + TaskDetailsElements.SEPARATOR.detailsElement + taskDetails.temperature_value + TaskDetailsElements.TEMPERATURE_UNIT.detailsElement
        } else {
            temperature.value = resourcesProvider.getString(R.string.no_temperature_text)
        }

        if (taskDetails.humidity_value != TaskDetailsElements.EMPTY_DETAIL_VALUE.detailsElement) {
            humidity.value =
                changeTextToSymbol(taskDetails.humidity_condition) + TaskDetailsElements.SEPARATOR.detailsElement + taskDetails.humidity_value + TaskDetailsElements.PERCENT.detailsElement
        } else {
            humidity.value = resourcesProvider.getString(R.string.no_humidity_text)
        }

        if (taskDetails.pressure_value != TaskDetailsElements.EMPTY_DETAIL_VALUE.detailsElement) {
            pressure.value =
                changeTextToSymbol(taskDetails.pressure_condition) + TaskDetailsElements.SEPARATOR.detailsElement + taskDetails.pressure_value + TaskDetailsElements.PRESSURE_UNIT.detailsElement
        } else {
            pressure.value = resourcesProvider.getString(R.string.no_pressure_text)
        }

        if (taskDetails.wind_value != TaskDetailsElements.EMPTY_DETAIL_VALUE.detailsElement) {
            wind.value =
                changeTextToSymbol(taskDetails.wind_condition) + TaskDetailsElements.SEPARATOR.detailsElement + taskDetails.wind_value + TaskDetailsElements.SPEED_UNIT.detailsElement
        } else {
            wind.value = resourcesProvider.getString(R.string.no_wind_text)
        }
    }

    private fun changeTextToSymbol(conditionText: String): String {
        return when (conditionText) {
            TaskDetailsElements.LESS_TEXT.detailsElement -> TaskDetailsElements.LESS_SYMBOL.detailsElement
            TaskDetailsElements.MORE_TEXT.detailsElement -> TaskDetailsElements.MORE_SYMBOL.detailsElement
            TaskDetailsElements.EQUAL_TEXT.detailsElement -> TaskDetailsElements.EQUAL_SYMBOL.detailsElement
            else -> TaskDetailsElements.SEPARATOR.detailsElement
        }
    }

    private fun postToastErrorMessageEvent(errorMessage: String) {
        toastMessageEvent.postValue(errorMessage)
    }

    fun sendCancelTask() {
        cloudDatabaseService.setCurrentTask(ActualTask())
        setEmptyActualTask()
    }
}
