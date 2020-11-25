package eu.agribot.agribotapp.ui.fragment.taskcreator.toolselect

import androidx.lifecycle.MutableLiveData
import com.google.firebase.Timestamp
import eu.agribot.agribotapp.base.BaseViewModel
import eu.agribot.agribotapp.common.livedata.MutableLiveEvent
import eu.agribot.agribotapp.common.navigation.events.DashboardNavigationEvent
import eu.agribot.agribotapp.domain.model.cdb.ToolItem
import eu.agribot.agribotapp.domain.model.cdb.taskprogram.taskmodel.NewRobotTaskDetails
import eu.agribot.agribotapp.domain.service.cloud.CloudDatabaseService
import eu.agribot.agribotapp.domain.service.cloud.TaskCreatorDataManager
import eu.agribot.agribotapp.ui.fragment.taskcreator.PathNameFileSymbol
import kotlinx.android.synthetic.main.fragment_creator_tool_select.front_tool_name

class CreatorToolSelectViewModel(
    private val taskCreatorDataManager: TaskCreatorDataManager,
    private val cloudDatabaseService: CloudDatabaseService
) :
    BaseViewModel() {
    private val NULL_VALUE_TIMESTAMP = 946728000L
    private val toastMessageEvent: MutableLiveEvent<String> = MutableLiveEvent()
    val mToolNames: MutableLiveData<List<String>> = MutableLiveData(listOf(""))
    val mToolModes: MutableLiveData<List<String>> = MutableLiveData(listOf("Cala trasa", "Miedzyrzedzie"))

    private fun getTaskData() {
        cloudDatabaseService.addTask(
            taskName = taskCreatorDataManager.getTaskParemeters().task_name,
            newRobotTaskDetails = NewRobotTaskDetails(
                path_name = formatPathName(taskCreatorDataManager.getTaskParemeters().path_name),
                start_time = if (taskCreatorDataManager.getTaskParemeters().start_time_estimated) {
                    Timestamp(NULL_VALUE_TIMESTAMP, 0)
                } else {
                    taskCreatorDataManager.getTaskParemeters().start_time
                },
                start_time_estimated = taskCreatorDataManager.getTaskParemeters().start_time_estimated,
                temperature_value = taskCreatorDataManager.getTaskParemeters().temperature_value,
                temperature_condition = taskCreatorDataManager.getTaskParemeters().temperature_condition,
                wind_value = taskCreatorDataManager.getTaskParemeters().wind_value,
                wind_condition = taskCreatorDataManager.getTaskParemeters().wind_condition,
                humidity_value = taskCreatorDataManager.getTaskParemeters().humidity_value,
                humidity_condition = taskCreatorDataManager.getTaskParemeters().humidity_condition,
                pressure_value = taskCreatorDataManager.getTaskParemeters().pressure_value,
                pressure_condition = taskCreatorDataManager.getTaskParemeters().pressure_condition,
                front_tool_name = taskCreatorDataManager.getTaskParemeters().front_tool_name,
                front_tool_mode = taskCreatorDataManager.getTaskParemeters().front_tool_mode,
                back_tool_name = taskCreatorDataManager.getTaskParemeters().back_tool_name,
                back_tool_mode = taskCreatorDataManager.getTaskParemeters().back_tool_mode
            )
        )
    }

    fun sendTaskData() {
        getTaskData()
        postDashboardNavigationEvent()
    }

    fun getAvailableTool() {
        cloudDatabaseService.getToolItem(
            onGetToolItem = this::onAvailableToolFound,
            onGetToolItemError = this::postToastErrorMessageEvent
        )
    }

    fun setFrontToolName(toolName: String) {
        taskCreatorDataManager.onUpdateFrontToolName(toolName)
    }

    fun setFrontToolMode(toolName: String) {
        taskCreatorDataManager.onUpdateFrontToolMode(toolName)
    }

    fun setBackToolName(toolName: String) {
        taskCreatorDataManager.onUpdateBackToolName(toolName)
    }

    fun setBackToolMode(toolName: String) {
        taskCreatorDataManager.onUpdateBackToolMode(toolName)
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
