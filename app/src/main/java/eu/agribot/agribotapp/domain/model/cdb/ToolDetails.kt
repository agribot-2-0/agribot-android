package eu.agribot.agribotapp.domain.model.cdb

data class ToolDetails(
    val rfid: Int,
    val pto: Double,
    val blue: Double,
    val yellow: Double,
    val green: Double,
    val tpl_up_down: Double,
    val tpl_push_pull: Double,
    val v_max: Double
)
