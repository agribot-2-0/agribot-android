package eu.agribot.agribotapp.domain.model.cdb

import eu.agribot.agribotapp.communication.database.clouddatabase.model.ActualTaskCDB
import eu.agribot.agribotapp.domain.model.fromdomain.BaseDomainDataModel
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ActualTask(
    @SerialName("actual_task") val actual_task: String
) : BaseDomainDataModel<ActualTaskCDB> {
    override fun toCommunicationModel() = ActualTaskCDB(
        actual_task = this.actual_task
    )
}
