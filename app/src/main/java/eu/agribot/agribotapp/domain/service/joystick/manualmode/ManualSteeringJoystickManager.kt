package eu.agribot.agribotapp.domain.service.joystick.manualmode

import eu.agribot.agribotapp.communication.bluetooth.message.writemessage.WriteBluetoothMessageManager
import eu.agribot.agribotapp.domain.model.fromdomain.ArcParametrs
import eu.agribot.agribotapp.domain.service.joystick.manualmode.JoystickConstants.A_FACTOR_LEFT
import eu.agribot.agribotapp.domain.service.joystick.manualmode.JoystickConstants.A_FACTOR_LEFT_SECOND
import eu.agribot.agribotapp.domain.service.joystick.manualmode.JoystickConstants.A_FACTOR_RIGHT
import eu.agribot.agribotapp.domain.service.joystick.manualmode.JoystickConstants.A_FACTOR_RIGHT_SECOND
import eu.agribot.agribotapp.domain.service.joystick.manualmode.JoystickConstants.BACKWARD_MAX_ANGLE
import eu.agribot.agribotapp.domain.service.joystick.manualmode.JoystickConstants.BACKWARD_MIN_ANGLE
import eu.agribot.agribotapp.domain.service.joystick.manualmode.JoystickConstants.B_FACTOR_LEFT
import eu.agribot.agribotapp.domain.service.joystick.manualmode.JoystickConstants.B_FACTOR_LEFT_SECOND
import eu.agribot.agribotapp.domain.service.joystick.manualmode.JoystickConstants.B_FACTOR_RIGHT
import eu.agribot.agribotapp.domain.service.joystick.manualmode.JoystickConstants.B_FACTOR_RIGHT_SECOND
import eu.agribot.agribotapp.domain.service.joystick.manualmode.JoystickConstants.FR0NT_LEFT_MAX_ANGLE
import eu.agribot.agribotapp.domain.service.joystick.manualmode.JoystickConstants.FRONT_LEFT_MIN_ANGLE
import eu.agribot.agribotapp.domain.service.joystick.manualmode.JoystickConstants.FRONT_MAX_ANGLE
import eu.agribot.agribotapp.domain.service.joystick.manualmode.JoystickConstants.FRONT_MIN_ANGLE
import eu.agribot.agribotapp.domain.service.joystick.manualmode.JoystickConstants.FRONT_RIGHT_MAX_ANGLE
import eu.agribot.agribotapp.domain.service.joystick.manualmode.JoystickConstants.FRONT_RIGHT_MIN_ANGLE
import eu.agribot.agribotapp.domain.service.joystick.manualmode.JoystickConstants.INITIALIZE_JOYSTICK_ANGLE
import eu.agribot.agribotapp.domain.service.joystick.manualmode.JoystickConstants.LEFT_MAX_ANGLE
import eu.agribot.agribotapp.domain.service.joystick.manualmode.JoystickConstants.LEFT_MIN_ANGLE
import eu.agribot.agribotapp.domain.service.joystick.manualmode.JoystickConstants.MAX_ARCH
import eu.agribot.agribotapp.domain.service.joystick.manualmode.JoystickConstants.MIN_ARCH
import eu.agribot.agribotapp.domain.service.joystick.manualmode.JoystickConstants.REAR_LEFT_MAX_ANGLE
import eu.agribot.agribotapp.domain.service.joystick.manualmode.JoystickConstants.REAR_LEFT_MIN_ANGLE
import eu.agribot.agribotapp.domain.service.joystick.manualmode.JoystickConstants.REAR_MAX_ANGLE
import eu.agribot.agribotapp.domain.service.joystick.manualmode.JoystickConstants.REAR_MIN_ANGLE
import eu.agribot.agribotapp.domain.service.joystick.manualmode.JoystickConstants.REAR_RIGHT_MAX_ANGLE
import eu.agribot.agribotapp.domain.service.joystick.manualmode.JoystickConstants.REAR_RIGHT_MIN_ANGLE
import eu.agribot.agribotapp.domain.service.joystick.manualmode.JoystickConstants.RIGHT_MAX_ANGLE
import eu.agribot.agribotapp.domain.service.joystick.manualmode.JoystickConstants.RIGHT_MIN_ANGLE
import eu.agribot.agribotapp.domain.service.joystick.manualmode.JoystickConstants.SPEED_CONVENTER
import eu.agribot.agribotapp.domain.service.joystick.manualmode.JoystickConstants.STOCK_JOYSTICK_FIRST_QUARTER_MAX
import eu.agribot.agribotapp.domain.service.joystick.manualmode.JoystickConstants.STOCK_JOYSTICK_FIRST_QUARTER_MIN
import eu.agribot.agribotapp.domain.service.joystick.manualmode.JoystickConstants.STOCK_JOYSTICK_FOURTH_QUARTER_MAX
import eu.agribot.agribotapp.domain.service.joystick.manualmode.JoystickConstants.STOCK_JOYSTICK_FOURTH_QUARTER_MIN
import eu.agribot.agribotapp.domain.service.joystick.manualmode.JoystickConstants.STOCK_JOYSTICK_SECOND_THIRD_QUARTER_MAX
import eu.agribot.agribotapp.domain.service.joystick.manualmode.JoystickConstants.STOCK_JOYSTICK_SECOND_THIRD_QUARTER_MIN

