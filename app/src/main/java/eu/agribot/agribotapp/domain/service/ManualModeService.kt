package eu.agribot.agribotapp.domain.service

import eu.agribot.agribotapp.common.navigation.events.OverrideNavigationEvent
import eu.agribot.agribotapp.communication.bluetooth.message.writemessage.WriteBluetoothMessageManager
import eu.agribot.agribotapp.domain.service.joystick.manualmode.ManualSteeringJoystickManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext


class ManualModeService(
    private val manualSteeringJoystickManager: ManualSteeringJoystickManager,
    private val bluetoothMessageManager: WriteBluetoothMessageManager
) {
    private val START_MANUAL_MODE_DELAY = 100L
    fun onManualSteeringJoystickParametrChanged(joystickAngle: Int, joystickStrength: Int) {
        manualSteeringJoystickManager.onJoystickConfigurationChanged(
            joystickAngle,
            joystickStrength
        )
    }

    fun startStopEngine() {
        bluetoothMessageManager.sendStopThreadMessage()
        bluetoothMessageManager.sendStartEngineMessage()
    }

    suspend fun runManualMode() =  withContext(Dispatchers.IO) {
        bluetoothMessageManager.sendManualControlInitMessage()
        delay(START_MANUAL_MODE_DELAY)
        bluetoothMessageManager.sendManualControlStartMessage()
    }



}