package eu.agribot.agribotapp.communication.database.clouddatabase.model

import com.google.firebase.Timestamp
import eu.agribot.agribotapp.domain.model.cdb.FirstRobotWeatherStationWind

data class FirstRobotWeatherStationWindCDB(
    val speed_accuracy: Int = -1,
    val speed_value: Int = -1,
    val timestamp: Timestamp = Timestamp(0, 0)
) :
    BaseCloudDatabaseModel<FirstRobotWeatherStationWind> {
    override fun toDomainModel() = FirstRobotWeatherStationWind(
        speed_accuracy = this.speed_accuracy,
        speed_value = this.speed_value,
        timestamp = this.timestamp
    )
}
