package eu.agribot.agribotapp.domain.service.cloud

import com.google.firebase.Timestamp
import eu.agribot.agribotapp.domain.model.cdb.TaskDetails

class TaskCreatorDataManager {
    private var taskName: String = ""
    private var pathName: String = "empty_path"
    private var timestampData: Timestamp = Timestamp(946728000L, 0)

    private var temperatureValueData: String = ""
    private var temperatureConditionData: String = ""

    private var windValueData: String = ""
    private var windConditionData: String = ""

    private var humidityValueData: String = ""
    private var humidityConditionData: String = ""

    private var pressureValueData: String = ""
    private var pressureConditionData: String = ""
    private var startTimeEstimated: Boolean = false
    private val defaultTimestamp = 946728000L
    private val nullValueSymbol = "*"
    private val emptyPath = "empty_path"
    private val emptyTaskNameText = "null"
    var taskDetails: TaskDetails = TaskDetails()


    fun getTaskParemeters(): TaskDetails {
        return taskDetails
    }

    fun updateAllTaskParametrs(taskDetails: TaskDetails) {
        this.taskDetails = taskDetails
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
