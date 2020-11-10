package eu.agribot.agribotapp.communication.database.clouddatabase.model

import com.google.firebase.Timestamp
import eu.agribot.agribotapp.domain.model.cdb.RobotWeatherStationPressure

data class RobotWeatherStationPressureCDB(
    val accuracy: Int = -1,
    val timestamp: Timestamp = Timestamp(0, 0),
    val value: Double = 1.0
) :
    BaseCloudDatabaseModel<RobotWeatherStationPressure> {
    override fun toDomainModel() = RobotWeatherStationPressure(
        accuracy = this.accuracy,
        timestamp = this.timestamp,
        value = this.value
    )
}
