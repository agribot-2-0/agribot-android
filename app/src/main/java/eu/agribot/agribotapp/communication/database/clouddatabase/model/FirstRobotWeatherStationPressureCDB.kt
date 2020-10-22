package eu.agribot.agribotapp.communication.database.clouddatabase.model

import com.google.firebase.Timestamp
import eu.agribot.agribotapp.domain.model.cdb.FirstRobotWeatherStationPressure

data class FirstRobotWeatherStationPressureCDB(
    val accuracy: Int = -1,
    val timestamp: Timestamp = Timestamp(0, 0),
    val value: Double = 1.0
) :
    BaseCloudDatabaseModel<FirstRobotWeatherStationPressure> {
    override fun toDomainModel() = FirstRobotWeatherStationPressure(
        accuracy = this.accuracy,
        timestamp = this.timestamp,
        value = this.value
    )
}
