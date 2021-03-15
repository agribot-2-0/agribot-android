package eu.agribot.agribotapp.communication.database.clouddatabase.model

import com.google.gson.annotations.SerializedName
import eu.agribot.agribotapp.domain.model.cdb.ActualTask

data class ActualTaskCDB(

    @SerializedName("actual_task")
    val actualTask: String = ""
) : BaseCloudDatabaseModel<ActualTask> {

    override fun toDomainModel() = ActualTask(
        actualTask = this.actualTask
    )
}
