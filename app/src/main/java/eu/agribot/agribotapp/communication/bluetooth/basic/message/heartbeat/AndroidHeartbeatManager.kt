package eu.agribot.agribotapp.communication.bluetooth.basic.message.heartbeat

import eu.agribot.agribotapp.communication.bluetooth.basic.message.frame.FrameConstant
import eu.agribot.agribotapp.communication.bluetooth.basic.message.receivedmessage.ConstantReceivedMessage
import eu.agribot.agribotapp.communication.bluetooth.basic.message.writemessage.WriteBluetoothMessageManager
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class AndroidHeartbeatManager(private val bluetoothWriteBluetoothMessageManager: WriteBluetoothMessageManager) : HeartbeatManager {
    private var heartbeatArray = ByteArray(FrameConstant.EMPTY_CAN_ARRAY_SIZE)
    private var nextHeartbeatScope = MainScope()
    private var heartbeatCounterScope = MainScope()
    private var heartbeatFlag: Boolean = false

    companion object {
        const val HEARTBEAT_VALUE_POSITION = 0
        const val HEARTBEAT_INIT_VALUE = 0.toByte()
        const val HEARTBEAT_DELAY = 180L
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
                delay(HEARTBEAT_DELAY)
                heartbeatArray[HEARTBEAT_VALUE_POSITION]++
                bluetoothWriteBluetoothMessageManager.writeHeartbeatMessage(heartbeatArray)
            }

        }
    }


    private fun startHeartbeatCounter() {
        heartbeatCounterScope.launch {
            var heartbeatCounter = ConstantReceivedMessage.HEARTBEAT_INIT_VALUE
            while (heartbeatCounter < ConstantReceivedMessage.HEARTBEAT_COUNTER_MAX_VALUE) {
                setHeartbeatFlag(true)
                delay(ConstantReceivedMessage.HEARTBEAT_COUNTER_DELAY.toLong())
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
        heartbeatCounterScope = MainScope()
        nextHeartbeatScope = MainScope()
    }


}