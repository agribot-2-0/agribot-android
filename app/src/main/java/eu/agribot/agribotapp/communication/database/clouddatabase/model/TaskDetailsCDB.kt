package eu.agribot.agribotapp.communication.database.clouddatabase.model

import com.google.firebase.Timestamp
import eu.agribot.agribotapp.domain.model.cdb.TaskDetails

data class TaskDetailsCDB(
    val path_name: String = DEFAULT_VALUE,
    val start_time: Timestamp? = Timestamp(0, 0),
    val start_time_estimated: Boolean = true,

    val temperature_value: String = DEFAULT_VALUE,
    val temperature_condition: String = DEFAULT_VALUE,

    val wind_value: String = DEFAULT_VALUE,
    val wind_condition: String = DEFAULT_VALUE,

    val humidity_value: String = DEFAULT_VALUE,
    val humidity_condition: String = DEFAULT_VALUE,

    val pressure_value: String = DEFAULT_VALUE,
    val pressure_condition: String = DEFAULT_VALUE,

    val front_tool_name: String = DEFAULT_VALUE,
    val front_tool_mode: Int = 0,
    val back_tool_name: String = DEFAULT_VALUE,
    val back_tool_mode: Int = 0
) : BaseCloudDatabaseModel<TaskDetails> {
    override fun toDomainModel() = TaskDetails(
        path_name = this.path_name,
        start_time = this.start_time,
        start_time_estimated = this.start_time_estimated,
        temperature_value = this.temperature_value,
        temperature_condition = this.temperature_condition,
        wind_value = this.wind_value,
        wind_condition = this.wind_condition,
        humidity_value = this.humidity_value,
        humidity_condition = this.humidity_condition,
        pressure_value = this.pressure_value,
        pressure_condition = this.pressure_condition,
        front_tool_name = this.front_tool_name,
        front_tool_mode = this.front_tool_mode,
        back_tool_name = this.back_tool_name,
        back_tool_mode = this.back_tool_mode
    )

    companion object {
        private const val DEFAULT_VALUE = "*"
    }
}
