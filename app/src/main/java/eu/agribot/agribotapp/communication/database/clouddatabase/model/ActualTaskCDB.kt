package eu.agribot.agribotapp.communication.database.clouddatabase.model

import eu.agribot.agribotapp.domain.model.cdb.ActualTask

data class ActualTaskCDB(
    val actualTask: String = ""
) : BaseCloudDatabaseModel<ActualTask> {
    override fun toDomainModel() = ActualTask(
        actualTask = this.actualTask
    )
}
