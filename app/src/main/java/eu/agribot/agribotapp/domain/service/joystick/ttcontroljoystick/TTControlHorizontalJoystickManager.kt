package eu.agribot.agribotapp.domain.service.joystick.ttcontroljoystick

import eu.agribot.agribotapp.communication.bluetooth.basic.message.writemessage.messagecreator.BluetoothMessageCreator
import eu.agribot.agribotapp.domain.model.fromdomain.TuzActivate
import eu.agribot.agribotapp.domain.service.joystick.ttcontroljoystick.TTControlJoystickConstant.HORIZONTAL_QUATER_MAXIMUM_ANGLE
import eu.agribot.agribotapp.domain.service.joystick.ttcontroljoystick.TTControlJoystickConstant.HORIZONTAL_QUATER_MINIMUM_ANGLE

class TTControlHorizontalJoystickManager(private val messageCreator: BluetoothMessageCreator) {

    fun onHorizontalJoystickAngleChanged(
        angle: Int,
        isFrontKartActive: Boolean,
        isRearKartActive: Boolean
    ) {
        when (angle) {
            in HORIZONTAL_QUATER_MINIMUM_ANGLE..HORIZONTAL_QUATER_MAXIMUM_ANGLE -> {
                turnLeft(isFrontKartActive, isRearKartActive)
            }
            else -> {
                turnRight(isFrontKartActive, isRearKartActive)
            }
        }
    }

    private fun turnLeft(
        isFrontKartActive: Boolean,
        isRearKartActive: Boolean
    ) {
        messageCreator.createTTControlLeftMessage(
            TuzActivate(
                isFrontKartActive,
                isRearKartActive
            ).toCommunicationModel()
        )
    }

    private fun turnRight(
        isFrontKartActive: Boolean,
        isRearKartActive: Boolean
    ) {
        messageCreator.createTTControlRightMessage(
            TuzActivate(
                isFrontKartActive,
                isRearKartActive
            ).toCommunicationModel()
        )
    }
}
