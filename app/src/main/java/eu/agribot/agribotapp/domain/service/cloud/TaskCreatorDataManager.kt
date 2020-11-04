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

    fun taskParameters(taskDetails: TaskDetails): List<TaskDetails> = listOf(taskDetails)

    fun getTaskParemeters(onGetTaskParametr: (taskDetails: TaskDetails) -> Unit) {
        taskParameters(taskDetails).forEach { taskDetail -> onGetTaskParametr(taskDetail) }
    }

    fun updateTaskParameters(taskDetails: TaskDetails) {
        this.taskDetails = taskDetails
        /*
        taskParameters(taskDetails)
        onUpdatePathName(taskDetails.path_name)
        onUpdateTimestamp(taskDetails.start_time!!)
        onUpdateTemperatureValue(taskDetails.temperature_value)
        onUpdateTemperatureCondition(taskDetails.temperature_condition)
        onUpdatewindValue(taskDetails.wind_value)
        onUpdatewindCondition(taskDetails.wind_condition)
        onUpdatehumidityValue(taskDetails.humidity_value)
        onUpdatehumidityCondition(taskDetails.humidity_condition)
        onUpdatepressureValue(taskDetails.pressure_value)
        onUpdatepressureCondition(taskDetails.pressure_condition)
        onUpdateTimestampEstimated(taskDetails.start_time_estimated)
         */
        // getTaskParemeters()
    }
/*
    fun onUpdateTaskName(taskName: String) {
        this.taskName = taskName
    }

    fun onUpdatePathName(pathName: String) {
        this.pathName = pathName
    }

    fun onUpdateTimestamp(timestamp: Timestamp) {
        timestampData = timestamp
    }

    fun onUpdateTemperatureValue(temperatureValue: String?) {
        if (temperatureValue != null) {
            temperatureValueData = temperatureValue
        }
    }

    fun onUpdateTemperatureCondition(temperatureCondition: String) {
        temperatureConditionData = temperatureCondition
    }

    fun onUpdatewindValue(windValue: String?) {
        if (windValue != null) {
            windValueData = windValue
        }
    }

    fun onUpdatewindCondition(windCondition: String) {
        windConditionData = windCondition
    }

    fun onUpdatehumidityValue(humidityValue: String?) {
        if (humidityValue != null) {
            humidityValueData = humidityValue
        }
    }

    fun onUpdatehumidityCondition(humidityCondition: String) {
        humidityConditionData = humidityCondition
    }

    fun onUpdatepressureValue(pressureValue: String?) {
        if (pressureValue != null) {
            pressureValueData = pressureValue
        }
    }

    fun onUpdatepressureCondition(pressureCondition: String) {
        pressureConditionData = pressureCondition
    }

    fun onUpdateTimestampEstimated(timestampEstimated: Boolean) {
        startTimeEstimated = timestampEstimated
    }

    fun getTaskName(): String {
        return taskName
    }

    fun getPathName(): String? {
        return pathName
    }

    fun getTimeStamp(): Timestamp? {
        return timestampData
    }

    fun getTemperatureValue(): String {
        return temperatureValueData
    }

    fun getTemperatureCondition(): String {
        return temperatureConditionData
    }

    fun getWindValue(): String {
        return windValueData
    }

    fun getWindCondition(): String {
        return windConditionData
    }

    fun getHumidityValue(): String {
        return humidityValueData
    }

    fun getHumidityCondition(): String {
        return humidityConditionData
    }

    fun getPressureValue(): String {
        return pressureValueData
    }

    fun getPressureCondition(): String {
        return pressureConditionData
    }

    fun getTimeEstimated(): Boolean {
        return startTimeEstimated
    }

 */
}
