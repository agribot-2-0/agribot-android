package eu.agribot.agribotapp.ui.fragment.controlPanel.viewpager.taskdetails

import android.annotation.SuppressLint
import android.text.format.DateFormat
import androidx.lifecycle.MutableLiveData
import eu.agribot.agribotapp.R
import eu.agribot.agribotapp.base.BaseViewModel
import eu.agribot.agribotapp.common.livedata.LiveEvent
import eu.agribot.agribotapp.common.livedata.MutableLiveEvent
import eu.agribot.agribotapp.common.resources.ResourcesProvider
import eu.agribot.agribotapp.domain.model.cdb.TaskDetails
import eu.agribot.agribotapp.domain.service.cloud.CloudDatabaseService
import eu.agribot.agribotapp.ui.fragment.diagnosticdatarecycler.RobotStateViewModel
import eu.agribot.agribotapp.ui.fragment.diagnosticdatarecycler.items.RobotStateItems
import eu.agribot.agribotapp.ui.fragment.taskcreator.TaskDetailsElements
import java.util.Calendar
import java.util.Locale

class PagerTaskDetailsViewModel(
    private val cloudDatabaseService: CloudDatabaseService,
    private val resourcesProvider: ResourcesProvider
) : BaseViewModel() {
    val robotDataStateList: MutableLiveData<List<RobotStateViewModel>> = MutableLiveData(listOf())
    private val emptyTaskName = ""
    private val currentTask: MutableLiveData<String> = MutableLiveData()
    val pathName: MutableLiveData<String> = MutableLiveData()
    val toastMessageEvent: LiveEvent<String> = MutableLiveEvent()

    private fun setEmptyActualTask() {
        onGotCurrentTaskName(emptyTaskName)
        onGotCurrentTaskDetails(TaskDetails())
    }

    fun getActualTask() {
        setEmptyActualTask()
        cloudDatabaseService.getActualTaskDetails(
            onGetCurrentTaskDetailsSuccess = this::onGotCurrentTaskDetails,
            onGetCurrentTaskNameSuccess = this::onGotCurrentTaskName,
            onError = this::postToastErrorMessageEvent
        )
    }

    private fun onGotCurrentTaskName(taskName: String) {
        currentTask.value = taskName
    }

    @SuppressLint("SimpleDateFormat")
    private fun onGotCurrentTaskDetails(taskDetails: TaskDetails) {
        (if (currentTask.value != null && currentTask.value != "") {
            setCurrentTask(currentTask.value!!)
        } else {
            setEmptyParameter(RobotStateItems.CURRENT_TASK.ID)
        })
        taskDetails.let {
            setCurrentPath(it)
            setStartTime(it)
            setStartDate(it)
            setTemperatureParam(it)
            setHumidityParam(it)
            setWindParam(it)
            setPressureParam(it)
        }
    }

    private fun setStartDate(taskDetails: TaskDetails) {
        if (!taskDetails.start_time_estimated) {
            addParameterToRobotDataStateList(
                RobotStateViewModel(
                    RobotStateItems.START_DATE.ID,
                    taskDetails.start_time?.let { getDate(it.seconds) }.toString(),
                    ""
                )
            )
        } else {
            setEmptyParameter(RobotStateItems.START_TIME.ID)
        }
    }

    private fun getDate(timestamp: Long): String {
        val calendar = Calendar.getInstance(Locale.ENGLISH)
        calendar.timeInMillis = timestamp * 1000L
        return DateFormat.format(TimePatterns.DATE.pattern, calendar).toString()
    }

    private fun getTime(timestamp: Long): String {
        val calendar = Calendar.getInstance(Locale.ENGLISH)
        calendar.timeInMillis = timestamp * 1000L
        return DateFormat.format(TimePatterns.TIME.pattern, calendar).toString()
    }

    private fun setStartTime(taskDetails: TaskDetails) {
        if (!taskDetails.start_time_estimated) {
            addParameterToRobotDataStateList(
                RobotStateViewModel(
                    RobotStateItems.START_TIME.ID,
                    taskDetails.start_time?.let { getTime(it.seconds) }.toString(),
                    ""
                )
            )
        } else {
            setEmptyParameter(RobotStateItems.START_DATE.ID)
        }
    }

    private fun setCurrentTask(taskName: String) {
        addParameterToRobotDataStateList(
            RobotStateViewModel(
                RobotStateItems.CURRENT_TASK.ID,
                taskName,
                ""
            )
        )
    }

    private fun setCurrentPath(taskDetails: TaskDetails) {
        if (taskDetails.path_name != TaskDetailsElements.EMPTY_DETAIL_VALUE.detailsElement) {
            addParameterToRobotDataStateList(
                RobotStateViewModel(
                    RobotStateItems.CURRENT_PATH.ID,
                    taskDetails.path_name,
                    ""
                )
            )
        } else {
            setEmptyParameter(RobotStateItems.CURRENT_PATH.ID)
        }
    }

    private fun setHumidityParam(taskDetails: TaskDetails) {
        if (taskDetails.humidity_value != TaskDetailsElements.EMPTY_DETAIL_VALUE.detailsElement) {
            addParameterToRobotDataStateList(
                RobotStateViewModel(
                    RobotStateItems.HUMIDITY.ID,
                    changeTextToSymbol(taskDetails.humidity_condition) + taskDetails.humidity_value,
                    TaskDetailsElements.PERCENT.detailsElement
                )
            )
        } else {
            setEmptyParameter(RobotStateItems.HUMIDITY.ID)
        }
    }

    private fun setTemperatureParam(taskDetails: TaskDetails) {
        if (taskDetails.temperature_value != TaskDetailsElements.EMPTY_DETAIL_VALUE.detailsElement) {
            addParameterToRobotDataStateList(
                RobotStateViewModel(
                    RobotStateItems.TEMPERATURE.ID,
                    changeTextToSymbol(taskDetails.temperature_condition) + taskDetails.temperature_value,
                    TaskDetailsElements.TEMPERATURE_UNIT.detailsElement
                )
            )
        } else {
            setEmptyParameter(RobotStateItems.TEMPERATURE.ID)
        }
    }

    private fun setWindParam(taskDetails: TaskDetails) {
        if (taskDetails.wind_value != TaskDetailsElements.EMPTY_DETAIL_VALUE.detailsElement) {
            addParameterToRobotDataStateList(
                RobotStateViewModel(
                    RobotStateItems.WIND.ID,
                    changeTextToSymbol(taskDetails.wind_condition) + taskDetails.wind_value,
                    TaskDetailsElements.SPEED_UNIT.detailsElement
                )
            )
        } else {
            setEmptyParameter(RobotStateItems.WIND.ID)
        }
    }

    private fun setPressureParam(taskDetails: TaskDetails) {
        if (taskDetails.pressure_value != TaskDetailsElements.EMPTY_DETAIL_VALUE.detailsElement) {
            addParameterToRobotDataStateList(
                RobotStateViewModel(
                    RobotStateItems.PRESSURE.ID,
                    changeTextToSymbol(taskDetails.pressure_condition) + taskDetails.pressure_value,
                    TaskDetailsElements.PRESSURE_UNIT.detailsElement
                )
            )
        } else {
            setEmptyParameter(RobotStateItems.PRESSURE.ID)
        }
    }

    private fun setEmptyParameter(paramID: Int) {
        addParameterToRobotDataStateList(
            RobotStateViewModel(
                paramID,
                resourcesProvider.getString(R.string.task_param_not_set), ""
            )
        )
    }

    private fun changeTextToSymbol(conditionText: String): String {
        return when (conditionText) {
            TaskDetailsElements.LESS_TEXT.detailsElement -> TaskDetailsElements.LESS_SYMBOL.detailsElement
            TaskDetailsElements.MORE_TEXT.detailsElement -> TaskDetailsElements.MORE_SYMBOL.detailsElement
            TaskDetailsElements.EQUAL_TEXT.detailsElement -> TaskDetailsElements.EQUAL_SYMBOL.detailsElement
            else -> TaskDetailsElements.SEPARATOR.detailsElement
        }
    }

    private fun addParameterToRobotDataStateList(robotStateParameter: RobotStateViewModel) {
        val isAttributeAlreadyAdded = robotDataStateList.value!!.any { arrtibute ->
            arrtibute.stateItemId == robotStateParameter.stateItemId
        }
        if (!isAttributeAlreadyAdded)
            robotDataStateList.value = listOf(
                *robotDataStateList.value!!.toTypedArray(),
                RobotStateViewModel(
                    robotStateParameter.stateItemId,
                    robotStateParameter.stateValue,
                    robotStateParameter.stateUnit
                )

            )
        else {
            robotDataStateList.value = robotDataStateList.value!!.map { oldAttribute ->
                if (oldAttribute.stateItemId == robotStateParameter.stateItemId) {
                    RobotStateViewModel(
                        oldAttribute.stateItemId,
                        robotStateParameter.stateValue,
                        oldAttribute.stateUnit
                    )
                } else {
                    oldAttribute
                }
            }
        }
    }

    private fun postToastErrorMessageEvent(errorMessage: String) {
        toastMessageEvent.postValue(errorMessage)
    }
}
