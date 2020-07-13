package eu.agribot.agribotapp.communication.bluetooth

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
import java.util.*


class AndroidBluetoothService(private val handler: Handler) : BluetoothService {
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
                        Log.e("xd", "Socket's accept() method failed", e)
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
        bluetoothDeviceAddress: String,
        onError: () -> Unit
    ) {
        onConnect(bluetoothAdapter.getRemoteDevice(bluetoothDeviceAddress), onError)
    }

    private suspend fun onConnect(device: BluetoothDevice, onError: () -> Unit) =
        withContext(Dispatchers.IO) {

            val bluetoothSocket: BluetoothSocket? by lazy(LazyThreadSafetyMode.NONE) {
                device.createRfcommSocketToServiceRecord(AGRIBOT_UUID_SECURE)
            }

            run() {
                try {
                    bluetoothAdapter.cancelDiscovery()

                    bluetoothSocket?.use { socket ->
                        socket.connect()
                    }
                } catch (exception: IOException) {
                    onError()
                }

            }
        }


    override suspend fun receiveBluetoothMessage(handleMessage: (message: String) -> Unit) =
        withContext(Dispatchers.IO) {
            val inputStream: InputStream = bluetoothSocket!!.inputStream
            val buffer: ByteArray = ByteArray(1024)

            run() {
                var numBytes: Int

                while (true) {
                    numBytes = try {
                        inputStream.read(buffer)
                        Log.e("xd", "read buffer" + buffer)
                    } catch (e: IOException) {
                        Log.d("xd", "Input stream was disconnected", e)
                        break
                    }

                    val readMsg = handler.obtainMessage(
                        MESSAGE_READ, numBytes, -1,
                        buffer
                    )
                    handleMessage(readMsg.toString())
                    readMsg.sendToTarget()
                }
            }
        }

    override fun writeBluetoothMessage(message: ByteArray) {
        val outputStream: OutputStream = bluetoothSocket!!.outputStream
        val buffer: ByteArray = ByteArray(1024) // mmBuffer store for the stream

        try {
            outputStream.write(message)
        } catch (e: IOException) {
            Log.e("xd", "Error occurred when sending data", e)

            // Send a failure message back to the activity.
            val writeErrorMsg = handler.obtainMessage(MESSAGE_TOAST)
            val bundle = Bundle().apply {
                putString("toast", "Couldn't send data to the other device")
            }
            writeErrorMsg.data = bundle
            handler.sendMessage(writeErrorMsg)
            return
        }

        // Share the sent message with the UI activity.
        val writtenMsg = handler.obtainMessage(
            MESSAGE_WRITE, -1, -1, buffer
        )
        writtenMsg.sendToTarget()
    }


}






