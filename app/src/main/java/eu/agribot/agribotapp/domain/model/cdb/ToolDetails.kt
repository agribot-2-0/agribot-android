package eu.agribot.agribotapp.domain.model.cdb

import eu.agribot.agribotapp.communication.database.clouddatabase.model.ToolDetailsCDB

data class ToolDetails(
    val rfid: Int,
    val pto: Double = 0.0,
    val blue: Double = 0.0,
    val yellow: Double = 0.0,
    val green: Double = 0.0,
    val tpl_up_down: Double = 0.0,
    val tpl_push_pull: Double = 0.0,
    val v_max: Double = 0.0
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
