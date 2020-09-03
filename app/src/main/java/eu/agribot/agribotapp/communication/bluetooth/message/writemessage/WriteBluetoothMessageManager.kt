package eu.agribot.agribotapp.communication.bluetooth.message.writemessage

import android.bluetooth.BluetoothSocket

interface WriteBluetoothMessageManager {

    fun sendHandshakeMessage(bluetoothSocket: BluetoothSocket?)
    fun sendManualControlInitMessage()
    fun sendManualControlStartMessage()
    fun sendManualSteeringDataMessage(
        arcParametrsArray: ByteArray
    )

    fun sendAutonomusModeInitMessage()
    fun sendStartEngineMessage()
    fun sendStopThreadMessage()
    fun sendSwitchSettingsMessage(
        switchSettingsArray: ByteArray
    )

    fun sendDiagnosticDataRequestMessage()
    fun sendOverrideModeVerticalSteeringMessage()
    fun sendOverrideModeHorizontalSteeringMessage()
    fun startSendingHeartbeat()
}