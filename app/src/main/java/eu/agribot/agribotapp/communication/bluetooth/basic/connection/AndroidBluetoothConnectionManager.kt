package eu.agribot.agribotapp.communication.bluetooth.basic.connection

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothServerSocket
import android.bluetooth.BluetoothSocket
import eu.agribot.agribotapp.communication.bluetooth.const.PersistentConnectionParameters.AGRIBOT_UUID_INSECURE
import eu.agribot.agribotapp.communication.bluetooth.const.PersistentConnectionParameters.AGRIBOT_UUID_SECURE
import eu.agribot.agribotapp.communication.bluetooth.error.connection.BluetoothConnectionErrorHandler
import eu.agribot.agribotapp.communication.bluetooth.basic.message.receivedmessage.ReceivedBluetoothMessageManager
import eu.agribot.agribotapp.communication.bluetooth.basic.connection.socket.BluetoothSocketManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException

class AndroidBluetoothConnectionManager(
    private val bluetoothConnectionErrorHandler: BluetoothConnectionErrorHandler,
    private val bluetoothReceivedBluetoothMessageManager: ReceivedBluetoothMessageManager,
    private val bluetoothSocketManager: BluetoothSocketManager
) : BluetoothConnectionManager {
    private var bluetoothSocket: BluetoothSocket? = null

    override fun checkIsBluetoothEnable() = bluetoothAdapter.isEnabled

    override fun enableBluetooth() {
        bluetoothAdapter.enable()
    }

    override suspend fun registerBluetoothServer(isSecureConnection: Boolean) =
        withContext(Dispatchers.IO) {

            val bluetoothSocketType =
                if (isSecureConnection) ConnectionSecurity.SECURE.security else ConnectionSecurity.UNSECURE.security

            val bluetoothServerSocket: BluetoothServerSocket? = if (isSecureConnection) {
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
                if (bluetoothSocket != null) {
                    BluetoothAdapter.getDefaultAdapter().cancelDiscovery()
                    bluetoothSocket!!.connect()
                    bluetoothSocketManager.setBluetoothSocket(bluetoothSocket!!)
                    bluetoothReceivedBluetoothMessageManager.registerMessageReceiver()
                }
            } catch (bluetoothConnectionError: IOException) {
                bluetoothConnectionErrorHandler.onBluetoothConnectionError()
            }
        }
}
