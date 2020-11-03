package eu.agribot.agribotapp.domain.model.cdb

import eu.agribot.agribotapp.communication.database.clouddatabase.model.ToolDetailsCDB

data class ToolDetails(
    val rfid: Int,
    val pto: Double,
    val blue: Double,
    val yellow: Double,
    val green: Double,
    val tpl_up_down: Double,
    val tpl_push_pull: Double,
    val v_max: Double
) : BaseCloudDatabaseModel<ToolDetailsCDB> {
    override fun toCommunicationModel() = ToolDetailsCDB(
        rfid = this.rfid,
        pto = this.pto,
        blue = this.blue,
        yellow = this.yellow,
        green = this.green,
        tpl_up_down = this.tpl_up_down,
        tpl_push_pull = this.tpl_push_pull,
        v_max = this.v_max
    )
}
