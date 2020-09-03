package eu.agribot.agribotapp.communication.bluetooth.message.receivedmessage

import android.bluetooth.BluetoothSocket
import android.util.Log
import eu.agribot.agribotapp.communication.bluetooth.const.BluetoothAdressCodeModel
import eu.agribot.agribotapp.communication.bluetooth.const.ConstatntConnectionValue
import eu.agribot.agribotapp.communication.bluetooth.error.message.receive.BluetoothMessageReceiveErrorHandler
import eu.agribot.agribotapp.communication.bluetooth.message.frame.BluetoothMessageFrame
import eu.agribot.agribotapp.communication.bluetooth.message.writemessage.WriteBluetoothMessageManager
import eu.agribot.agribotapp.communication.bluetooth.model.DiagnosticDataBD
import java.io.IOException
import java.io.InputStream

class AndroidReceivedBluetoothMessageManager(
    private val receiveErrorHandler: BluetoothMessageReceiveErrorHandler,
    private val writeBluetoothMessageManager: WriteBluetoothMessageManager
) : ReceivedBluetoothMessageManager {

    override fun registerMessageReceiver(bluetoothSocket: BluetoothSocket?) {
        writeBluetoothMessageManager.sendHandshakeMessage(bluetoothSocket)
        val inputStream: InputStream = bluetoothSocket!!.inputStream
        val receiveMessageBuffer = ByteArray(ConstatntConnectionValue.MESSAGE_BUFFER_SIZE)
        while (true) {
            try {
                inputStream.read(receiveMessageBuffer)
                Log.e("xd", "receive" + receiveMessageBuffer)
            } catch (receiveMessageException: IOException) {
                receiveErrorHandler.onReceiveBluetoothMessageError(
                    receiveMessageException
                )
                break
            }
            handleMessage(receiveMessageBuffer)
        }
    }

    private fun handleMessage(
        bluetoothMessage: ByteArray
    ) {
        try {
            if (bluetoothMessage[0] == 67.toByte()) {
                val length = bluetoothMessage[1].toInt()
                if (length <= 0)
                    return
                if (bluetoothMessage.isEmpty())
                    return
                val btFrame =
                    BluetoothMessageFrame(
                        bluetoothMessage
                    )
                handleMessageFrame(btFrame)
            } else {
                return
            }
        } catch (handleMessageException: IOException) {
            receiveErrorHandler.onReceiveBluetoothMessageError(
                handleMessageException
            )
        }
    }

    private fun handleMessageFrame(bluetoothMessageFrame: BluetoothMessageFrame) {
        when (bluetoothMessageFrame.getAddress()) {

            BluetoothAdressCodeModel.AGRIBOT_HANDSHAKE -> {
                 handleHandshakeMessage()
            }

            BluetoothAdressCodeModel.MOBILE_HEARTBEAT -> {
                handleHeartbeatMessage(bluetoothMessageFrame)
            }

            BluetoothAdressCodeModel.AGRIBOT_DIAGNOSTIC_DATA_1 -> {
                handleDiagnosticData(bluetoothMessageFrame)
            }

            BluetoothAdressCodeModel.AGRIBOT_ERROR -> {
            }


        }
    }

    private fun handleDiagnosticData(bluetoothMessageFrame: BluetoothMessageFrame) {
        if (bluetoothMessageFrame.getMessage().size < 8)
            return
        val engineRpm = bytesToUnsingedInt(bluetoothMessageFrame.getMessage().sliceArray(0..1))
        val batteryVoltage =
            bytesToUnsingedInt(bluetoothMessageFrame.getMessage().sliceArray(6..7)) * 0.001
        val batteryInfo: String = if (batteryVoltage.toString().length > 4) {
            batteryVoltage.toString().slice(0..4)
        } else {
            batteryVoltage.toString()
        }

        val fuelLevel: Double = bluetoothMessageFrame.getMessage()[3].toDouble() * 0.1

        DiagnosticDataBD(
            engineRpm,
            bluetoothMessageFrame.getMessage()[2].toInt(),
            bluetoothMessageFrame.getMessage()[3].toDouble(),
            fuelLevel,
            bluetoothMessageFrame.getMessage()[5].toInt(),
            batteryInfo
        )
    }

    private fun bytesToUnsingedInt(array: ByteArray): Int {
        return bytesToUnsingedInt(array, true)
    }

    private fun bytesToUnsingedInt(array: ByteArray, bigEndian: Boolean): Int {
        var result = 0
        if (bigEndian)
            for (i in array.indices)
                result += (array[array.size - 1 - i].toInt() and 0xFF) shl 8 * i
        else
            for (i in array.indices)
                result += (array[i].toInt() and 0xFF) shl 8 * i
        return result
    }

    private fun handleHandshakeMessage() {
        writeBluetoothMessageManager.startSendingHeartbeat()
    }

    private fun handleHeartbeatMessage(bluetoothMessageFrame: BluetoothMessageFrame) {
        if (bluetoothMessageFrame.getMessage()[0].toInt() % 4 == 0) {
            writeBluetoothMessageManager.sendDiagnosticDataRequestMessage()
        }
    }

}