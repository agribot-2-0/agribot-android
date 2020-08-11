package eu.agribot.agribotapp.domain.service

import eu.agribot.agribotapp.domain.model.BluetoothAdressCodeModel
import eu.agribot.agribotapp.domain.model.BluetoothAdressCodeModel.Companion.RIDE_THE_CURVE
import eu.agribot.agribotapp.domain.model.BluetoothMessageModel
import kotlin.math.abs

class ManualModeService(private val bluetoothUtils: BluetoothUtils) {

    companion object {
        private const val MAX_ARCH = 11.0
        private const val MIN_ARCH = 1.8
        private const val A_FACTOR_LEFT = 0.15333333333333332
        private const val B_FACTOR_LEFT = 13.3
        private const val A_FACTOR_LEFT_SECOND = -0.15333333333333332
        private const val B_FACTOR_LEFT_SECOND = -14.299999999999997
        private const val A_FACTOR_RIGHT = 0.15333333333333332
        private const val B_FACTOR_RIGHT = -13.3
        private const val A_FACTOR_RIGHT_SECOND = -0.15333333333333332
        private const val B_FACTOR_RIGHT_SECOND = 14.299999999999997
    }

    private val singleArray = ByteArray(1) { 255.toByte() }
    var moveArcArray = ByteArray(8)

    fun sendManualControlInitRequest() {
        bluetoothUtils.writeMessage(
            BluetoothMessageModel(
                BluetoothMessageModel.CMD_ONE, BluetoothAdressCodeModel.MANUAL_STARTING_INIT,
                ByteArray(1)
            ).byteArray
        )
    }

    fun sendStartManualControlFrame() {
        bluetoothUtils.writeMessage(
            BluetoothMessageModel(
                BluetoothMessageModel.CMD_ONE, BluetoothAdressCodeModel.MANUAL_START,
                ByteArray(1)
            ).byteArray
        )
    }

    fun runAgribotEngine() {
        bluetoothUtils.writeMessage(
            BluetoothMessageModel(
                BluetoothMessageModel.CMD_ONE, BluetoothAdressCodeModel.START_ENGINE, singleArray
            ).byteArray
        )
    }

    fun stopAgribotEngine() {
        bluetoothUtils.writeMessage(
            BluetoothMessageModel(
                BluetoothMessageModel.CMD_ONE, BluetoothAdressCodeModel.STOP_SIGNAL, singleArray
            ).byteArray
        )
    }

    fun onJoystickConfigurationChanged(angle: Int, strength: Int) {
        var joystickAngle = 0
        var radius: Double = MAX_ARCH

        when (angle) {
            in 90..269 -> {
                joystickAngle = -angle + 90
            }
            in 0..89 -> {
                joystickAngle = -angle + 90
            }
            in 270..360 -> {
                val beta = -(angle - 90)
                joystickAngle = if (beta <= 180) {
                    beta + 360
                } else {
                    beta
                }
            }
        }

        if (strength == 0) {
            radius = 0.0
        } else {
<<<<<<< HEAD

            if (joystickAngle < -15 && joystickAngle > 15) {
                radius = MAX_ARCH
            } else if (joystickAngle < -165 || joystickAngle > 165) {
                radius = MAX_ARCH
            } else if (joystickAngle < -15 && joystickAngle > -75) {
                radius =
                    A_FACTOR_LEFT * joystickAngle + B_FACTOR_LEFT
            } else if (joystickAngle < -75 && joystickAngle > -105) {
                radius = MIN_ARCH
            } else if (joystickAngle < -105 && joystickAngle > -165) {
                radius =
                    A_FACTOR_LEFT_SECOND * joystickAngle + B_FACTOR_LEFT_SECOND
            } else if (joystickAngle in 15..75) {
                radius =
                    A_FACTOR_RIGHT * joystickAngle + B_FACTOR_RIGHT
            } else if (joystickAngle in 76..105) {
                radius = -MIN_ARCH
            } else if (joystickAngle in 106..165) {
                radius =
                    A_FACTOR_RIGHT_SECOND * joystickAngle + B_FACTOR_RIGHT_SECOND
            }
            val direction = if (radius > 0) 1 else 0
            val speed = (strength * 2.3) * 0.3 / 152.0f
            val forward = if (angle in 181..359) 0 else 1

            if (strength < 20) {
                sendInitJoystickParametrs(0, 0, 0)
            } else {
                sendChangedJoystickParametrs(radius, direction, forward, speed)
            }
=======
            sendChangedJoystickParametrs(radius, direction, forward, speed)
>>>>>>> 0bd8dbd... static code analyze fix
        }
    }

    private fun sendInitJoystickParametrs(fwd: Int, dir: Int, speed: Int) {
        moveArcArray[0] = fwd.toByte()
        moveArcArray[1] = dir.toByte()
        moveArcArray[2] = 0
        moveArcArray[3] = 110.toByte()
        moveArcArray[4] = 0
        moveArcArray[5] = speed.toByte()
        moveArcArray[6] = 0xff.toByte()
        moveArcArray[7] = 0xff.toByte()
        sendChangedJoystickParametrs(0.00, 0, 0, 0.0)
    }

    fun sendChangedJoystickParametrs(radius: Double, direction: Int, forward: Int, speed: Double) {
        moveArcArray[0] = forward.toByte()
        moveArcArray[1] = direction.toByte()
        moveArcArray[2] = 0
        moveArcArray[3] = (abs(radius).toInt() * 10).toByte()
        moveArcArray[4] = 0
        moveArcArray[5] = (abs(speed) * 100).toByte()
        moveArcArray[6] = 0xff.toByte()
        moveArcArray[7] = 0xff.toByte()
        val moveBackFrame =
<<<<<<< HEAD
            BluetoothMessageModel(BluetoothMessageModel.CMD_ONE, RIDE_THE_CURVE, moveArcArray)

        // bluetoothUtils.writeMessage(moveBackFrame.byteArray)
=======
            BluetoothMessageModel(
                BluetoothMessageModel.CMD_ONE,
                RIDE_THE_CURVE,
                moveArcArray
            )

        bluetoothUtils.writeMessage(moveBackFrame.byteArray)
    }

    fun stopHeartbeat() {
        bluetoothUtils.stopSendingHeartbeat()
    }

    fun sendHeartbeatError() {
        bluetoothUtils.setWrongHeartbeat()
    }

    fun sendHanshakeAndHeartbeat() {
        Handler().postDelayed({
            bluetoothUtils.restartHandshakeAndHeartbeat()
        }, RESTERT_HEARTBEAT_DELAY)
>>>>>>> 0bd8dbd... static code analyze fix
    }
}
