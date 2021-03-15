package eu.agribot.agribotapp.communication.database.clouddatabase.model

import com.google.gson.annotations.SerializedName
import eu.agribot.agribotapp.domain.model.cdb.ToolDetails

data class ToolDetailsCDB(

    val rfid: List<Int> = listOf(),

    val pto: Double = 0.0,

    val blue: Double = 0.0,

    val yellow: Double = 0.0,

    val green: Double = 0.0,

    @SerializedName("tpl_up_down")
    val tplUpDown: Double = 0.0,

    @SerializedName("tpl_push_pull")
    val tplPushPull: Double = 0.0,

    @SerializedName("v_max")
    val vMax: Double = 0.0
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
