package eu.agribot.agribotapp.communication.database.clouddatabase.model

import com.google.firebase.Timestamp
import eu.agribot.agribotapp.domain.model.FirstRobotWeatherStationTemperature
import java.util.Date

data class FirstRobotWeatherStationTemperatureCDB(
    val accuracy: Int = 1,
    val temperature: Int = 1,
    val timestamp: Timestamp = Timestamp(Date(1590508315L))
) : BaseCloudDatabaseModel<FirstRobotWeatherStationTemperature> {

    override fun toDomainModel() = FirstRobotWeatherStationTemperature(
        accuracy = this.accuracy,
        temperature = this.temperature,
        timestamp = this.timestamp
    )
}
