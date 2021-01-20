package eu.agribot.agribotapp.communication.bluetooth.basic.message.frame.data

enum class FramePositions(val position: Int) {

    START_OF_FRAME(0),

    LENGTH(1),

    COMMAND_BYTE(2),

    FIRST_BYTE_OF_ADRESS(3),

    SECOND_BYTE_OF_ADRESS(4),

    START_MESSAGE_DATA(5),

    START_OF_CHECKSUM_ARRAY(1),

    INITIALIZATION_CHECKSUM_BYTE(0)
}
