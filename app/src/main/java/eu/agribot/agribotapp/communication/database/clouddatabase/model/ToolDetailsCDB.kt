package eu.agribot.agribotapp.communication.database.clouddatabase.model

import eu.agribot.agribotapp.domain.model.ToolDetails

class ToolDetailsCDB(
    val rfid: String = ""
) : BaseCloudDatabaseModel<ToolDetails> {
    override fun toDomainModel() = ToolDetails(
        rfid = this.rfid,
    )
}
