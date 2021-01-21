package eu.agribot.agribotapp.domain.service

import eu.agribot.agribotapp.communication.tcpconnection.TcpConnectionManager

class InternetConnectionService(private val tcpConnectionManager: TcpConnectionManager) {

    suspend fun connectWithAgribot(onConnectionError: (error: String) -> Unit) {
        tcpConnectionManager.connectWithAgribot(onConnectionError = { onConnectionError(it.toString()) })
    }
}
