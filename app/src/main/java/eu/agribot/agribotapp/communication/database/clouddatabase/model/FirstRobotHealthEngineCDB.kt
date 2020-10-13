package eu.agribot.agribotapp.communication.database.clouddatabase.model

import com.google.firebase.Timestamp
import eu.agribot.agribotapp.domain.model.FirstRobotHealthEngine
import java.util.Date

data class FirstRobotHealthEngineCDB(
    val error_code: Int = -1,
    val error_name: String = "",
    val timestamp: Timestamp = Timestamp(Date(1590508315L))
) : BaseCloudDatabaseModel<FirstRobotHealthEngine> {

    override fun toDomainModel() = FirstRobotHealthEngine(
        errorCode = this.error_code,
        errorName = this.error_name,
        timestamp = this.timestamp
    )
}
