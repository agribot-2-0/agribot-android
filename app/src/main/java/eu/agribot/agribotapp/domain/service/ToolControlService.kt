package eu.agribot.agribotapp.domain.service

import eu.agribot.agribotapp.communication.bluetooth.basic.message.receivedmessage.encoder.Tools

class ToolControlService {

    fun getTplProgressValue(toolId: Int, toolProgress: Int): String {
        return if (toolId == Tools.TplHeight.Front.toolId || toolId == Tools.TplHeight.Rear.toolId) {
            String.format("%.2f", toolProgress.toDouble() * 0.01) + "m"
        } else {
            String.format("%.2f", toolProgress.toDouble()* 0.01) + "rad"
        }
    }


}