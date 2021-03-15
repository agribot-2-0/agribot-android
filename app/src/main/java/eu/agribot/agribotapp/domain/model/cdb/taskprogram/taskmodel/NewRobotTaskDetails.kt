package eu.agribot.agribotapp.domain.model.cdb.taskprogram.taskmodel

import com.google.firebase.Timestamp
import eu.agribot.agribotapp.communication.database.clouddatabase.model.NewRobotTaskDetailsCDB
import eu.agribot.agribotapp.domain.model.cdb.BaseCloudDatabaseModel

data class NewRobotTaskDetails(
    // TODO: Naming refactor from snake_case to camelCase
    val path_name: String,
    val start_time: Timestamp?,
    val start_time_estimated: Boolean,

    val temperature_value: String,
    val temperature_condition: String,

    val wind_value: String,
    val wind_condition: String,

    val humidity_value: String,
    val humidity_condition: String,

    val pressure_value: String,
    val pressure_condition: String,

    val front_tool_name: String,
    val front_tool_mode: Int,
    val back_tool_name: String,
    val back_tool_mode: Int

) : BaseCloudDatabaseModel<NewRobotTaskDetailsCDB> {
    override fun toCommunicationModel() = NewRobotTaskDetailsCDB(
        pathName = this.path_name,
        startTime = this.start_time,
        startTimeEstimated = this.start_time_estimated,
        temperatureValue = this.temperature_value,
        temperatureCondition = this.temperature_condition,
        windValue = this.wind_value,
        windCondition = this.wind_condition,
        humidityValue = this.humidity_value,
        humidityCondition = this.humidity_condition,
        pressureValue = this.pressure_value,
        pressureCondition = this.pressure_condition,
        frontToolName = this.front_tool_name,
        frontToolMode = this.front_tool_mode,
        backToolName = this.back_tool_name,
        backToolMode = this.back_tool_mode
    )
}
