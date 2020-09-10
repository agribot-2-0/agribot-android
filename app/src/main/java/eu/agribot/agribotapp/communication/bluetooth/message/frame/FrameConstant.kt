package eu.agribot.agribotapp.communication.bluetooth.message.frame

object FrameConstant {
    const val ARC_MESSAGE_ARRAY_SIZE = 8
    const val EMPTY_CAN_ARRAY_SIZE = 1
    const val TTCONTROL_MOVE_BACK_FRONT_ARRAY_SIZE = 8
    const val TTCONTROL_MOVE_LEFT_RIGHT_ARRAY_SIZE = 4

    const val START_FRAME_BYTE = 0x43.toByte()
    const val COMMAND_BYTE = 0x00.toByte()
    const val CONSTANT_FRAME_ELEMENTS_SIZE = 7
    const val EMPTY_ARRAY = 0
    const val DUMMY_BYTE = 0xFF.toByte()
    const val END_OF_FRAME_BYTE = 0x0D.toByte()
    const val CONSTANT_ELEMENTS_OF_FRAME_SIZE = 3
    const val BIT_SHIFT = 8
    const val ADDRESS_FLAG = 256
    const val TWO_ELEMENT_ADDRES = 2
    const val CONST_ELEMENT_OF_FRAME_SIZE = 4
    const val BYTE_ARRAY_OF_CHECKSUM = 0x00.toByte()
}