class ManualSteeringJoystickManager(private val writeBluetoothMessageManager: WriteBluetoothMessageManager) {

    fun onJoystickConfigurationChanged(angle: Int, strength: Int) {
        var joystickAngle = INITIALIZE_JOYSTICK_ANGLE
        var radius: Double = MAX_ARCH

        when (angle) {
            in STOCK_JOYSTICK_SECOND_THIRD_QUARTER_MIN..STOCK_JOYSTICK_SECOND_THIRD_QUARTER_MAX -> {
                joystickAngle = -angle + JoystickConstants.RIGHT_ANGLE
            }
            in STOCK_JOYSTICK_FIRST_QUARTER_MIN..STOCK_JOYSTICK_FIRST_QUARTER_MAX -> {
                joystickAngle = -angle + JoystickConstants.RIGHT_ANGLE
            }
            in STOCK_JOYSTICK_FOURTH_QUARTER_MIN..STOCK_JOYSTICK_FOURTH_QUARTER_MAX -> {
                val beta = -(angle - JoystickConstants.RIGHT_ANGLE)
                joystickAngle = if (beta <= JoystickConstants.SEMI_TURN_ANGLE) {
                    beta + JoystickConstants.FULL_TURN_ANGLE
                } else {
                    beta
                }
            }
        }

        if (joystickAngle in (FRONT_MIN_ANGLE) until FRONT_MAX_ANGLE) {
            radius =
                MAX_ARCH
        } else if (joystickAngle < REAR_MIN_ANGLE || joystickAngle > REAR_MAX_ANGLE) {
            radius =
                MAX_ARCH
        } else if (joystickAngle in (FRONT_LEFT_MIN_ANGLE) until FR0NT_LEFT_MAX_ANGLE) {
            radius =
                A_FACTOR_LEFT * joystickAngle + B_FACTOR_LEFT
        } else if (joystickAngle in (LEFT_MIN_ANGLE) until LEFT_MAX_ANGLE) {
            radius =
                MIN_ARCH
        } else if (joystickAngle in (REAR_LEFT_MIN_ANGLE) until REAR_LEFT_MAX_ANGLE) {
            radius =
                A_FACTOR_LEFT_SECOND * joystickAngle + B_FACTOR_LEFT_SECOND
        } else if (joystickAngle in FRONT_RIGHT_MIN_ANGLE..FRONT_RIGHT_MAX_ANGLE) {
            radius =
                A_FACTOR_RIGHT * joystickAngle + B_FACTOR_RIGHT
        } else if (joystickAngle in RIGHT_MIN_ANGLE..RIGHT_MAX_ANGLE) {
            radius = -MIN_ARCH
        } else if (joystickAngle in REAR_RIGHT_MIN_ANGLE..REAR_RIGHT_MAX_ANGLE) {
            radius =
                A_FACTOR_RIGHT_SECOND * joystickAngle + B_FACTOR_RIGHT_SECOND
        }
        val direction =
            if (radius > 0) JoystickDirection.RIGHT.directory else JoystickDirection.LEFT.directory
        val speed = (strength * SPEED_CONVENTER)
        val forward =
            if (angle in BACKWARD_MIN_ANGLE..BACKWARD_MAX_ANGLE) JoystickForward.BACKWARD.forward else JoystickForward.FORWARD.forward

        if (angle != 0) {
            sendChangedJoystickParmaetrs(ArcParametrs(radius, direction, forward, speed))
        }
    }

    private fun sendChangedJoystickParmaetrs(arcParametrs: ArcParametrs) {
        writeBluetoothMessageManager.sendManualSteeringDataMessage(arcParametrs.toCommunicationModel())
    }
}
