package eu.agribot.agribotapp.communication.database.clouddatabase.model

import com.google.firebase.Timestamp
import com.google.gson.annotations.SerializedName
import eu.agribot.agribotapp.domain.model.cdb.RobotWeatherStationWind

data class RobotWeatherStationWindCDB(

    @SerializedName("speed_accuracy")
    val speedAccuracy: Double = 0.0,

    @SerializedName("speed_value")
    val speedValue: Double = 1.0,

    val timestamp: Timestamp = Timestamp(0, 0)
) :
    BaseCloudDatabaseModel<RobotWeatherStationWind> {
    override fun toDomainModel() = RobotWeatherStationWind(
        speedAccuracy = this.speedAccuracy,
        speedValue = this.speedValue,
        timestamp = this.timestamp
    )
}
