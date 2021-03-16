package eu.agribot.agribotapp.communication.database.clouddatabase.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.PropertyName

data class NewRobotTaskDetailsCDB(

    @get:PropertyName("path_name")
    @set:PropertyName("path_name")
    var pathName: String,

    @get:PropertyName("start_time")
    @set:PropertyName("start_time")
    var startTime: Timestamp?,

    @get:PropertyName("start_time_estimated")
    @set:PropertyName("start_time_estimated")
    var startTimeEstimated: Boolean,

    @get:PropertyName("temperature_value")
    @set:PropertyName("temperature_value")
    var temperatureValue: String,

    @get:PropertyName("temperature_condition")
    @set:PropertyName("temperature_condition")
    var temperatureCondition: String,

    @get:PropertyName("wind_value")
    @set:PropertyName("wind_value")
    var windValue: String,

    @get:PropertyName("wind_condition")
    @set:PropertyName("wind_condition")
    var windCondition: String,

    @get:PropertyName("humidity_value")
    @set:PropertyName("humidity_value")
    var humidityValue: String,

    @get:PropertyName("humidity_condition")
    @set:PropertyName("humidity_condition")
    var humidityCondition: String,

    @get:PropertyName("pressure_value")
    @set:PropertyName("pressure_value")
    var pressureValue: String,

    @get:PropertyName("pressure_condition")
    @set:PropertyName("pressure_condition")
    var pressureCondition: String,

    @get:PropertyName("front_tool_name")
    @set:PropertyName("front_tool_name")
    var frontToolName: String,

    @get:PropertyName("front_tool_mode")
    @set:PropertyName("front_tool_mode")
    var frontToolMode: Int,

    @get:PropertyName("back_tool_name")
    @set:PropertyName("back_tool_name")
    var backToolName: String,

    @get:PropertyName("back_tool_mode")
    @set:PropertyName("back_tool_mode")
    var backToolMode: Int
)
