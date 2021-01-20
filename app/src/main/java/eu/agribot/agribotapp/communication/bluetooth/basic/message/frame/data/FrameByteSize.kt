package eu.agribot.agribotapp.communication.bluetooth.basic.message.frame.data

enum class FrameByteSize(val size: Int) {

    START_OF_FRAME_BYTE(1),

    LENGTH_BYTE(1),

    COMMNAD_BYTE (1),

    ADDRESS_BYTE(1),

    CHECKSUM_BYTE (1),

    END_OF_FRAME_BYTE(1),

    DIFFERENCE_BETWEEN_SIZE_AND_POSITION(1)
}