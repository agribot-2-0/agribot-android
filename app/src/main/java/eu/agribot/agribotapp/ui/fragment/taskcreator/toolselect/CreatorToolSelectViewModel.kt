package eu.agribot.agribotapp.ui.fragment.taskcreator.toolselect

import com.google.firebase.Timestamp
import eu.agribot.agribotapp.base.BaseViewModel
import eu.agribot.agribotapp.common.navigation.events.DashboardNavigationEvent
import eu.agribot.agribotapp.domain.model.cdb.taskprogram.taskmodel.NewRobotTaskDetails
import eu.agribot.agribotapp.domain.service.cloud.CloudDatabaseService
import eu.agribot.agribotapp.domain.service.cloud.TaskCreatorDataManager

class CreatorToolSelectViewModel(
    private val taskCreatorDataManager: TaskCreatorDataManager,
    private val cloudDatabaseService: CloudDatabaseService
) :
    BaseViewModel() {
    private val NULL_VALUE_TIMESTAMP = 946728000L
    fun sendTaskData() {
        postDashboardNavigationEvent()
        cloudDatabaseService.programTaskToRobot(
            taskName = taskCreatorDataManager.getTaskName(),
            newRobotTaskDetails = NewRobotTaskDetails(
                path_name = formatPathName(),
                start_time = if (taskCreatorDataManager.getTimeEstimated()) {
                    Timestamp(NULL_VALUE_TIMESTAMP, 0)
                } else {
                    taskCreatorDataManager.getTimeStamp()
                },
                start_time_estimated = taskCreatorDataManager.getTimeEstimated(),
                temperature_value = taskCreatorDataManager.getTemperatureValue(),
                temperature_condition = taskCreatorDataManager.getTemperatureCondition(),
                wind_value = taskCreatorDataManager.getWindValue(),
                wind_condition = taskCreatorDataManager.getWindCondition(),
                humidity_value = taskCreatorDataManager.getHumidityValue(),
                humidity_condition = taskCreatorDataManager.getHumidityCondition(),
                pressure_value = taskCreatorDataManager.getPressureValue(),
                pressure_condition = taskCreatorDataManager.getPressureCondition()
            )
        )
    }

    private fun formatPathName(): String {
        return if (taskCreatorDataManager.getPathName().toString().contains("_")) {
            taskCreatorDataManager.getPathName().toString().substringBefore("_")
        } else {
            taskCreatorDataManager.getPathName().toString().substringBefore(".")
        }
    }

    private fun postDashboardNavigationEvent() {
        navigationEvent.postValue(DashboardNavigationEvent())
    }
}
