package eu.agribot.agribotapp.domain.service.cloud

import androidx.lifecycle.MutableLiveData
import com.google.firebase.Timestamp
import eu.agribot.agribotapp.domain.model.TaskDetails

class TaskCreatorDataManager {
    private val taskName: MutableLiveData<String> = MutableLiveData()
    private val pathName: MutableLiveData<String> = MutableLiveData()
    private val timestampData: MutableLiveData<Timestamp> = MutableLiveData()

    private val temperatureValueData: MutableLiveData<String> = MutableLiveData()
    private val temperatureConditionData: MutableLiveData<String> = MutableLiveData()

    private val windValueData: MutableLiveData<String> = MutableLiveData()
    private val windConditionData: MutableLiveData<String> = MutableLiveData()

    private val humidityValueData: MutableLiveData<String> = MutableLiveData()
    private val humidityConditionData: MutableLiveData<String> = MutableLiveData()

    private val pressureValueData: MutableLiveData<String> = MutableLiveData()
    private val pressureConditionData: MutableLiveData<String> = MutableLiveData()
    private val startTimeEstimated: MutableLiveData<Boolean> = MutableLiveData()
    private val NULL_VALUE_TIMESTAMP = 946728000L
    private val NULL_VALUE_SYMBOL = "*"

    fun updateAllTaskParameters(taskDetails: TaskDetails) {
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
    }

    fun onUpdateTaskName(taskName: String) {
        this.taskName.value = taskName
    }

    fun onUpdatePathName(pathName: String) {
        this.pathName.value = pathName
    }

    fun onUpdateTimestamp(timestamp: Timestamp) {
        timestampData.value = timestamp
    }

    fun onUpdateTemperatureValue(temperatureValue: String?) {
        temperatureValueData.value = temperatureValue
    }

    fun onUpdateTemperatureCondition(temperatureCondition: String) {
        temperatureConditionData.value = temperatureCondition
    }

    fun onUpdatewindValue(windValue: String?) {
        windValueData.value = windValue
    }

    fun onUpdatewindCondition(windCondition: String) {
        windConditionData.value = windCondition
    }

    fun onUpdatehumidityValue(humidityValue: String?) {
        humidityValueData.value = humidityValue
    }

    fun onUpdatehumidityCondition(humidityCondition: String) {
        humidityConditionData.value = humidityCondition
    }

    fun onUpdatepressureValue(pressureValue: String?) {
        pressureValueData.value = pressureValue
    }

    fun onUpdatepressureCondition(pressureCondition: String) {
        pressureConditionData.value = pressureCondition
    }

    fun onUpdateTimestampEstimated(timestampEstimated: Boolean) {
        startTimeEstimated.value = timestampEstimated
    }

    fun getTaskName(): String {
        return if (taskName.value != null) {
            taskName.value!!
        } else {
            "null"
        }
    }

    fun getPathName(): String? {
        return if (pathName.value != null) {
            pathName.value!!
        } else {
            "empty_path"
        }
    }

    fun getTimeStamp(): Timestamp? {
        return if (timestampData.value != null)
            timestampData.value!!
        else Timestamp(NULL_VALUE_TIMESTAMP, 0)
    }

    fun getTemperatureValue(): String {
        return if (temperatureValueData.value != null)
            temperatureValueData.value!!
        else NULL_VALUE_SYMBOL
    }

    fun getTemperatureCondition(): String {
        return if (temperatureValueData.value != null && temperatureConditionData.value != null) {
            temperatureConditionData.value!!
        } else NULL_VALUE_SYMBOL
    }

    fun getWindValue(): String {
        return if (windValueData.value != null) {
            windValueData.value!!
        } else NULL_VALUE_SYMBOL
    }

    fun getWindCondition(): String {
        return if (windValueData.value != null && windConditionData.value != null) {
            windConditionData.value!!
        } else NULL_VALUE_SYMBOL
    }

    fun getHumidityValue(): String {
        return if (humidityValueData.value != null) {
            humidityValueData.value!!
        } else NULL_VALUE_SYMBOL
    }

    fun getHumidityCondition(): String {
        return if (humidityValueData.value != null && humidityConditionData.value != null) {
            humidityConditionData.value!!
        } else NULL_VALUE_SYMBOL
    }

    fun getPressureValue(): String {
        return if (pressureValueData.value != null) {
            pressureValueData.value!!
        } else NULL_VALUE_SYMBOL
    }

    fun getPressureCondition(): String {
        return if (pressureValueData.value != null && pressureConditionData.value != null) {
            pressureConditionData.value!!
        } else NULL_VALUE_SYMBOL
    }

    fun getTimeEstimated(): Boolean {
        return if (startTimeEstimated.value != null) {
            startTimeEstimated.value!!
        } else return true
    }
}
