package eu.agribot.agribotapp.domain.service

import eu.agribot.agribotapp.communication.bluetooth.basic.message.receivedmessage.ReceivedBluetoothMessageManager
import eu.agribot.agribotapp.communication.bluetooth.basic.message.writemessage.WriteBluetoothMessageManager
import eu.agribot.agribotapp.communication.bluetooth.basic.message.writemessage.messagecreator.BluetoothMessageCreator
import eu.agribot.agribotapp.domain.model.fromdomain.SwitchSettings
import eu.agribot.agribotapp.domain.model.todomain.DiagnosticData
import eu.agribot.agribotapp.domain.service.joystick.manualmode.ManualSteeringJoystickManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

class ManualModeService(
    private val manualSteeringJoystickManager: ManualSteeringJoystickManager,
    private val writeBluetoothMessageManager: WriteBluetoothMessageManager,
    private val receivedBluetoothMessageManager: ReceivedBluetoothMessageManager,
    private val messageCreator: BluetoothMessageCreator
) {
    private val START_MANUAL_MODE_DELAY = 100L
    fun onManualSteeringJoystickParametrChanged(joystickAngle: Int, joystickStrength: Int) {
        manualSteeringJoystickManager.onJoystickConfigurationChanged(
            joystickAngle,
            joystickStrength
        )
    }

    fun startStopEngine() {
        writeBluetoothMessageManager.sendStopThreadMessage()
        writeBluetoothMessageManager.sendStartEngineMessage()
    }

    suspend fun runManualMode() = withContext(Dispatchers.IO) {
        writeBluetoothMessageManager.sendManualControlInitMessage()
        delay(START_MANUAL_MODE_DELAY)
        writeBluetoothMessageManager.sendManualControlStartMessage()
    }

    fun onSwitchParametrChanged(switchSettings: SwitchSettings) {
        messageCreator.createSwitchesMessage(switchSettings.toCommunicationModel())
    }

    fun getDiagnosticData(): DiagnosticData? {
        return receivedBluetoothMessageManager.getDiagnosticData()?.toDomainModel()
    }
}
