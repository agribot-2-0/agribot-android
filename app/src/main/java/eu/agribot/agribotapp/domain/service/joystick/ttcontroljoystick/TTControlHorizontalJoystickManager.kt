package eu.agribot.agribotapp.domain.service.joystick.ttcontroljoystick

import eu.agribot.agribotapp.communication.bluetooth.message.writemessage.WriteBluetoothMessageManager
import eu.agribot.agribotapp.domain.service.joystick.ttcontroljoystick.TTControlJoystickConstant.HORIZONTAL_QUATER_MAXIMUM_ANGLE
import eu.agribot.agribotapp.domain.service.joystick.ttcontroljoystick.TTControlJoystickConstant.HORIZONTAL_QUATER_MINIMUM_ANGLE

class TTControlHorizontalJoystickManager(private val bluetoothMessageWriteManager: WriteBluetoothMessageManager) {

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
        bluetoothMessageWriteManager.sendTTControlTurnLeftMessage(
            isFrontKartActive,
            isRearKartActive
        )
    }

    private fun turnRight(
        isFrontKartActive: Boolean,
        isRearKartActive: Boolean
    ) {
        bluetoothMessageWriteManager.sendTTControlTurnRightMessage(
            isFrontKartActive,
            isRearKartActive
        )
    }
}
