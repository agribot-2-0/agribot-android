package eu.agribot.agribotapp.domain.service.cloud

import eu.agribot.agribotapp.domain.model.cdb.ToolDetails

class ToolDataManager {
    private val initString = ""
    private val initDouble = 0.0
    val noRfidTag = listOf<String>()
    var toolName: String = initString
    var rfid: List<String> = noRfidTag
    var pto: Double = initDouble
    var tplUpDown: Double = initDouble
    var tplPushPull: Double = initDouble
    var green: Double = initDouble
    var yellow: Double = initDouble
    var blue: Double = initDouble
    var vMax: Double = initDouble

    fun updateAllToolParameters(toolDetails: ToolDetails) {
        rfid = toolDetails.rfid
        pto = toolDetails.pto
        tplUpDown = toolDetails.tplUpDown
        tplPushPull = toolDetails.tplPushPull
        green = toolDetails.green
        blue = toolDetails.blue
        yellow = toolDetails.yellow
        vMax = toolDetails.vMax
    }
}
