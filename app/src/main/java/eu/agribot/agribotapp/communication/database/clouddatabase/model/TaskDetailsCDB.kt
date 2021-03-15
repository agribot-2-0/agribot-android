package eu.agribot.agribotapp.communication.database.clouddatabase.model

import com.google.firebase.Timestamp
import com.google.gson.annotations.SerializedName
import eu.agribot.agribotapp.domain.model.cdb.TaskDetails

data class TaskDetailsCDB(
    @SerializedName("path_name")
    val pathName: String = DEFAULT_VALUE,

    @SerializedName("start_time")
    val startTime: Timestamp? = Timestamp(0, 0),

    @SerializedName("start_time_estimated")
    val startTimeEstimated: Boolean = true,

    @SerializedName("temperature_value")
    val temperatureValue: String = DEFAULT_VALUE,

    @SerializedName("temperature_condition")
    val temperatureCondition: String = DEFAULT_VALUE,

    @SerializedName("wind_value")
    val windValue: String = DEFAULT_VALUE,

    @SerializedName("wind_condition")
    val windCondition: String = DEFAULT_VALUE,

    @SerializedName("humidity_value")
    val humidityValue: String = DEFAULT_VALUE,

    @SerializedName("humidity_condition")
    val humidityCondition: String = DEFAULT_VALUE,

    @SerializedName("pressure_value")
    val pressureValue: String = DEFAULT_VALUE,

    @SerializedName("pressure_condition")
    val pressureCondition: String = DEFAULT_VALUE,

    @SerializedName("front_tool_name")
    val frontToolName: String = DEFAULT_VALUE,

    @SerializedName("front_tool_mode")
    val frontToolMode: Int = 0,

    @SerializedName("back_tool_name")
    val backToolName: String = DEFAULT_VALUE,

    @SerializedName("back_tool_mode")
    val backToolMode: Int = 0

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
