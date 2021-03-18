package eu.agribot.agribotapp.communication.database.clouddatabase.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.PropertyName
import eu.agribot.agribotapp.domain.model.cdb.TaskDetails

data class TaskDetailsCDB(

    @get:PropertyName("path_name")
    @set:PropertyName("path_name")
    var pathName: String = DEFAULT_VALUE,

    @get:PropertyName("start_time")
    @set:PropertyName("start_time")
    var startTime: Timestamp? = Timestamp(0, 0),

    @get:PropertyName("start_time_estimated")
    @set:PropertyName("start_time_estimated")
    var startTimeEstimated: Boolean = true,

    @get:PropertyName("temperature_value")
    @set:PropertyName("temperature_value")
    var temperatureValue: String = DEFAULT_VALUE,

    @get:PropertyName("temperature_condition")
    @set:PropertyName("temperature_condition")
    var temperatureCondition: String = DEFAULT_VALUE,

    @get:PropertyName("wind_value")
    @set:PropertyName("wind_value")
    var windValue: String = DEFAULT_VALUE,

    @get:PropertyName("wind_condition")
    @set:PropertyName("wind_condition")
    var windCondition: String = DEFAULT_VALUE,

    @get:PropertyName("humidity_value")
    @set:PropertyName("humidity_value")
    var humidityValue: String = DEFAULT_VALUE,

    @get:PropertyName("humidity_condition")
    @set:PropertyName("humidity_condition")
    var humidityCondition: String = DEFAULT_VALUE,

    @get:PropertyName("pressure_value")
    @set:PropertyName("pressure_value")
    var pressureValue: String = DEFAULT_VALUE,

    @get:PropertyName("pressure_condition")
    @set:PropertyName("pressure_condition")
    var pressureCondition: String = DEFAULT_VALUE,

    @get:PropertyName("front_tool_name")
    @set:PropertyName("front_tool_name")
    var frontToolName: String = DEFAULT_VALUE,

    @get:PropertyName("front_tool_mode")
    @set:PropertyName("front_tool_mode")
    var frontToolMode: Int = 0,

    @get:PropertyName("back_tool_name")
    @set:PropertyName("back_tool_name")
    var backToolName: String = DEFAULT_VALUE,

    @get:PropertyName("back_tool_mode")
    @set:PropertyName("back_tool_mode")
    var backToolMode: Int = 0

) : BaseCloudDatabaseModel<TaskDetails> {
    override fun toDomainModel() = TaskDetails(
        pathName = this.pathName,
        startTime = this.startTime,
        startTimeEstimated = this.startTimeEstimated,
        temperatureValue = this.temperatureValue,
        temperatureCondition = this.temperatureCondition,
        windValue = this.windValue,
        windCondition = this.windCondition,
        humidityValue = this.humidityValue,
        humidityCondition = this.humidityCondition,
        pressureValue = this.pressureValue,
        pressureCondition = this.pressureCondition,
        frontToolName = this.frontToolName,
        frontToolMode = this.frontToolMode,
        backToolName = this.backToolName,
        backToolMode = this.backToolMode
    )

    companion object {
        private const val DEFAULT_VALUE = "*"
    }
}
