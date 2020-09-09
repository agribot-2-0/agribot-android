package eu.agribot.agribotapp.domain.service

import eu.agribot.agribotapp.domain.service.joystick.ttcontroljoystick.TTControlHorizontalJoystickManager
import eu.agribot.agribotapp.domain.service.joystick.ttcontroljoystick.TTControlVerticalJoystickManager

class TTControlService(
    private val horizontalJoystickManager: TTControlHorizontalJoystickManager,
    private val verticalJoystickManager: TTControlVerticalJoystickManager
) {

    fun onHorizontalJoystickParametrsChanged(
        angle: Int,
        isFrontKartActive: Boolean,
        isRearKartActive: Boolean
    ) {
        horizontalJoystickManager.onHorizontalJoystickAngleChanged(
            angle,
            isFrontKartActive,
            isRearKartActive
        )
    }

    fun onVerticalJoystickParametrsChanged(
        angle: Int,
        isFrontKartActive: Boolean,
        isRearKartActive: Boolean
    ) {
        verticalJoystickManager.onVerticalJoystickAngleChanged(
            angle,
            isFrontKartActive,
            isRearKartActive
        )
    }
}
