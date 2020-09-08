package eu.agribot.agribotapp.communication.bluetooth.message.writemessage

import android.bluetooth.BluetoothSocket
import eu.agribot.agribotapp.domain.model.ArcParametrs

interface WriteBluetoothMessageManager {

    fun sendHandshakeMessage(bluetoothSocket: BluetoothSocket?)
    fun sendManualControlInitMessage()
    fun sendManualControlStartMessage()
    fun sendManualSteeringDataMessage(
        arcParametrs: ArcParametrs
    )

    fun sendAutonomusModeInitMessage()
    fun sendStartEngineMessage()
    fun sendStopThreadMessage()
    fun sendSwitchSettingsMessage(
        switchSettingsArray: ByteArray
    )

    fun sendDiagnosticDataRequestMessage()
    fun sendTTControlForwardMessage(
        isFrontKartActive: Boolean,
        isRearKartActive: Boolean
    )

    fun sendTTControlBackwardMessage(
        isFrontKartActive: Boolean,
        isRearKartActive: Boolean
    )

    fun sendTTControlTurnLeftMessage(
        isFrontKartActive: Boolean,
        isRearKartActive: Boolean
    )

    fun sendTTControlTurnRightMessage(
        isFrontKartActive: Boolean,
        isRearKartActive: Boolean
    )

    fun startSendingHeartbeat()
    fun stopSendingHeartbeat()
}