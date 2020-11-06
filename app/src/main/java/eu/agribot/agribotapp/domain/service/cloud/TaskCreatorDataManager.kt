package eu.agribot.agribotapp.domain.service.cloud

import com.google.firebase.Timestamp
import eu.agribot.agribotapp.domain.model.cdb.TaskDetails

class TaskCreatorDataManager {
    var taskDetails: TaskDetails = TaskDetails()

    fun getTaskParemeters(): TaskDetails {
        return taskDetails
    }

    fun updateAllTaskParametrs(taskDetail: TaskDetails) {
        taskDetails =
            taskDetails.copy(
                path_name = taskDetail.path_name,
                start_time = taskDetail.start_time,
                start_time_estimated = taskDetail.start_time_estimated,
                temperature_value = taskDetail.temperature_value,
                temperature_condition = taskDetail.temperature_condition,
                pressure_value = taskDetail.pressure_value,
                pressure_condition = taskDetail.pressure_condition,
                humidity_value = taskDetail.humidity_value,
                humidity_condition = taskDetail.humidity_condition,
                wind_value = taskDetail.wind_value,
                wind_condition = taskDetail.wind_condition
            )
    }

    fun onUpdateTaskName(updatedTaskName: String) {
        taskDetails = taskDetails.copy(task_name = updatedTaskName)
    }

    fun onUpdatePathName(pathName: String) {
        taskDetails = taskDetails.copy(path_name = pathName)
    }

    fun onUpdateTimestamp(timestamp: Timestamp) {
        taskDetails = taskDetails.copy(start_time = timestamp)
    }

    fun onUpdateTemperatureValue(temperatureValue: String?) {
        if (temperatureValue != null) {
            taskDetails = taskDetails.copy(temperature_value = temperatureValue)
        }
    }

    fun onUpdateTemperatureCondition(temperatureCondition: String) {
        taskDetails = taskDetails.copy(temperature_condition = temperatureCondition)
    }

    fun onUpdatewindValue(windValue: String?) {
        if (windValue != null) {
            taskDetails = taskDetails.copy(wind_value = windValue)
        }
    }

    fun onUpdatewindCondition(windCondition: String) {
        taskDetails = taskDetails.copy(wind_condition = windCondition)
    }

    fun onUpdatehumidityValue(humidityValue: String?) {
        if (humidityValue != null)
            taskDetails = taskDetails.copy(humidity_value = humidityValue)
    }

    fun onUpdatehumidityCondition(humidityCondition: String) {
        taskDetails = taskDetails.copy(humidity_condition = humidityCondition)
    }

    fun onUpdatepressureValue(pressureValue: String?) {
        if (pressureValue != null) {
            taskDetails = taskDetails.copy(pressure_value = pressureValue)
        }
    }

    fun onUpdatepressureCondition(pressureCondition: String) {
        taskDetails = taskDetails.copy(pressure_condition = pressureCondition)
    }

    fun onUpdateTimestampEstimated(timestampEstimated: Boolean) {
        taskDetails = taskDetails.copy(start_time_estimated = timestampEstimated)
    }
}
