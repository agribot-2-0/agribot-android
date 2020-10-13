package eu.agribot.agribotapp.communication.database.clouddatabase.model

import eu.agribot.agribotapp.domain.model.ActualTask

data class ActualTaskCDB(
    val actual_task: String = "",
    val router_number: String = "",
    val task_ordered: ArrayList<String> = ArrayList()
) : BaseCloudDatabaseModel<ActualTask> {
    override fun toDomainModel() = ActualTask(
        actual_task = this.actual_task,
        router_number = this.router_number,
        task_ordered = this.task_ordered
    )
}
