package eu.agribot.agribotapp.communication.database.clouddatabase.model

import com.google.firebase.firestore.PropertyName
import eu.agribot.agribotapp.domain.model.cdb.ActualTask

data class ActualTaskCDB(
    @get:PropertyName("actual_task")
    @set:PropertyName("actual_task")
    var actualTask: String = ""
) : BaseCloudDatabaseModel<ActualTask> {

    override fun toDomainModel() = ActualTask(
        actualTask = this.actualTask
    )
}
