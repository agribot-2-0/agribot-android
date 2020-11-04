package eu.agribot.agribotapp.domain.model.cdb

import eu.agribot.agribotapp.communication.database.clouddatabase.model.ToolDetailsCDB

data class ToolDetails(
    val rfid: Int,
    val pto: Double,
    val blue: Double,
    val yellow: Double,
    val green: Double,
    val tplUpDown: Double,
    val tplPushPull: Double,
    val vMax: Double
) : BaseCloudDatabaseModel<ToolDetailsCDB> {
    override fun toCommunicationModel() = ToolDetailsCDB(
        rfid = this.rfid,
        pto = this.pto,
        blue = this.blue,
        yellow = this.yellow,
        green = this.green,
        tpl_up_down = this.tplUpDown,
        tpl_push_pull = this.tplPushPull,
        v_max = this.vMax
    )
}
