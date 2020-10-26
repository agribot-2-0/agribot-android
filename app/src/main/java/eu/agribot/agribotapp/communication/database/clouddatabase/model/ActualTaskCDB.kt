package eu.agribot.agribotapp.communication.database.clouddatabase.model

import eu.agribot.agribotapp.domain.model.cdb.ActualTask

data class ActualTaskCDB(
    val actual_task: String = ""
) : BaseCloudDatabaseModel<ActualTask> {
    override fun toDomainModel() = ActualTask(
        actual_task = this.actual_task
    )
}
