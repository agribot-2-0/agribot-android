package eu.agribot.agribotapp.communication.bluetooth.basic.message.receivedmessage.rfid.data

enum class RfidMessageParameters(val parameter: Int) {

    RFID_DATA_SIZE(7),

    RFID_INFORMATION_MESSAGE(2),

    RFID_ONE_MESSAGE_DATA(0),

    RFID_NO_RFID_TAG(0),

    COMPLETE_MESSAGE_COUNTER_VALUE(0),

    DECREMENT_COUNTER_VALUE(1),

    RFID_INITIALIZATION_ARRAY_SIZE(0),

    DIFFERENCE_BETWEEN_SIZE_AND_LAST_ARRAY_POSITION(1)
}
