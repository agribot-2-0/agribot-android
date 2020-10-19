package eu.agribot.agribotapp.ui.fragment.taskcreator.toolselect

import eu.agribot.agribotapp.base.BaseViewModel
import eu.agribot.agribotapp.common.navigation.events.DashboardNavigationEvent
import eu.agribot.agribotapp.domain.model.program.weather.toCommunication.NewRobotTaskDetails
import eu.agribot.agribotapp.domain.service.cloud.CloudDatabaseService
import eu.agribot.agribotapp.domain.service.cloud.TaskCreatorDataManager

class CreatorToolSelectViewModel(
    private val taskCreatorDataManager: TaskCreatorDataManager,
    private val cloudDatabaseService: CloudDatabaseService
) :
    BaseViewModel() {

    fun sendTaskData() {
        cloudDatabaseService.programTaskToRobot(
            taskName = taskCreatorDataManager.getTaskName(),
            newRobotTaskDetails = NewRobotTaskDetails(
                path_name = taskCreatorDataManager.getPathName().toString(),
                start_time = taskCreatorDataManager.getTimeStamp(),
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

        postDashboardNavigationEvent()
    }

    private fun postDashboardNavigationEvent() {
        navigationEvent.postValue(DashboardNavigationEvent())
    }
}
