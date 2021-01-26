package eu.agribot.agribotapp.communication.bluetooth.basic.message.frame.data

enum class FrameByteValue(val value: Byte) {

    START_OF_FRAME(0x43.toByte()),

    COMMAND_BYTE(0x00.toByte()),

    END_OF_FRAME(0x0D.toByte())
}
