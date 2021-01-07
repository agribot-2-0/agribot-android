package eu.agribot.agribotapp.domain.service

import eu.agribot.agribotapp.communication.bluetooth.basic.message.receivedmessage.ReceivedBluetoothMessageManager
import eu.agribot.agribotapp.communication.bluetooth.basic.message.writemessage.WriteBluetoothMessageManager
import eu.agribot.agribotapp.communication.bluetooth.basic.message.writemessage.messagecreator.BluetoothMessageCreator
import eu.agribot.agribotapp.domain.model.fromdomain.SwitchSettings
import eu.agribot.agribotapp.domain.model.todomain.DiagnosticData
import eu.agribot.agribotapp.domain.service.joystick.manualmode.ManualSteeringJoystickManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class ManualModeService(
    private val manualSteeringJoystickManager: ManualSteeringJoystickManager,
    private val writeBluetoothMessageManager: WriteBluetoothMessageManager,
    private val receivedBluetoothMessageManager: ReceivedBluetoothMessageManager,
    private val messageCreator: BluetoothMessageCreator
) {
    var startStopEngineScope = CoroutineScope(Job() + Dispatchers.IO)
    private val engineStartDelay = 100L
    private val diagnosticDataFlow = MutableStateFlow<DiagnosticData?>(null)
    var diagnosticDataFlowScope = CoroutineScope(Job() + Dispatchers.IO)

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

    private fun getDiagnosticData() {
        diagnosticDataFlowScope.launch {
            receivedBluetoothMessageManager.observeDiagnosticData()?.collect {
                if (it != null) {
                    diagnosticDataFlow.value = it.toDomainModel()
                }
            }
        }
    }

    fun observeDiagnosticData(): MutableStateFlow<DiagnosticData?> {
        getDiagnosticData()
        return diagnosticDataFlow
    }
}
