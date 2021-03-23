package eu.agribot.agribotapp.communication.tcpconnection

import eu.agribot.agribotapp.communication.tcpconnection.TcpConnectionDetails.SLEEP_IP_ADDRESS
import eu.agribot.agribotapp.communication.tcpconnection.TcpConnectionDetails.SLEEP_MESSAGE
import eu.agribot.agribotapp.communication.tcpconnection.TcpConnectionDetails.SLEEP_PORT_NUMBER
import eu.agribot.agribotapp.communication.tcpconnection.TcpConnectionDetails.WAKEUP_IP_ADDRESS
import eu.agribot.agribotapp.communication.tcpconnection.TcpConnectionDetails.WAKEUP_MESSAGE
import eu.agribot.agribotapp.communication.tcpconnection.TcpConnectionDetails.WAKE_UP_PORT_NUMBER
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.Socket

class AndroidTcpConnectionManager : TcpConnectionManager {

    override suspend fun wakeupAgribot(onConnectionError: (exception: Exception) -> Unit) {
        connectWithAgribot(WAKEUP_IP_ADDRESS, WAKE_UP_PORT_NUMBER, WAKEUP_MESSAGE, onConnectionError)
    }

    override suspend fun sleepAgribot(onConnectionError: (exception: Exception) -> Unit) {
        connectWithAgribot(SLEEP_IP_ADDRESS, SLEEP_PORT_NUMBER, SLEEP_MESSAGE, onConnectionError)
    }

    private suspend fun connectWithAgribot(
        ipAddress: String,
        ipPort: Int,
        message: String,
        onConnectionError: (exception: Exception) -> Unit
    ) =
            withContext(Dispatchers.IO) {
                try {
                    val tcpClient = Socket(ipAddress, ipPort)
                    sendByteMessage(tcpClient, message)
                } catch (exception: Exception) {
                    onConnectionError(exception)
                }
            }

    private fun sendByteMessage(client: Socket, message: String) {
        client.outputStream.write(message.toByteArray())
        closeConnection(client)
    }

    private fun closeConnection(client: Socket) {
        client.close()
    }
}
