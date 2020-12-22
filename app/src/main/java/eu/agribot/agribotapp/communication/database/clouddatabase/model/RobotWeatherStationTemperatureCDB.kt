package eu.agribot.agribotapp.communication.database.clouddatabase.model

import com.google.firebase.Timestamp
import eu.agribot.agribotapp.domain.model.cdb.RobotWeatherStationTemperature

data class RobotWeatherStationTemperatureCDB(
    val accuracy: Int = 1,
    val value: Double = 1.0,
    val timestamp: Timestamp = Timestamp(0, 0)
) : BaseCloudDatabaseModel<RobotWeatherStationTemperature> {

    override fun toDomainModel() = RobotWeatherStationTemperature(
        accuracy = this.accuracy,
        value = this.value,
        timestamp = this.timestamp
    )
}
