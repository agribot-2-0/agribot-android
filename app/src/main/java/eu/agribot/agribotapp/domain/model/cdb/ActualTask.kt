package eu.agribot.agribotapp.domain.model

import eu.agribot.agribotapp.communication.database.clouddatabase.model.ActualTaskCDB
import eu.agribot.agribotapp.domain.model.fromdomain.BaseDomainDataModel

class ActualTask(
    val actual_task: String
) : BaseDomainDataModel<ActualTaskCDB> {
    override fun toCommunicationModel() = ActualTaskCDB(
        actual_task = this.actual_task
    )
}
