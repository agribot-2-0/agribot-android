package eu.agribot.agribotapp.communication.database.clouddatabase.model

import com.google.firebase.Timestamp

data class NewRobotTaskDetailsCDB(
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
)
