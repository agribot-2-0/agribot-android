package eu.agribot.agribotapp.communication.bluetooth.basic.connection.message.receivedmessage

object ConstantReceivedMessage {
    const val BATTERY_VOLTAGE_RATIO = 0.001
    const val FUEL_LEVEL_RATIO = 10
    const val VOLTAGE_DROP = 0.8
    const val EIGHT_BYTE_MESSAGE = 8
    const val FOUR_DIGIT_NUMBER = 4
    const val FIRST_DIGIT = 0
    const val FOURTH_DIGIT = 4
    const val START_FRAME_MESSAGE = 0x43.toByte()
    const val START_OF_FRAME_POSITION = 0
    const val LENGHT_BYTE_POSITION = 1

    const val HEARTBEAT_INIT_VALUE = 0
    const val HEARTBEAT_COUNTER_MAX_VALUE = 20
    const val HEARTBEAT_COUNTER_DELAY = 30L
}
