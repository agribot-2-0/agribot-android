package eu.agribot.agribotapp.domain.service.joystick.manualmode

import android.annotation.SuppressLint
import eu.agribot.agribotapp.communication.bluetooth.basic.message.writemessage.messagecreator.BluetoothMessageCreator
import eu.agribot.agribotapp.domain.model.fromdomain.ArcParameters
import eu.agribot.agribotapp.domain.service.joystick.manualmode.JoystickConstants.FORWARD_RANGE_MAX
import eu.agribot.agribotapp.domain.service.joystick.manualmode.JoystickConstants.FORWARD_RANGE_MIN
import eu.agribot.agribotapp.domain.service.joystick.manualmode.JoystickConstants.MAX_JOYSTICK_STRENGTH_VALUE
import eu.agribot.agribotapp.domain.service.joystick.manualmode.JoystickConstants.MAX_ROBOT_SPEED_IN_METERS_PER_SECOND
import timber.log.Timber

class ManualSteeringJoystickManager(
    private val messageCreator: BluetoothMessageCreator
) {

    @SuppressLint("LogNotTimber")
    fun onJoystickConfigurationChanged(angle: Int, strength: Int) {
        var radius: Double = getRadiusValue(angle)
        val direction = getDirectionValue(radius)
        val speed = getSpeedValue(strength)
        val forward = getForwardValue(angle)
        if (speed == 0.0)
        {
            radius = 0.0
        }

//        if (speed != NO_REQUEST_SPEED) {
            sendChangedJoystickParmaetrs(
                ArcParameters(
                    radius,
                    direction,
                    forward,
                    speed
                )
            )
//        }

        Timber.d(
            String.format(
                "\nJoystic Param :\n" +
                        " 1. Radius %s |\n" +
                        " 2. Speed %s |\n" +
                        " 3. Forward %s |\n" +
                        " 4. Direction %s |\n ",
                radius.toString(),
                speed.toString(),
                forward.toString(),
                direction.toString()
            )
        )
    }

    private fun getRadiusValue(joystickAngle: Int): Double {
        var joystickRadius = INITIALIZATION_RADIUS

        when (joystickAngle) {
            in AngleRange.FRONT.minValue..AngleRange.FRONT.maxValue -> {
                joystickRadius = RadiusData.STRAIGHT.value
            }
            in AngleRange.LOW_FRONT_RIGHT.minValue..AngleRange.LOW_FRONT_RIGHT.maxValue -> {
                joystickRadius = RadiusData.CLOCKWISE.value * RadiusData.GENTLE.value
            }
            in AngleRange.HARD_FRONT_RIGHT.minValue..AngleRange.HARD_FRONT_RIGHT.maxValue -> {
                joystickRadius = RadiusData.CLOCKWISE.value * RadiusData.SHARP.value
            }
            in AngleRange.HARD_BACK_RIGHT.minValue..AngleRange.HARD_BACK_RIGHT.maxValue -> {
                joystickRadius = RadiusData.CLOCKWISE.value * RadiusData.SHARP.value
            }
            in AngleRange.LOW_BACK_RIGHT.minValue..AngleRange.LOW_BACK_RIGHT.maxValue -> {
                joystickRadius = RadiusData.CLOCKWISE.value * RadiusData.GENTLE.value
            }
            in AngleRange.BACK.minValue..AngleRange.BACK.maxValue -> {
                joystickRadius = RadiusData.STRAIGHT.value
            }
            in AngleRange.LOW_BACK_LEFT.minValue..AngleRange.LOW_BACK_LEFT.maxValue -> {
                joystickRadius = RadiusData.COUNTERCLOCKWISE.value * RadiusData.GENTLE.value
            }
            in AngleRange.HARD_BACK_LEFT.minValue..AngleRange.HARD_BACK_LEFT.maxValue -> {
                joystickRadius = RadiusData.COUNTERCLOCKWISE.value * RadiusData.SHARP.value
            }
            in AngleRange.HARD_FRONT_LEFT.minValue..AngleRange.HARD_FRONT_LEFT.maxValue -> {
                joystickRadius = RadiusData.COUNTERCLOCKWISE.value * RadiusData.SHARP.value
            }
            in AngleRange.LOW_FRONT_LEFT.minValue..AngleRange.LOW_FRONT_LEFT.maxValue -> {
                joystickRadius = RadiusData.COUNTERCLOCKWISE.value * RadiusData.GENTLE.value
            }
        }
        return joystickRadius
    }

    private fun getDirectionValue(radius: Double): Int {
        return if (radius > RIGHT_DIRECTION_VALUE) JoystickDirection.RIGHT.direction else JoystickDirection.LEFT.direction
    }

    private fun getSpeedValue(strength: Int): Double {
        return (strength * MAX_ROBOT_SPEED_IN_METERS_PER_SECOND) / MAX_JOYSTICK_STRENGTH_VALUE
    }

    private fun getForwardValue(angle: Int): Int {
        return if (angle in FORWARD_RANGE_MIN..FORWARD_RANGE_MAX) JoystickForward.FORWARD.value else JoystickForward.BACKWARD.value
    }

    private fun sendChangedJoystickParmaetrs(arcParameters: ArcParameters) {
        messageCreator.createARCMessage(arcParameters.toCommunicationModel())
    }

    companion object {
        const val INITIALIZATION_RADIUS = 0.0
        const val NO_REQUEST_SPEED = 0.0
        const val RIGHT_DIRECTION_VALUE = 0
    }
}
