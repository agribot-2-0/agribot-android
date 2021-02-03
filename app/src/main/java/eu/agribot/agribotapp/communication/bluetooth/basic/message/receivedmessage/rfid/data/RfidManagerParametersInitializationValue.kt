package eu.agribot.agribotapp.communication.bluetooth.basic.message.receivedmessage.rfid.data

enum class RfidManagerParametersInitializationValue(val value: Int) {

    RFID_DATA_ARRAY_INITIALIZATION_VALUE(0),

    RFID_ANTENNA_NUMBER_INITIALIZATION_VALUE(-1),

    RFID_END_OF_FRAME_BYTE_INITIALIZATION_VALUE(-1),

    RFID_MESSAGE_COUNTER_INITIALIZATION_VALUE(-1)
}
