package eu.agribot.agribotapp.domain.service.cloud

import com.google.firebase.Timestamp
import eu.agribot.agribotapp.domain.model.cdb.TaskDetails

class TaskCreatorDataManager {
    companion object {
        const val defaultTimestap = 946728000L
        const val defaultToolName: String = "*"
        const val defaultToolMode: Int = 0
    }

    var taskDetails: TaskDetails = TaskDetails()

    fun getTaskParemeters(): TaskDetails {
        return taskDetails
    }

    fun updateAllTaskParametrs(taskDetail: TaskDetails) {
        taskDetails =
            taskDetails.copy(
                pathName = taskDetail.pathName,
                startTime = taskDetail.startTime,
                startTimeEstimated = taskDetail.startTimeEstimated,
                temperatureValue = taskDetail.temperatureValue,
                temperatureCondition = taskDetail.temperatureCondition,
                pressureValue = taskDetail.pressureValue,
                pressureCondition = taskDetail.pressureCondition,
                humidityValue = taskDetail.humidityValue,
                humidityCondition = taskDetail.humidityCondition,
                windValue = taskDetail.windValue,
                windCondition = taskDetail.windCondition,
                frontToolName = taskDetail.frontToolName,
                frontToolMode = taskDetail.frontToolMode,
                backToolName = taskDetail.backToolName,
                backToolMode = taskDetail.backToolMode
            )
    }

    fun onUpdateTaskName(updatedTaskName: String) {
        taskDetails = taskDetails.copy(task_name = updatedTaskName)
    }

    fun onUpdatePathName(pathName: String) {
        taskDetails = taskDetails.copy(pathName = pathName)
    }

    fun onUpdateTimestamp(timestamp: Timestamp) {
        taskDetails = taskDetails.copy(startTime = timestamp)
    }

    fun onUpdateTemperatureValue(temperatureValue: String?) {
        if (temperatureValue != null) {
            taskDetails = taskDetails.copy(temperatureValue = temperatureValue)
        }
    }

    fun onUpdateTemperatureCondition(temperatureCondition: String) {
        taskDetails = taskDetails.copy(temperatureCondition = temperatureCondition)
    }

    fun onUpdatewindValue(windValue: String?) {
        if (windValue != null) {
            taskDetails = taskDetails.copy(windValue = windValue)
        }
    }

    fun onUpdatewindCondition(windCondition: String) {
        taskDetails = taskDetails.copy(windCondition = windCondition)
    }

    fun onUpdatehumidityValue(humidityValue: String?) {
        if (humidityValue != null)
            taskDetails = taskDetails.copy(humidityValue = humidityValue)
    }

    fun onUpdatehumidityCondition(humidityCondition: String) {
        taskDetails = taskDetails.copy(humidityCondition = humidityCondition)
    }

    fun onUpdatepressureValue(pressureValue: String?) {
        if (pressureValue != null) {
            taskDetails = taskDetails.copy(pressureValue = pressureValue)
        }
    }

    fun onUpdatepressureCondition(pressureCondition: String) {
        taskDetails = taskDetails.copy(pressureCondition = pressureCondition)
    }

    fun onUpdateTimestampEstimated(timestampEstimated: Boolean) {
        taskDetails = taskDetails.copy(startTimeEstimated = timestampEstimated)
    }

    fun onUpdateFrontToolName(toolName: String) {
        taskDetails = taskDetails.copy(frontToolName = toolName)
    }

    fun onUpdateFrontToolMode(toolMode: Int) {
        taskDetails = taskDetails.copy(frontToolMode = toolMode)
    }

    fun onUpdateBackToolName(toolName: String) {
        taskDetails = taskDetails.copy(backToolName = toolName)
    }

    fun onUpdateBackToolMode(toolMode: Int) {
        taskDetails = taskDetails.copy(backToolMode = toolMode)
    }
}
