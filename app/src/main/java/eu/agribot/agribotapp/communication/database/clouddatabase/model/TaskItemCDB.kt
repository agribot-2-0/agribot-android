package eu.agribot.agribotapp.communication.database.clouddatabase.model

import eu.agribot.agribotapp.domain.model.TaskItem

class TaskItemCDB(val taskName: String = "", val pathName: String = "") :
    BaseCloudDatabaseModel<TaskItem> {
    override fun toDomainModel() = TaskItem(
        taskName = this.taskName,
        pathName = this.pathName
    )
}
