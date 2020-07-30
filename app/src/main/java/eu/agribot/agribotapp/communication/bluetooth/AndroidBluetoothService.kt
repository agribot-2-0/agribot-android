package eu.agribot.agribotapp.communication.bluetooth

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothServerSocket
import android.bluetooth.BluetoothSocket
import android.os.Bundle
import android.os.Handler
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.util.UUID

class AndroidBluetoothService(private val handler: Handler) : BluetoothService {
    val inputStream: InputStream = bluetoothSocket!!.inputStream
    val outputStream: OutputStream = bluetoothSocket!!.outputStream

    companion object {
        const val MESSAGE_READ: Int = 0
        const val MESSAGE_WRITE: Int = 1
        const val MESSAGE_TOAST: Int = 2
        var bluetoothSocket: BluetoothSocket? = null

        private val AGRIBOT_UUID_SECURE =
            UUID.fromString("00001101-0000-1000-8000-00805f9b34fb")
        private val AGRIBOT_UUID_INSECURE =
            UUID.fromString("8ce255c0-200a-11e0-ac64-0800200c9a63")
    }

    override fun checkIsBluetoothEnable(): Boolean = when {
        bluetoothAdapter.isEnabled -> true
        else -> false
    }

    override fun enableBluetooth() {
        bluetoothAdapter.enable()
    }

    override suspend fun registerBluetoothServer(isSecurityConnection: Boolean) =
        withContext(Dispatchers.IO) {
            val bluetoothSocketType = if (isSecurityConnection) "Secure" else "Unsecure"

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
        try {
            inputStream.close()
            outputStream.close()
            bluetoothSocket?.close()
        } catch (exception: IOException) {
            Log.e("xd", "disconnect error" + exception)
        }
        bluetoothSocket = null
    }

    private suspend fun onConnect(deviceAdress: String) =
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
            } catch (e: IOException) {
            }
        }

    override suspend fun receiveBluetoothMessage(handleMessage: (message: ByteArray) -> Unit) =
        withContext(Dispatchers.IO) {

            val buffer = ByteArray(1024)

            run() {
                var numBytes: Int

                while (true) {
                    numBytes = try {
                        inputStream.read(buffer)
                    } catch (e: IOException) {
                        break
                    }

                    val readMsg = handler.obtainMessage(
                        MESSAGE_READ, numBytes, -1,
                        buffer
                    )
                    handleMessage(buffer)
                    readMsg.sendToTarget()
                }
            }
        }

    override fun writeBluetoothMessage(message: ByteArray) {
        val mmBuffer = ByteArray(1024)

        try {
            outputStream.write(message)
        } catch (e: IOException) {
            val writeErrorMsg = handler.obtainMessage(MESSAGE_TOAST)
            val bundle = Bundle().apply {
                putString("toast", "Couldn't send data to the other device")
            }
            writeErrorMsg.data = bundle
            handler.sendMessage(writeErrorMsg)
            return
        }
        val writtenMsg = handler.obtainMessage(
            MESSAGE_WRITE, -1, -1, mmBuffer
        )
        writtenMsg.sendToTarget()
    }

    override fun setConnectionState(isConnected: Boolean) {
        while (bluetoothSocket?.isConnected!!) {
            isConnected
        }
        while (!bluetoothSocket?.isConnected!!) {
            !isConnected
        }
    }
}
