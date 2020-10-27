package eu.agribot.agribotapp.communication.database.clouddatabase.model

import eu.agribot.agribotapp.domain.model.ToolItem

class TooltemCDB(val toolName: String = "", val rfid: String = "") :
    BaseCloudDatabaseModel<ToolItem> {
    override fun toDomainModel() = ToolItem(
        toolName = this.toolName,
        rfid = this.rfid
    )
}
