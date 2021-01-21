package eu.agribot.agribotapp.communication.database.clouddatabase.model

import eu.agribot.agribotapp.domain.model.cdb.ToolDetails

data class ToolDetailsCDB(
    val rfid: List<Int> = listOf(),
    val pto: Double = 0.0,
    val blue: Double = 0.0,
    val yellow: Double = 0.0,
    val green: Double = 0.0,
    val tpl_up_down: Double = 0.0,
    val tpl_push_pull: Double = 0.0,
    val v_max: Double = 0.0
) : BaseCloudDatabaseModel<ToolDetails> {
    override fun toDomainModel() = ToolDetails(
        rfid = this.rfid,
        pto = this.pto,
        blue = this.blue,
        yellow = this.yellow,
        green = this.green,
        tplUpDown = this.tpl_up_down,
        tplPushPull = this.tpl_push_pull,
        vMax = this.v_max
    )
}
