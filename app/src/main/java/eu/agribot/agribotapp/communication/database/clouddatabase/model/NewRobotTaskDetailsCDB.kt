package eu.agribot.agribotapp.communication.database.clouddatabase.model

import com.google.firebase.Timestamp
import com.google.gson.annotations.SerializedName

data class NewRobotTaskDetailsCDB(

    @SerializedName("path_name")
    val pathName: String,

    @SerializedName("start_time")
    val startTime: Timestamp?,

    @SerializedName("start_time_estimated")
    val startTimeEstimated: Boolean,

    @SerializedName("temperature_value")
    val temperatureValue: String,

    @SerializedName("temperature_condition")
    val temperatureCondition: String,

    @SerializedName("wind_value")
    val windValue: String,

    @SerializedName("wind_condition")
    val windCondition: String,

    @SerializedName("humidity_value")
    val humidityValue: String,

    @SerializedName("humidity_condition")
    val humidityCondition: String,

    @SerializedName("pressure_value")
    val pressureValue: String,

    @SerializedName("pressure_condition")
    val pressureCondition: String,

    @SerializedName("front_tool_name")
    val frontToolName: String,

    @SerializedName("front_tool_mode")
    val frontToolMode: Int,

    @SerializedName("back_tool_name")
    val backToolName: String,

    @SerializedName("back_tool_mode")
    val backToolMode: Int
)
