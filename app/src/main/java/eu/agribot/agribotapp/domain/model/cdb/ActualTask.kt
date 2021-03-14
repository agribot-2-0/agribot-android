package eu.agribot.agribotapp.domain.model.cdb

import eu.agribot.agribotapp.communication.database.clouddatabase.model.ActualTaskCDB
import eu.agribot.agribotapp.domain.model.fromdomain.BaseDomainDataModel

data class ActualTask(
    val actualTask: String = ""
) : BaseDomainDataModel<ActualTaskCDB> {
    override fun toCommunicationModel() = ActualTaskCDB(
        actualTask = this.actualTask
    )
}
