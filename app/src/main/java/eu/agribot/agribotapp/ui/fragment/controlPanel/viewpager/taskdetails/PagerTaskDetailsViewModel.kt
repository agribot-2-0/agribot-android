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
import eu.agribot.agribotapp.ui.fragment.diagnosticdatarecycler.RobotStateViewModel
import eu.agribot.agribotapp.ui.fragment.diagnosticdatarecycler.items.RobotStateItems
import eu.agribot.agribotapp.ui.fragment.taskcreator.TaskDetailsElements

class PagerTaskDetailsViewModel(
    private val cloudDatabaseService: CloudDatabaseService,
    private val resourcesProvider: ResourcesProvider
) : BaseViewModel() {

    val robotDataStateList: MutableLiveData<List<RobotStateViewModel>> = MutableLiveData(listOf())

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
        taskDetails.let {
            setCurrentTask(it)
            setCurrentPath(it)
            setStartDate(it)
            setStartTime(it)
            setTemperatureParam(it)
            setHumidityParam(it)
            setWindParam(it)
            setPressureParam(it)
        }


    }

    private fun setStartDate(taskDetails: TaskDetails) {
        addParameterToRobotDataStateList(
            RobotStateViewModel(
                RobotStateItems.START_DATE.ID,
                changeTextToSymbol(taskDetails.temperature_condition) + taskDetails.temperature_value,
                TaskDetailsElements.TEMPERATURE_UNIT.detailsElement
            )
        )
    }

    private fun setStartTime(taskDetails: TaskDetails) {
        addParameterToRobotDataStateList(
            RobotStateViewModel(
                RobotStateItems.START_TIME.ID,
                changeTextToSymbol(taskDetails.temperature_condition) + taskDetails.temperature_value,
                TaskDetailsElements.TEMPERATURE_UNIT.detailsElement
            )
        )
    }

    private fun setCurrentTask(taskDetails: TaskDetails) {
        if (taskDetails.temperature_value != TaskDetailsElements.EMPTY_DETAIL_VALUE.detailsElement) {
            addParameterToRobotDataStateList(
                RobotStateViewModel(
                    RobotStateItems.CURRENT_TASK.ID,
                    changeTextToSymbol(taskDetails.temperature_condition) + taskDetails.temperature_value,
                    TaskDetailsElements.TEMPERATURE_UNIT.detailsElement
                )
            )
        } else {
            addParameterToRobotDataStateList(
                RobotStateViewModel(
                    RobotStateItems.CURRENT_TASK.ID,
                    resourcesProvider.getString(R.string.no_temperature_text),
                    TaskDetailsElements.TEMPERATURE_UNIT.detailsElement
                )
            )
        }
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
            addParameterToRobotDataStateList(
                RobotStateViewModel(
                    RobotStateItems.CURRENT_PATH.ID,
                    resourcesProvider.getString(R.string.empty_task_name),
                    ""
                )
            )
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
            addParameterToRobotDataStateList(
                RobotStateViewModel(
                    RobotStateItems.HUMIDITY.ID,
                    resourcesProvider.getString(R.string.no_temperature_text),
                    TaskDetailsElements.PERCENT.detailsElement
                )
            )
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
            addParameterToRobotDataStateList(
                RobotStateViewModel(
                    RobotStateItems.TEMPERATURE.ID,
                    resourcesProvider.getString(R.string.no_temperature_text),
                    TaskDetailsElements.TEMPERATURE_UNIT.detailsElement
                )
            )
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
            addParameterToRobotDataStateList(
                RobotStateViewModel(
                    RobotStateItems.WIND.ID,
                    resourcesProvider.getString(R.string.no_wind_text),
                    TaskDetailsElements.SPEED_UNIT.detailsElement
                )
            )
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
            addParameterToRobotDataStateList(
                RobotStateViewModel(
                    RobotStateItems.PRESSURE.ID,
                    resourcesProvider.getString(R.string.no_pressure_text),
                    TaskDetailsElements.SPEED_UNIT.detailsElement
                )
            )
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


    private fun addParameterToRobotDataStateList(robotStateParameter: RobotStateViewModel) {
        val isAttributeAlreadyAdded = robotDataStateList.value!!.any { arrtibute ->
            arrtibute.stateItemId == robotStateParameter.stateItemId
        }
        if (!isAttributeAlreadyAdded)
            robotDataStateList.value = listOf(  *robotDataStateList.value!!.toTypedArray(),
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

    fun sendCancelTask() {
        cloudDatabaseService.setCurrentTask(ActualTask())
        setEmptyActualTask()
    }
}
