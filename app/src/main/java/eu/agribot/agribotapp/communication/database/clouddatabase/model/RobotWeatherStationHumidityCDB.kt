package eu.agribot.agribotapp.communication.database.clouddatabase.model

import com.google.firebase.Timestamp
import eu.agribot.agribotapp.domain.model.cdb.RobotWeatherStationHumidity

data class RobotWeatherStationHumidityCDB(
    val accuracy: Int = -1,
    val value: Int = -1,
    val timestamp: Timestamp = Timestamp(0, 0)
) : BaseCloudDatabaseModel<RobotWeatherStationHumidity> {

    override fun toDomainModel() = RobotWeatherStationHumidity(
        accuracy = this.accuracy,
        value = this.value,
        timestamp = this.timestamp
    )
}
