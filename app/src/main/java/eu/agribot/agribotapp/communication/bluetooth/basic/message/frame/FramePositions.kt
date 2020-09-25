package eu.agribot.agribotapp.communication.bluetooth.basic.connection.message.frame

object FramePositions {
    const val START_OF_FRAME_POSITION = 0
    const val LENGTH_POSITION = 1
    const val COMMAND_BYTE_POSITION = 2
    const val FIRST_BYTE_OF_ADRESS_POSITION = 3
    const val SECOND_BYTE_OF_ADRESS_POSITION = 4
    const val NEXT_POSITION = 1
    const val END_OF_FRAME_POSITION = -1
}
