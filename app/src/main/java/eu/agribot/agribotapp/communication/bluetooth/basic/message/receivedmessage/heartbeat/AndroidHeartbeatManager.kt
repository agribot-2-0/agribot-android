package eu.agribot.agribotapp.communication.bluetooth.basic.message.heartbeat

import eu.agribot.agribotapp.communication.bluetooth.basic.message.frame.data.FrameConstant
import eu.agribot.agribotapp.communication.bluetooth.basic.message.receivedmessage.data.ConstantReceivedMessage
import eu.agribot.agribotapp.communication.bluetooth.basic.message.writemessage.WriteBluetoothMessageManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class AndroidHeartbeatManager(private val bluetoothWriteBluetoothMessageManager: WriteBluetoothMessageManager) : HeartbeatManager {
    private var heartbeatArray = ByteArray(FrameConstant.EMPTY_CAN_ARRAY_SIZE)
    private var nextHeartbeatScope = CoroutineScope(Job() + Dispatchers.IO)
    private var heartbeatCounterScope = CoroutineScope(Job() + Dispatchers.IO)
    private var heartbeatFlag: Boolean = false

    companion object {
        const val HEARTBEAT_VALUE_POSITION = 0
        const val HEARTBEAT_INIT_VALUE = 0.toByte()
        const val HEARTBEAT_DELAY = 120L
    }

    override fun restartHeartbeatCounter() {
        heartbeatCounterScope.cancel()
        heartbeatCounterScope = MainScope()
        startHeartbeatCounter()
    }

    override fun sendFirstHeartbeat() {
        heartbeatArray[HEARTBEAT_VALUE_POSITION] = HEARTBEAT_INIT_VALUE
        bluetoothWriteBluetoothMessageManager.writeHeartbeatMessage(heartbeatArray)
        setHeartbeatFlag(true)
        sendNextHeartbeat()
    }

    override fun sendNextHeartbeat() {
        nextHeartbeatScope.launch {
            while (getHeartbeatFlagStatus()) {
                heartbeatArray[HEARTBEAT_VALUE_POSITION]++
                bluetoothWriteBluetoothMessageManager.writeHeartbeatMessage(heartbeatArray)
                delay(HEARTBEAT_DELAY)
            }
        }
    }

    private fun startHeartbeatCounter() {
        heartbeatCounterScope.launch {
            var heartbeatCounter = ConstantReceivedMessage.HEARTBEAT_INIT_VALUE
            while (heartbeatCounter < ConstantReceivedMessage.HEARTBEAT_COUNTER_MAX_VALUE) {
                setHeartbeatFlag(true)
                delay(HEARTBEAT_DELAY)
                heartbeatCounter++
            }
            setHeartbeatFlag(false)
            bluetoothWriteBluetoothMessageManager.sendHandshakeMessage()
            stopHeartbeatScopes()
        }
    }

    private fun setHeartbeatFlag(isHeartbeatActive: Boolean) {
        heartbeatFlag = isHeartbeatActive
    }

    private fun getHeartbeatFlagStatus(): Boolean {
        return heartbeatFlag
    }

    private fun stopHeartbeatScopes() {
        heartbeatCounterScope.cancel()
        nextHeartbeatScope.cancel()
        nextHeartbeatScope = MainScope()
        heartbeatCounterScope = MainScope()
    }
}
