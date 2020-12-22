package eu.agribot.agribotapp.communication.database.clouddatabase.model

import eu.agribot.agribotapp.domain.model.cdb.ToolItem

data class TooltemCDB(
    val toolName: String
) : BaseCloudDatabaseModel<ToolItem> {
    override fun toDomainModel() = ToolItem(
        toolName = this.toolName
    )
}
