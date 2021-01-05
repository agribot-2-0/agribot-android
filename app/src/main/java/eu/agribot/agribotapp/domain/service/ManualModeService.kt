package eu.agribot.agribotapp.domain.service

import eu.agribot.agribotapp.communication.bluetooth.basic.message.receivedmessage.ReceivedBluetoothMessageManager
import eu.agribot.agribotapp.communication.bluetooth.basic.message.writemessage.WriteBluetoothMessageManager
import eu.agribot.agribotapp.communication.bluetooth.basic.message.writemessage.messagecreator.BluetoothMessageCreator
import eu.agribot.agribotapp.domain.model.fromdomain.SwitchSettings
import eu.agribot.agribotapp.domain.model.todomain.DiagnosticData
import eu.agribot.agribotapp.domain.service.joystick.manualmode.ManualSteeringJoystickManager
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ManualModeService(
    private val manualSteeringJoystickManager: ManualSteeringJoystickManager,
    private val writeBluetoothMessageManager: WriteBluetoothMessageManager,
    private val receivedBluetoothMessageManager: ReceivedBluetoothMessageManager,
    private val messageCreator: BluetoothMessageCreator
) {
    var startStopEngineScope = MainScope()
    private val engineStartDelay = 100L

    fun onManualSteeringJoystickParametrChanged(joystickAngle: Int, joystickStrength: Int) {
        manualSteeringJoystickManager.onJoystickConfigurationChanged(
                joystickAngle,
                joystickStrength
        )
    }

    fun startStopEngine() {
        startStopEngineScope.launch {
            writeBluetoothMessageManager.sendStopThreadMessage()
            delay(engineStartDelay)
            writeBluetoothMessageManager.sendStartEngineMessage()
        }
        startStopEngineScope.cancel()
    }

    fun onSwitchParametrChanged(switchSettings: SwitchSettings) {
        messageCreator.createSwitchesMessage(switchSettings.toCommunicationModel())
    }

    fun getDiagnosticData(): DiagnosticData? {
        return receivedBluetoothMessageManager.getDiagnosticData()?.toDomainModel()
    }
}
