package eu.agribot.agribotapp.communication.database.clouddatabase.model

import com.google.firebase.Timestamp
import eu.agribot.agribotapp.domain.model.cdb.RobotHealthEngine
import java.util.Date

data class RobotHealthEngineCDB(
    val error_code: Int = -1,
    val error_name: String = "",
    val timestamp: Timestamp = Timestamp(Date(1590508315L))
) : BaseCloudDatabaseModel<RobotHealthEngine> {

    override fun toDomainModel() = RobotHealthEngine(
        errorCode = this.error_code,
        errorName = this.error_name,
        timestamp = this.timestamp
    )
}
