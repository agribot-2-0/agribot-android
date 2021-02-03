package eu.agribot.agribotapp.communication.bluetooth.const

/**
 * Class with command code to communicate with Agribot
 */

enum class BluetoothAdressCodeModel(val number: Int) {
    MOBILE_HEARTBEAT(1),

    MOBILE_HANDSHAKE(10),

    AGRIBOT_HANDSHAKE(11),

    MANUAL_STARTING_INIT(20),

    MANUAL_START(21),

    AUTO_START(30),

    AGRIBOT_ERROR(200),

    START_ENGINE(90),

    STOP_SIGNAL(255),

    MOVE_FORWARD_BACKWARD(50),

    ROTATE_CARRIAGES(51),

    RIDE_THE_CURVE(53),

    AGRIBOT_SET_SWITCH(60),

    AGRIBOT_DIAGNOSTIC_DATA_1(100),

    RFID_TAG(500)
}
