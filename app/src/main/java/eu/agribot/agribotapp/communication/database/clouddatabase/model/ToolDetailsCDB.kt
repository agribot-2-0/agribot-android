package eu.agribot.agribotapp.communication.database.clouddatabase.model

import com.google.firebase.firestore.PropertyName
import eu.agribot.agribotapp.domain.model.cdb.ToolDetails

data class ToolDetailsCDB(

    val rfid: List<Int> = listOf(),

    val pto: Double = 0.0,

    val blue: Double = 0.0,

    val yellow: Double = 0.0,

    val green: Double = 0.0,

    @get:PropertyName("tpl_up_down")
    @set:PropertyName("tpl_up_down")
    var tplUpDown: Double = 0.0,

    @get:PropertyName("tpl_push_pull")
    @set:PropertyName("tpl_push_pull")
    var tplPushPull: Double = 0.0,

    @get:PropertyName("v_max")
    @set:PropertyName("v_max")
    var vMax: Double = 0.0
) : BaseCloudDatabaseModel<ToolDetails> {
    override fun toDomainModel() = ToolDetails(
        rfid = this.rfid,
        pto = this.pto,
        blue = this.blue,
        yellow = this.yellow,
        green = this.green,
        tplUpDown = this.tplUpDown,
        tplPushPull = this.tplPushPull,
        vMax = this.vMax
    )
}
