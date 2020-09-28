package eu.agribot.agribotapp.domain.service.joystick.ttcontroljoystick

import eu.agribot.agribotapp.communication.bluetooth.basic.message.writemessage.messagecreator.BluetoothMessageCreator
import eu.agribot.agribotapp.domain.model.fromdomain.TuzActivate
import eu.agribot.agribotapp.domain.service.joystick.ttcontroljoystick.TTControlJoystickConstant.VERTICAL_QUATER_MAXIMUM_ANGLE
import eu.agribot.agribotapp.domain.service.joystick.ttcontroljoystick.TTControlJoystickConstant.VERTICAL_QUATER_MINIMUM_ANGLE

class TTControlVerticalJoystickManager(private val messageCreator: BluetoothMessageCreator) {

    fun onVerticalJoystickAngleChanged(
        angle: Int,
        isFrontKartActive: Boolean,
        isRearKartActive: Boolean
    ) {
        when (angle) {
            in VERTICAL_QUATER_MINIMUM_ANGLE..VERTICAL_QUATER_MAXIMUM_ANGLE -> {
                goForward(isFrontKartActive, isRearKartActive)
            }
            else -> {
                goBackward(isFrontKartActive, isRearKartActive)
            }
        }
    }

    private fun goForward(
        isFrontKartActive: Boolean,
        isRearKartActive: Boolean
    ) {
        messageCreator.createTTControlForwardMessage(
            TuzActivate(
                isFrontKartActive,
                isRearKartActive
            ).toCommunicationModel()
        )
    }

    private fun goBackward(
        isFrontKartActive: Boolean,
        isRearKartActive: Boolean
    ) {
        messageCreator.createTTControlBackwardMessage(
            TuzActivate(
                isFrontKartActive,
                isRearKartActive
            ).toCommunicationModel()
        )
    }
}
