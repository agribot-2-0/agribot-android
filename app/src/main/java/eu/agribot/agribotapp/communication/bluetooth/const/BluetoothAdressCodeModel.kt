package eu.agribot.agribotapp.communication.bluetooth.const

/**
 * Class with command code to communicate with Agribot
 */

object BluetoothAdressCodeModel {
    const val MOBILE_HEARTBEAT = 1

    const val MOBILE_HANDSHAKE = 10

    const val AGRIBOT_HANDSHAKE = 11

    const val MANUAL_STARTING_INIT = 20

    const val MANUAL_START = 21

    const val AUTO_START = 30

    const val AGRIBOT_ERROR = 200

    const val START_ENGINE = 90

    const val STOP_SIGNAL = 255

    const val MOVE_FORWARD_BACKWARD = 50

    const val ROTATE_CARRIAGES = 51

    const val RIDE_THE_CURVE = 53

    const val AGRIBOT_SET_SWITCH = 60

    const val AGRIBOT_DIAGNOSTIC_DATA_1 = 100
}
