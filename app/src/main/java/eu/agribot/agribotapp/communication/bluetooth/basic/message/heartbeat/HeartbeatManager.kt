package eu.agribot.agribotapp.communication.bluetooth.basic.message.heartbeat

interface HeartbeatManager {

    fun restartHeartbeatCounter()

    fun sendFirstHeartbeat()

    fun sendNextHeartbeat()
}