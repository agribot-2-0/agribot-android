package eu.agribot.agribotapp.domain.service

import eu.agribot.agribotapp.communication.tcpconnection.TcpConnectionManager

class InternetConnectionService(private val tcpConnectionManager: TcpConnectionManager) {

    suspend fun wakeupAgribot(onConnectionError: (error: String) -> Unit) {
        tcpConnectionManager.wakeupAgribot(onConnectionError = { onConnectionError(it.toString()) })
    }

    suspend fun sleepAgribot(onConnectionError: (error: String) -> Unit) {
        tcpConnectionManager.sleepAgribot(onConnectionError = { onConnectionError(it.toString()) })
    }
}
