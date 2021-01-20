package eu.agribot.agribotapp.communication.bluetooth.basic.message.receivedmessage.rfid.data

enum class RfidMessageBytePosition(val position: Int) {

    RFID_ANTENNA_NUMBER_BYTE(1),

    RFID_COM_NUMBER(0),

    RFID_MESSAGE_LENGTH(2),

    START_OF_RFID_TAG_DATA_MESSAGE(1),

    END_OF_RFID_TAG_DATA_MESSAGE(7),

    START_RFID_ARRAY_POSITION(0)
}
