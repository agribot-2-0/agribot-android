package eu.agribot.agribotapp.communication.database.clouddatabase.model

import com.google.firebase.Timestamp
import eu.agribot.agribotapp.domain.model.cdb.RobotLocalizationPose
import java.util.Date

data class RobotLocalizationPoseCDB(
    val altitude: Double = 193.73,
    val latitude: Double = 51.26,
    val longitude: Double = 11.11,
    val heading: Float = 1.8809F,
    val timestamp: Timestamp = Timestamp(Date(1590508315L))
) : BaseCloudDatabaseModel<RobotLocalizationPose> {

    override fun toDomainModel() = RobotLocalizationPose(
        altitude = this.altitude,
        latitude = this.latitude,
        longitude = this.longitude,
        heading = this.heading,
        timestamp = this.timestamp
    )
}
