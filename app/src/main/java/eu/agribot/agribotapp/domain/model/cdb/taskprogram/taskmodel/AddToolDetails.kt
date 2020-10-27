package eu.agribot.agribotapp.domain.model.cdb.taskprogram.taskmodel

import eu.agribot.agribotapp.communication.database.clouddatabase.model.ToolDetailsCDB

class AddToolDetails(
    val rfid: String
) : BaseProgramDataToCommunicationModel<ToolDetailsCDB> {
    override fun toCommunicationModel() = ToolDetailsCDB(
        rfid = this.rfid
    )
}
