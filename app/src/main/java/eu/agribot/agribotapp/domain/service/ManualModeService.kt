package eu.agribot.agribotapp.domain.service

import eu.agribot.agribotapp.communication.bluetooth.basic.message.receivedmessage.ReceivedBluetoothMessageManager
import eu.agribot.agribotapp.communication.bluetooth.basic.message.writemessage.WriteBluetoothMessageManager
import eu.agribot.agribotapp.communication.bluetooth.basic.message.writemessage.messagecreator.BluetoothMessageCreator
import eu.agribot.agribotapp.communication.database.clouddatabase.CloudDatabase
import eu.agribot.agribotapp.domain.error.CloudStorageErrorHandler
import eu.agribot.agribotapp.domain.model.fromdomain.SwitchSettings
import eu.agribot.agribotapp.domain.model.todomain.DiagnosticData
import eu.agribot.agribotapp.domain.model.todomain.UserRole
import eu.agribot.agribotapp.domain.service.joystick.manualmode.ManualSteeringJoystickManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class ManualModeService(
    private val manualSteeringJoystickManager: ManualSteeringJoystickManager,
    private val writeBluetoothMessageManager: WriteBluetoothMessageManager,
    private val receivedBluetoothMessageManager: ReceivedBluetoothMessageManager,
    private val messageCreator: BluetoothMessageCreator,
    private val cloudStorageErrorHandler: CloudStorageErrorHandler,
    private val cloudDatabase: CloudDatabase
) {
    private val engineStartDelay = 100L

    @ExperimentalCoroutinesApi
    private val ttControlFlow = MutableStateFlow<Boolean>(false)

    @ExperimentalCoroutinesApi
    private val diagnosticDataFlow = MutableStateFlow<DiagnosticData?>(null)
    var diagnosticDataFlowScope = CoroutineScope(Job() + Dispatchers.IO)

    fun sendManualModeInitMessage() {
        writeBluetoothMessageManager.sendManualControlInitMessage()
    }

    fun onManualSteeringJoystickParametrChanged(joystickAngle: Int, joystickStrength: Int) {
        manualSteeringJoystickManager.onJoystickConfigurationChanged(
            joystickAngle,
            joystickStrength
        )
    }

    suspend fun startStopEngine() {
        writeBluetoothMessageManager.sendStopThreadMessage()
        delay(engineStartDelay)
        writeBluetoothMessageManager.sendStartEngineMessage()
    }

    fun onSwitchParametrChanged(switchSettings: SwitchSettings) {
        messageCreator.createSwitchesMessage(switchSettings.toCommunicationModel())
    }

    @ExperimentalCoroutinesApi
    private fun getDiagnosticData() {
        diagnosticDataFlowScope.launch {
            receivedBluetoothMessageManager.observeDiagnosticData()?.collect {
                if (it != null) {
                    diagnosticDataFlow.value = it.toDomainModel()
                }
            }
        }
    }

    @ExperimentalCoroutinesApi
    fun onTTControlEvent(state: Boolean) {
        ttControlFlow.value = state
    }

    @ExperimentalCoroutinesApi
    fun observeTTControlFlow(): MutableStateFlow<Boolean> {
        return ttControlFlow
    }

    @ExperimentalCoroutinesApi
    fun observeDiagnosticData(): MutableStateFlow<DiagnosticData?> {
        getDiagnosticData()
        return diagnosticDataFlow
    }

    fun getUserRole(
        onGotUserRole: (userRole: UserRole) -> Unit,
        onDatabaseError: (error: String) -> Unit
    ) {
        cloudDatabase.getUserRole(
            onGotUserRole = { onGotUserRole(it.toDomainModel()) },
            onDatabaseError = { onDatabaseError(cloudStorageErrorHandler.getStorageError(it)) }
        )
    }
}
