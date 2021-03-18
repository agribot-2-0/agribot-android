package eu.agribot.agribotapp.communication.database.clouddatabase.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.PropertyName
import eu.agribot.agribotapp.domain.model.cdb.RobotWeatherStationWind

data class RobotWeatherStationWindCDB(

    @get:PropertyName("speed_accuracy")
    @set:PropertyName("speed_accuracy")
    var speedAccuracy: Double = 0.0,

    @get:PropertyName("speed_value")
    @set:PropertyName("speed_value")
    var speedValue: Double = 1.0,

    val timestamp: Timestamp = Timestamp(0, 0)
) :
    BaseCloudDatabaseModel<RobotWeatherStationWind> {
    override fun toDomainModel() = RobotWeatherStationWind(
        speedAccuracy = this.speedAccuracy,
        speedValue = this.speedValue,
        timestamp = this.timestamp
    )
}
