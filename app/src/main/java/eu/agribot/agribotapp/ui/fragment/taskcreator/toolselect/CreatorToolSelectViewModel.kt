package eu.agribot.agribotapp.ui.fragment.taskcreator.toolselect

import com.google.firebase.Timestamp
import eu.agribot.agribotapp.base.BaseViewModel
import eu.agribot.agribotapp.common.navigation.events.DashboardNavigationEvent
import eu.agribot.agribotapp.domain.model.cdb.TaskDetails
import eu.agribot.agribotapp.domain.model.cdb.taskprogram.taskmodel.NewRobotTaskDetails
import eu.agribot.agribotapp.domain.service.cloud.CloudDatabaseService
import eu.agribot.agribotapp.domain.service.cloud.TaskCreatorDataManager
import eu.agribot.agribotapp.ui.fragment.taskcreator.PathNameFileSymbol

class CreatorToolSelectViewModel(
    private val taskCreatorDataManager: TaskCreatorDataManager,
    private val cloudDatabaseService: CloudDatabaseService
) :
    BaseViewModel() {
    private val NULL_VALUE_TIMESTAMP = 946728000L

    fun getTaskData() {
        taskCreatorDataManager.getTaskParemeters(this::onGotTaskParametrs)
    }

    private fun onGotTaskParametrs(taskDetails: TaskDetails) {
        cloudDatabaseService.programTaskToRobot(
            taskName = taskDetails.task_name,
            newRobotTaskDetails = NewRobotTaskDetails(
                path_name = formatPathName(taskDetails.path_name),
                start_time = if (taskDetails.start_time_estimated) {
                    Timestamp(NULL_VALUE_TIMESTAMP, 0)
                } else {
                    taskDetails.start_time
                },
                start_time_estimated = taskDetails.start_time_estimated,
                temperature_value = taskDetails.temperature_value,
                temperature_condition = taskDetails.temperature_condition,
                wind_value = taskDetails.wind_value,
                wind_condition = taskDetails.wind_condition,
                humidity_value = taskDetails.humidity_value,
                humidity_condition = taskDetails.humidity_condition,
                pressure_value = taskDetails.pressure_value,
                pressure_condition = taskDetails.pressure_condition
            )
        )
    }

    fun sendTaskData() {
        getTaskData()
        postDashboardNavigationEvent()
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
}
