package eu.agribot.agribotapp.domain.model

import com.google.firebase.Timestamp

data class TaskDetails(
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
    val pressure_condition: String
)
