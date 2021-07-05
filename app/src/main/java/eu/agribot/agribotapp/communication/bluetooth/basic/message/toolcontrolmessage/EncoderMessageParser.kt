package eu.agribot.agribotapp.communication.bluetooth.basic.message.toolcontrolmessage

import eu.agribot.agribotapp.communication.bluetooth.model.fromcommunication.ToolFeedbackDataBD
import eu.agribot.agribotapp.domain.model.ToolControlModel

interface EncoderMessageParser {

    fun decodeToolMessage(encoderResponse: ByteArray): ToolFeedbackDataBD

    fun encodeToolMessage(toolControlModel: ToolControlModel): ByteArray
}
