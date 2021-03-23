package eu.agribot.agribotapp.communication.tcpconnection

interface TcpConnectionManager {

    suspend fun wakeupAgribot(onConnectionError: (exception: Exception) -> Unit)

    suspend fun sleepAgribot(onConnectionError: (exception: Exception) -> Unit)
}
