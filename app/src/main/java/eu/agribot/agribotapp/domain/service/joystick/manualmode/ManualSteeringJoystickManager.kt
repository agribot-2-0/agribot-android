package eu.agribot.agribotapp.domain.service.joystick.manualmode

import android.util.Log
import eu.agribot.agribotapp.communication.bluetooth.basic.message.writemessage.messagecreator.BluetoothMessageCreator
import eu.agribot.agribotapp.domain.model.fromdomain.ArcParameters
import eu.agribot.agribotapp.domain.service.joystick.manualmode.JoystickConstants.FORWARD_RANGE_MAX
import eu.agribot.agribotapp.domain.service.joystick.manualmode.JoystickConstants.FORWARD_RANGE_MIN
import eu.agribot.agribotapp.domain.service.joystick.manualmode.JoystickConstants.SPEED_CONVENTER

class ManualSteeringJoystickManager(
    private val messageCreator: BluetoothMessageCreator
) {

    fun onJoystickConfigurationChanged(angle: Int, strength: Int) {
        val radius: Double = getRadiusValue(angle)
        val direction = getDirectionValue(radius)
        val speed = (getSpeedValue(strength))
        val forward = getForwardValue(angle)

        if (speed != 0.toDouble()) {
            sendChangedJoystickParmaetrs(
                ArcParameters(
                    radius,
                    direction,
                    forward,
                    speed
                )
            )
        }
        Log.e("JOYSTICK", " ANGLE : " + angle + " RADIUS: " + radius + " DIRECTION: " + direction + " FORWARD: " + forward + "SPEED: " + speed)
    }

    private fun getRadiusValue(joystickAngle: Int): Double {
        var joystickRadius: Double = 0.toDouble()

        when (joystickAngle) {
            in AngleRange.FRONT.minValue..AngleRange.FRONT.maxValue -> {
                joystickRadius = RadiusData.FRONT.value
            }
            in AngleRange.LOW_FRONT_RIGHT.minValue..AngleRange.LOW_FRONT_RIGHT.maxValue -> {
                joystickRadius = RadiusData.LOW_FRONT_RIGHT.value
            }
            in AngleRange.HARD_FRONT_RIGHT.minValue..AngleRange.HARD_FRONT_RIGHT.maxValue -> {
                joystickRadius = RadiusData.HARD_FRONT_RIGHT.value
            }
            in AngleRange.HARD_BACK_RIGHT.minValue..AngleRange.HARD_BACK_RIGHT.maxValue -> {
                joystickRadius = RadiusData.HARD_BACK_RIGHT.value
            }
            in AngleRange.LOW_BACK_RIGHT.minValue..AngleRange.LOW_BACK_RIGHT.maxValue -> {
                joystickRadius = RadiusData.LOW_BACK_RIGHT.value
            }
            in AngleRange.BACK.minValue..AngleRange.BACK.maxValue -> {
                joystickRadius = RadiusData.BACK.value
            }
            in AngleRange.LOW_BACK_LEFT.minValue..AngleRange.LOW_BACK_LEFT.maxValue -> {
                joystickRadius = RadiusData.LOW_BACK_LEFT.value
            }
            in AngleRange.HARD_BACK_LEFT.minValue..AngleRange.HARD_BACK_LEFT.maxValue -> {
                joystickRadius = RadiusData.HARD_BACK_LEFT.value
            }
            in AngleRange.HARD_FRONT_LEFT.minValue..AngleRange.HARD_FRONT_LEFT.maxValue -> {
                joystickRadius = RadiusData.HARD_FRONT_LEFT.value
            }
            in AngleRange.LOW_FRONT_LEFT.minValue..AngleRange.LOW_FRONT_LEFT.maxValue -> {
                joystickRadius = RadiusData.LOW_FRONT_LEFT.value
            }
        }
        return joystickRadius
    }

    private fun getDirectionValue(radius: Double): Int {
        return if (radius > 0) JoystickDirection.RIGHT.directory else JoystickDirection.LEFT.directory
    }

    private fun getSpeedValue(strength: Int): Double {
        return (strength * SPEED_CONVENTER)
    }

    private fun getForwardValue(angle: Int): Int {
        return if (angle in FORWARD_RANGE_MIN..FORWARD_RANGE_MAX) JoystickForward.FORWARD.value else JoystickForward.BACKWARD.value
    }

    private fun sendChangedJoystickParmaetrs(arcParameters: ArcParameters) {
        messageCreator.createARCMessage(arcParameters.toCommunicationModel())
    }
}
