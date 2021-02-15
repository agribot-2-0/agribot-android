package eu.agribot.agribotapp.communication.database.clouddatabase.model.state

import eu.agribot.agribotapp.communication.database.clouddatabase.model.BaseCloudDatabaseModel
import eu.agribot.agribotapp.domain.model.cdb.state.RobotTask

data class RobotTaskCDB(
    val progress: Int = 0
) : BaseCloudDatabaseModel<RobotTask> {

    override fun toDomainModel() = RobotTask(
        progress = this.progress
    )
}
