package eu.agribot.agribotapp.communication.database.clouddatabase.model

import com.google.firebase.Timestamp
import eu.agribot.agribotapp.domain.model.FirstRobotWeatherStationHumidity

data class FirstRobotWeatherStationHumidityCDB(
    val accuracy: Int = -1,
    val value: Int = -1,
    val timestamp: Timestamp = Timestamp(0, 0)
) : BaseCloudDatabaseModel<FirstRobotWeatherStationHumidity> {

    override fun toDomainModel() = FirstRobotWeatherStationHumidity(
        accuracy = this.accuracy,
        value = this.value,
        timestamp = this.timestamp
    )
}
