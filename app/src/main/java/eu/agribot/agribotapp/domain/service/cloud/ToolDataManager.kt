package eu.agribot.agribotapp.domain.service.cloud

import eu.agribot.agribotapp.domain.model.cdb.ToolDetails
import eu.agribot.agribotapp.domain.service.cloud.downloader.FilePath.TOOL_PHOTO_FILE_TYPE

class ToolDataManager {
    private val initString = ""
    private val initDouble = 0.0
    val noRfidTag = listOf<Int>()
    var toolName: String = initString
    var rfid: List<Int> = noRfidTag
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

    fun getToolPhotoPathName(): String {
        return "/$toolName$TOOL_PHOTO_FILE_TYPE"
    }
}
