package eu.agribot.agribotapp.communication.database.clouddatabase.model

import com.google.firebase.Timestamp
import eu.agribot.agribotapp.domain.model.cdb.RobotWeatherStationWind

data class RobotWeatherStationWindCDB(
    val speed_accuracy: Int = -1,
    val speed_value: Double = 1.0,
    val timestamp: Timestamp = Timestamp(0, 0)
) :
    BaseCloudDatabaseModel<RobotWeatherStationWind> {
    override fun toDomainModel() = RobotWeatherStationWind(
        speed_accuracy = this.speed_accuracy,
        speed_value = this.speed_value,
        timestamp = this.timestamp
    )
}
