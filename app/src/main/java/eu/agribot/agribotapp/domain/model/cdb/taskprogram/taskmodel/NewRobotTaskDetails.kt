package eu.agribot.agribotapp.domain.model.cdb.taskprogram.taskmodel

import com.google.firebase.Timestamp
import eu.agribot.agribotapp.communication.database.clouddatabase.model.programtasks.NewRobotTaskDetailsCDB

class NewRobotTaskDetails(
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
) : BaseProgramDataToCommunicationModel<NewRobotTaskDetailsCDB> {
    override fun toCommunicationModel() = NewRobotTaskDetailsCDB(
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
        pressure_condition = this.pressure_condition
    )
}
