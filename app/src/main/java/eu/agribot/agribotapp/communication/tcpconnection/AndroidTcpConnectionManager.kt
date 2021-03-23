package eu.agribot.agribotapp.communication.tcpconnection

import com.google.firebase.crashlytics.FirebaseCrashlytics
import eu.agribot.agribotapp.communication.tcpconnection.TcpConnectionDetails.IP_ADDRESS
import eu.agribot.agribotapp.communication.tcpconnection.TcpConnectionDetails.PORT_NUMBER
import eu.agribot.agribotapp.communication.tcpconnection.TcpConnectionDetails.WAKEUP_MESSAGE
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.Socket

class AndroidTcpConnectionManager : TcpConnectionManager {

    override suspend fun connectWithAgribot(onConnectionError: (exception: Exception) -> Unit) {
        onConnect(onConnectionError)
    }

    private suspend fun onConnect(
        onConnectionError: (exception: Exception) -> Unit
    ) =
        withContext(Dispatchers.IO) {
            try {
                val tcpClient = Socket(IP_ADDRESS, PORT_NUMBER)
                sendWakeupMessage(tcpClient)
            } catch (exception: Exception) {
                FirebaseCrashlytics.getInstance().log("Tcp connection exception :$exception")
                FirebaseCrashlytics.getInstance().recordException(exception)
                onConnectionError(exception)
            }
        }

    private fun sendWakeupMessage(client: Socket) {
        client.outputStream.write(WAKEUP_MESSAGE.toByteArray())
        closeConnection(client)
    }

    private fun closeConnection(client: Socket) {
        client.close()
    }
}
