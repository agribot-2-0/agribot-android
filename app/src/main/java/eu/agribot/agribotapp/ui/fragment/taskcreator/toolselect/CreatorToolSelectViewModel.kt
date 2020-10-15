package eu.agribot.agribotapp.ui.fragment.taskcreator.toolselect

import eu.agribot.agribotapp.base.BaseViewModel
import eu.agribot.agribotapp.domain.model.program.weather.toCommunication.NewRobotTaskDetails
import eu.agribot.agribotapp.domain.service.AutonomyService
import eu.agribot.agribotapp.domain.service.cloud.TaskCreatorDataManager

class CreatorToolSelectViewModel(
    private val taskCreatorDataManager: TaskCreatorDataManager,
    private val autonomyService: AutonomyService
) :
    BaseViewModel() {

    fun sendTaskData() {
        taskCreatorDataManager.getPathName()?.let {
            NewRobotTaskDetails(
                path_name = it,
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
        }?.let {
            autonomyService.programTaskToRobot(
                taskName = taskCreatorDataManager.getTaskName(),
                newRobotTaskDetails = it
            )
        }
    }
}
