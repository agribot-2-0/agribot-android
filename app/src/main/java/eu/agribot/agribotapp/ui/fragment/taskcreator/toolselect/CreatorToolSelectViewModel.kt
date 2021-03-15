package eu.agribot.agribotapp.ui.fragment.taskcreator.toolselect

import androidx.lifecycle.MutableLiveData
import com.google.firebase.Timestamp
import eu.agribot.agribotapp.R
import eu.agribot.agribotapp.base.BaseViewModel
import eu.agribot.agribotapp.common.livedata.MutableLiveEvent
import eu.agribot.agribotapp.common.navigation.events.DashboardNavigationEvent
import eu.agribot.agribotapp.common.resources.ResourcesProvider
import eu.agribot.agribotapp.domain.model.cdb.ToolItem
import eu.agribot.agribotapp.domain.model.cdb.taskprogram.taskmodel.NewRobotTaskDetails
import eu.agribot.agribotapp.domain.service.cloud.CloudDatabaseService
import eu.agribot.agribotapp.domain.service.cloud.TaskCreatorDataManager
import eu.agribot.agribotapp.domain.service.cloud.TaskCreatorDataManager.Companion.defaultTimestap
import eu.agribot.agribotapp.domain.service.cloud.TaskCreatorDataManager.Companion.defaultToolMode
import eu.agribot.agribotapp.domain.service.cloud.TaskCreatorDataManager.Companion.defaultToolName
import eu.agribot.agribotapp.ui.fragment.taskcreator.PathNameFileSymbol

class CreatorToolSelectViewModel(
    val taskCreatorDataManager: TaskCreatorDataManager,
    private val cloudDatabaseService: CloudDatabaseService,
    private val resourcesProvider: ResourcesProvider
) :
    BaseViewModel() {
    private val toastMessageEvent: MutableLiveEvent<String> = MutableLiveEvent()
    val mToolNames: MutableLiveData<List<String>> = MutableLiveData(listOf(defaultToolName))
    val mToolModes: MutableLiveData<List<String>> = MutableLiveData(
        listOf(
            resourcesProvider.getString(
                R.string.tool_mode_zero
            ), resourcesProvider.getString(
                R.string.tool_mode_one
            )
        )
    )
    var frontToolName: String = defaultToolName
    var frontToolMode: Int = defaultToolMode
    var backToolName: String = defaultToolName
    var backToolMode: Int = defaultToolMode

    private fun getTaskData() {
        cloudDatabaseService.addTask(
            taskName = taskCreatorDataManager.getTaskParemeters().task_name,
            newRobotTaskDetails = NewRobotTaskDetails(
                path_name = formatPathName(taskCreatorDataManager.getTaskParemeters().pathName),
                start_time = if (taskCreatorDataManager.getTaskParemeters().startTimeEstimated) {
                    Timestamp(defaultTimestap, 0)
                } else {
                    taskCreatorDataManager.getTaskParemeters().startTime
                },
                start_time_estimated = taskCreatorDataManager.getTaskParemeters().startTimeEstimated,
                temperature_value = taskCreatorDataManager.getTaskParemeters().temperatureValue,
                temperature_condition = taskCreatorDataManager.getTaskParemeters().temperatureCondition,
                wind_value = taskCreatorDataManager.getTaskParemeters().windValue,
                wind_condition = taskCreatorDataManager.getTaskParemeters().windCondition,
                humidity_value = taskCreatorDataManager.getTaskParemeters().humidityValue,
                humidity_condition = taskCreatorDataManager.getTaskParemeters().humidityCondition,
                pressure_value = taskCreatorDataManager.getTaskParemeters().pressureValue,
                pressure_condition = taskCreatorDataManager.getTaskParemeters().pressureCondition,
                front_tool_name = frontToolName,
                front_tool_mode = if (frontToolName == defaultToolName) {
                    defaultToolMode
                } else {
                    frontToolMode
                },
                back_tool_name = backToolName,
                back_tool_mode = if (backToolName == defaultToolName) {
                    defaultToolMode
                } else {
                    backToolMode
                }
            )
        )
    }

    fun sendTaskData() {
        getTaskData()
        postDashboardNavigationEvent()
    }

    fun getAvailableTool() {
        frontToolName = taskCreatorDataManager.getTaskParemeters().frontToolName
        frontToolMode = taskCreatorDataManager.getTaskParemeters().frontToolMode
        backToolName = taskCreatorDataManager.getTaskParemeters().backToolName
        backToolMode = taskCreatorDataManager.getTaskParemeters().backToolMode
        cloudDatabaseService.getToolItem(
            onGetToolItem = this::onAvailableToolFound,
            onGetToolItemError = this::postToastErrorMessageEvent
        )
    }

    private fun onAvailableToolFound(toolItem: ToolItem) {
        mToolNames.value =
            listOf(
                toolItem.toolName, *mToolNames.value!!.toTypedArray()
            )
    }

    private fun formatPathName(taskName: String): String {
        return if (taskName.contains(PathNameFileSymbol.VARSION_SEPARATOR.symbol)
        ) {
            taskName.substringBefore(PathNameFileSymbol.VARSION_SEPARATOR.symbol)
        } else {
            taskName.substringBefore(PathNameFileSymbol.FILE_FORMAT_SEPARATOR.symbol)
        }
    }

    private fun postDashboardNavigationEvent() {
        navigationEvent.postValue(DashboardNavigationEvent())
    }

    private fun postToastErrorMessageEvent(errorMessage: String) {
        toastMessageEvent.postValue(errorMessage)
    }
}
