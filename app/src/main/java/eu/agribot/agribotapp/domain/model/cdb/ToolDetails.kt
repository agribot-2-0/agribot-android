package eu.agribot.agribotapp.domain.model.cdb

import eu.agribot.agribotapp.communication.database.clouddatabase.model.ToolDetailsCDB

data class ToolDetails(
    val rfid: List<Int> = listOf(),
    val pto: Double = 0.0,
    val blue: Double = 0.0,
    val yellow: Double = 0.0,
    val green: Double = 0.0,
    val tplUpDown: Double = 0.0,
    val tplPushPull: Double = 0.0,
    val vMax: Double = 0.0
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
