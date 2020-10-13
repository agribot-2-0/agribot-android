package eu.agribot.agribotapp.communication.database.clouddatabase.model

import com.google.firebase.Timestamp
import eu.agribot.agribotapp.domain.model.FirstRobotWeatherStationHumidity
import java.util.Date

data class FirstRobotWeatherStationHumidityCDB(
    val accuracy: Int = -1,
    val humidity: Int = -1,
    val timestamp: Timestamp = Timestamp(Date(1590508315L))
) : BaseCloudDatabaseModel<FirstRobotWeatherStationHumidity> {

    override fun toDomainModel() = FirstRobotWeatherStationHumidity(
        accuracy = this.accuracy,
        humidity = this.humidity,
        timestamp = this.timestamp
    )
}
