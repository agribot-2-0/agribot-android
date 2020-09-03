package eu.agribot.agribotapp.communication.bluetooth.connection

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothServerSocket
import android.bluetooth.BluetoothSocket
import android.os.Handler
import android.os.HandlerThread
import android.os.Process
import eu.agribot.agribotapp.communication.bluetooth.const.ConstatntConnectionValue.AGRIBOT_UUID_INSECURE
import eu.agribot.agribotapp.communication.bluetooth.const.ConstatntConnectionValue.AGRIBOT_UUID_SECURE
import eu.agribot.agribotapp.communication.bluetooth.error.connection.BluetoothConnectionErrorHandler
import eu.agribot.agribotapp.communication.bluetooth.message.receivedmessage.ReceivedBluetoothMessageManager
import eu.agribot.agribotapp.communication.bluetooth.message.writemessage.WriteBluetoothMessageManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException

class AndroidBluetoothConnectionManager(
    private val bluetoothConnectionErrorHandler: BluetoothConnectionErrorHandler,
    private val bluetoothReceivedBluetoothMessageManager : ReceivedBluetoothMessageManager,
    private val bluetoothMessageManager: WriteBluetoothMessageManager
) : BluetoothConnectionManager {
    private var bluetoothSocket: BluetoothSocket? = null


    override fun checkIsBluetoothEnable(): Boolean = when {
        bluetoothAdapter.isEnabled -> true
        else -> false
    }

    override fun enableBluetooth() {
        bluetoothAdapter.enable()
    }

    override suspend fun registerBluetoothServer(isSecurityConnection: Boolean) =
        withContext(Dispatchers.IO) {
            val bluetoothSocketType =
                if (isSecurityConnection) ConnectionSecurity.SECURE.security else ConnectionSecurity.UNSECURE.security

            val bluetoothServerSocket: BluetoothServerSocket? = if (isSecurityConnection) {
                bluetoothAdapter.listenUsingRfcommWithServiceRecord(
                    bluetoothSocketType,
                    AGRIBOT_UUID_SECURE
                )
            } else {
                bluetoothAdapter.listenUsingInsecureRfcommWithServiceRecord(
                    bluetoothSocketType,
                    AGRIBOT_UUID_INSECURE
                )
            }

            run() {
                var shouldLoop = true
                while (shouldLoop) {
                    try {
                        bluetoothServerSocket?.accept()
                    } catch (e: IOException) {
                        shouldLoop = false
                    }
                    bluetoothServerSocket?.also {
                        bluetoothServerSocket.close()
                        shouldLoop = false
                    }
                }
            }
        }

    override suspend fun connectBluetoothWithAgribot(
        bluetoothDeviceAddress: String
    ) {
        onConnect(bluetoothDeviceAddress)
    }

    override fun disconnect() {
        bluetoothSocket?.inputStream?.close()
        bluetoothSocket?.outputStream?.close()
        bluetoothSocket?.close()
    }

    private suspend fun onConnect(
        deviceAdress: String
    ) =
        withContext(Dispatchers.IO) {
            try {
                val bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
                val device: BluetoothDevice = bluetoothAdapter.getRemoteDevice(
                    deviceAdress
                )
                bluetoothSocket = device.createInsecureRfcommSocketToServiceRecord(
                    AGRIBOT_UUID_SECURE
                )
                BluetoothAdapter.getDefaultAdapter().cancelDiscovery()
                bluetoothSocket?.connect()
                bluetoothReceivedBluetoothMessageManager.registerMessageReceiver(bluetoothSocket)
            } catch (bluetoothConnectionError: IOException) {
                bluetoothConnectionErrorHandler.onBluetoothConnectionError(bluetoothConnectionError)
            }
        }


}
