package eu.agribot.agribotapp.domain.model

class BluetoothAdressCodeModel {

    companion object {
        const val MOBILE_HEARTBEAT = 1
        const val AGRIBOT_HEARTBEAT = 2
        const val MOBILE_HANDSHAKE = 10
        const val AGRIBOT_HANDSHAKE = 11
        const val AGRIBOT_ERROR = 200
        const val AGRIBOT_MANUAL_ERROR = 201

        const val MANUAL_STARTING_INIT = 20
        const val MANUAL_START = 21
        const val AUTO_START = 30
        const val AUTO_CONFIRMED = 31

        const val START_ENGINE = 90
        const val STOP_SIGNAL = 255
        const val MOVE_FORWARD_BACKWARD = 50
        const val ROTATE_CARRIAGES = 51
        const val ALIGN_CARRIAGES = 52
        const val RIDE_THE_CURVE = 53
        const val RESET_THE_CURVE = 54
        const val ALIGN_FRONT_OR_REAR = 55
        const val AGRIBOT_SET_SWITCH = 60
        const val AGRIBOT_RPM_ENGINE = 61

        const val GEO_DESTINATION_POINT = 66

        const val AGRIBOT_DIAGNOSTIC_DATA_1 = 100
<<<<<<< HEAD
        const val AGRIBOT_DIAGNOSTIC_DATA_2 = 101
        const val AGRIBOT_DIAGNOSTIC_DATA_3 = 105
        const val AGRIBOT_DIAGNOSTIC_DATA_4 = 106
        const val AGRIBOT_DIAGNOSTIC_DATA_5 = 110
        const val AGRIBOT_DIAGNOSTIC_DATA_6 = 111
        const val AGRIBOT_DIAGNOSTIC_DATA_7 = 112

        const val MOBILE_SWITCHESSTATUS = 120
        const val AGRIBOT_ENGINGERRORS = 121

        var lastMoveCommand = -1
=======
>>>>>>> 0bd8dbd... static code analyze fix
    }
}
