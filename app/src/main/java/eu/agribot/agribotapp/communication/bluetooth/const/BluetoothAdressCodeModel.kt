package eu.agribot.agribotapp.communication.bluetooth.const

/**
 * Class with command code to communicate with Agribot
 */

enum class BluetoothAdressCodeModel(val number: Int) {

    //  Sensor data info frames
    AGRIBOT_DIAGNOSTIC_DATA_1(0x064),

    HYDRAULIC_DIAGNOSTIC_DATA_(0x069),

    //  State changing and command frames
    MOBILE_HEARTBEAT(0x001),

    MOBILE_HANDSHAKE(0x00A),

    AGRIBOT_HANDSHAKE(0x00B),

    MANUAL_STARTING_INIT(0x014),

    MANUAL_START(0x015),

    AUTO_START(0x01E),

    SAFE_MODE(0x028),

    //  Error frames
    AGRIBOT_ERROR(0x0C8),

    //  Manual command frames
    MOVE_FORWARD_BACKWARD(0x032),

    ROTATE_CARRIAGES(0x033),

    RIDE_THE_CURVE(0x035),

    START_ENGINE(0x05A),

    STOP_SIGNAL(0x0FF),

    CATERPILLAR_CART_FRONT(0x1F5),

    CATERPILLAR_CART_BACK(0x1F6),

    CHANGE_STEERING_MODE(0x16C),

    //  Tools control
    AGRIBOT_SET_SWITCH(0x03C),

    TOOL_REQUEST(0x13D),

    TOOL_FEEDBACK(0x13F),

    //  RFID frames
    RFID_TAG(0x1F4)
}
