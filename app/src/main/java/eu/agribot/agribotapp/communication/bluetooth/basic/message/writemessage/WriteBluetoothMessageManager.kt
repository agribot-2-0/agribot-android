package eu.agribot.agribotapp.communication.bluetooth.basic.message.writemessage

interface WriteBluetoothMessageManager {

    fun sendHandshakeMessage()

    fun sendManualControlInitMessage()

    fun sendManualControlStartMessage()

    fun sendManualSteeringDataMessage(arcArray: ByteArray)

    fun sendAutonomusModeInitMessage()

    fun sendStartEngineMessage()

    fun sendStopThreadMessage()

    fun sendSwitchSettingsMessage(switchSettingsArray: ByteArray)

    fun sendDiagnosticDataRequestMessage()

    fun sendTTControlForwardMessage(moveForwardArray: ByteArray)

    fun sendTTControlBackwardMessage(moveBackArray: ByteArray)

    fun sendTTControlTurnLeftMessage(moveLeftArray: ByteArray)

    fun sendTTControlTurnRightMessage(moveRightArray: ByteArray)

    fun stopHandshakeScope()

    fun writeHeartbeatMessage(heartbeatArray: ByteArray)
}
