package eu.agribot.agribotapp.communication.tcpconnection

interface TcpConnectionManager {

    suspend fun connectWithAgribot(onConnectionError: (exception: Exception) -> Unit)
}
