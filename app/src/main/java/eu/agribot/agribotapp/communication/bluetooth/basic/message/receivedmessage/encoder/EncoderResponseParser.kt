package eu.agribot.agribotapp.communication.bluetooth.basic.message.receivedmessage.encoder

import eu.agribot.agribotapp.communication.bluetooth.model.fromcommunication.ToolFeedbackDataBD

interface EncoderResponseParser {

    fun parseEncoderResponse(encoderResponse : ByteArray) : ToolFeedbackDataBD
}