package eu.agribot.agribotapp.ui.fragment.toolmanagement.toolparams

import com.google.firebase.Timestamp
import eu.agribot.agribotapp.base.BaseViewModel
import eu.agribot.agribotapp.common.navigation.events.ToolNavigationEvent


class ToolParamsViewModel() : BaseViewModel() {
//    fun onTaskNameChanged(text: CharSequence) {
//    }
//
//    fun onNextButtonClicked() {
//
//    }

    fun sendToolData() {
        postToolManagementNavigationEvent()
//        cloudDatabaseService.programTaskToRobot(
//            taskName = taskCreatorDataManager.getTaskName(),
//            newRobotTaskDetails = NewRobotTaskDetails(
//                path_name = formatPathName(),
//                start_time = if (taskCreatorDataManager.getTimeEstimated()) {
//                    Timestamp(NULL_VALUE_TIMESTAMP, 0)
//                } else {
//                    taskCreatorDataManager.getTimeStamp()
//                },
//                start_time_estimated = taskCreatorDataManager.getTimeEstimated(),
//                temperature_value = taskCreatorDataManager.getTemperatureValue(),
//                temperature_condition = taskCreatorDataManager.getTemperatureCondition(),
//                wind_value = taskCreatorDataManager.getWindValue(),
//                wind_condition = taskCreatorDataManager.getWindCondition(),
//                humidity_value = taskCreatorDataManager.getHumidityValue(),
//                humidity_condition = taskCreatorDataManager.getHumidityCondition(),
//                pressure_value = taskCreatorDataManager.getPressureValue(),
//                pressure_condition = taskCreatorDataManager.getPressureCondition()
//            )
//        )
    }

    fun postToolManagementNavigationEvent() {
        navigationEvent.postValue(
            ToolNavigationEvent()
        )
    }
}
