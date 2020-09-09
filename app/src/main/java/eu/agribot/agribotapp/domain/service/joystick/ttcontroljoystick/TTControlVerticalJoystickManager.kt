package eu.agribot.agribotapp.domain.service.joystick.ttcontroljoystick

import eu.agribot.agribotapp.communication.bluetooth.message.writemessage.WriteBluetoothMessageManager
import eu.agribot.agribotapp.domain.service.joystick.ttcontroljoystick.TTControlJoystickConstant.VERTICAL_QUATER_MAXIMUM_ANGLE
import eu.agribot.agribotapp.domain.service.joystick.ttcontroljoystick.TTControlJoystickConstant.VERTICAL_QUATER_MINIMUM_ANGLE

class TTControlVerticalJoystickManager(private val bluetoothMessageWriteManager: WriteBluetoothMessageManager) {

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
        bluetoothMessageWriteManager.sendTTControlForwardMessage(
            isFrontKartActive,
            isRearKartActive
        )
    }

    private fun goBackward(
        isFrontKartActive: Boolean,
        isRearKartActive: Boolean
    ) {
        bluetoothMessageWriteManager.sendTTControlBackwardMessage(
            isFrontKartActive,
            isRearKartActive
        )
    }
}
