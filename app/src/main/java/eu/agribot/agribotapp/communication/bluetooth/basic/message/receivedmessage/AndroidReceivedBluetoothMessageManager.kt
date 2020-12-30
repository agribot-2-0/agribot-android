package eu.agribot.agribotapp.communication.bluetooth.basic.message.receivedmessage

import android.util.Log
import eu.agribot.agribotapp.communication.bluetooth.basic.connection.socket.BluetoothSocketManager
import eu.agribot.agribotapp.communication.bluetooth.basic.message.frame.BluetoothMessageFrame
import eu.agribot.agribotapp.communication.bluetooth.basic.message.frame.FrameConstant.DUMMY_BYTE
import eu.agribot.agribotapp.communication.bluetooth.basic.message.heartbeat.HeartbeatManager
import eu.agribot.agribotapp.communication.bluetooth.basic.message.receivedmessage.ConstantReceivedMessage.ARRAY_BIT_SHIFT
import eu.agribot.agribotapp.communication.bluetooth.basic.message.receivedmessage.ConstantReceivedMessage.HEARTBEAT_COUNTER_DELAY
import eu.agribot.agribotapp.communication.bluetooth.basic.message.receivedmessage.ConstantReceivedMessage.HEARTBEAT_COUNTER_MAX_VALUE
import eu.agribot.agribotapp.communication.bluetooth.basic.message.receivedmessage.ConstantReceivedMessage.HEARTBEAT_INIT_VALUE
import eu.agribot.agribotapp.communication.bluetooth.basic.message.receivedmessage.ConstantReceivedMessage.LENGHT_BYTE_POSITION
import eu.agribot.agribotapp.communication.bluetooth.basic.message.receivedmessage.ConstantReceivedMessage.START_FRAME_MESSAGE
import eu.agribot.agribotapp.communication.bluetooth.basic.message.receivedmessage.ConstantReceivedMessage.START_OF_FRAME_POSITION
import eu.agribot.agribotapp.communication.bluetooth.basic.message.writemessage.WriteBluetoothMessageManager
import eu.agribot.agribotapp.communication.bluetooth.const.BluetoothAdressCodeModel
import eu.agribot.agribotapp.communication.bluetooth.const.PersistentConnectionParameters
import eu.agribot.agribotapp.communication.bluetooth.error.message.receive.BluetoothMessageReceiveErrorHandler
import eu.agribot.agribotapp.communication.bluetooth.model.fromcommunication.DiagnosticBluetoothDataBD
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.IOException

class AndroidReceivedBluetoothMessageManager(
        private val receiveErrorHandler: BluetoothMessageReceiveErrorHandler,
        private val writeBluetoothMessageManager: WriteBluetoothMessageManager,
        private val bluetoothSocketManager: BluetoothSocketManager,
        private val heartbeatManager: HeartbeatManager
) : ReceivedBluetoothMessageManager {
    private var agribotDiagnosticData: DiagnosticBluetoothDataBD? = null

    override fun registerMessageReceiver() {
        writeBluetoothMessageManager.sendHandshakeMessage()
        val receiveMessageBuffer = ByteArray(PersistentConnectionParameters.MESSAGE_BUFFER_SIZE)
        while (true) {
            try {
                bluetoothSocketManager.getBluetoothSocket()?.inputStream?.read(receiveMessageBuffer)
                handleMessage(receiveMessageBuffer)
            } catch (receiveMessageException: IOException) {
                receiveErrorHandler.onReceiveBluetoothMessageError()
                break
            }
        }
    }

    // DEVELOP FUNCTION
    private fun bytesToHex(data: ByteArray): String {
        val builder = StringBuilder()
        for (b in data) {
            builder.append(String.format("%02x", b))
        }
        return builder.toString()
    }

    override fun getDiagnosticData(): DiagnosticBluetoothDataBD? {
        return agribotDiagnosticData
    }

    private fun handleMessage(
            bluetoothMessage: ByteArray
    ) {
        try {
            if (bluetoothMessage[START_OF_FRAME_POSITION] == START_FRAME_MESSAGE) {
                val length = bluetoothMessage[LENGHT_BYTE_POSITION].toInt()
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
            receiveErrorHandler.onReceiveBluetoothMessageError()
        }
    }


    private fun handleMessageFrame(bluetoothMessageFrame: BluetoothMessageFrame) {
        when (bluetoothMessageFrame.getAddress()) {
            BluetoothAdressCodeModel.AGRIBOT_HANDSHAKE -> {
                handleHandshakeMessage()
            }

            BluetoothAdressCodeModel.MOBILE_HEARTBEAT -> {
                handleHeartbeatMessage()

            }

            BluetoothAdressCodeModel.AGRIBOT_ERROR -> {
                receiveErrorHandler.handleAgribotError()
            }

            BluetoothAdressCodeModel.MANUAL_START -> {
                writeBluetoothMessageManager.sendManualControlStartMessage()
            }

            BluetoothAdressCodeModel.AGRIBOT_DIAGNOSTIC_DATA_1 -> {
                handleDiagnosticData(bluetoothMessageFrame)
            }
        }
    }

    private fun handleDiagnosticData(bluetoothMessageFrame: BluetoothMessageFrame) {
        /*
        if (bluetoothMessageFrame.getMessage().size < EIGHT_BYTE_MESSAGE)
            return
        agribotDiagnosticData = DiagnosticBluetoothDataBD(
            engineRotationsPerMinute = bytesToUnsingedInt(
                bluetoothMessageFrame.getMessage()
                    .sliceArray(ENGINE_ROTATIONS_FIRST_BYTE_POSITION..ENGINE_ROTATIONS_SECOND_BYTE_POSITION)
            ),
            torqueOfEngine = bluetoothMessageFrame.getMessage()[TORQUE_OF_ENGINE_BYTE_POSITION].toInt(),
            engineFuelRate = if ((bluetoothMessageFrame.getMessage()[ENGINE_FUEL_RATE_BYTE_POSITION] / FUEL_LEVEL_RATIO)
                    .toString().length > FOUR_DIGIT_NUMBER
            ) {
                (bluetoothMessageFrame.getMessage()[ENGINE_FUEL_RATE_BYTE_POSITION] / FUEL_LEVEL_RATIO)
                    .toString().slice(FIRST_DIGIT..FOURTH_DIGIT)
            } else {
                (bluetoothMessageFrame.getMessage()[ENGINE_FUEL_RATE_BYTE_POSITION] / FUEL_LEVEL_RATIO)
                    .toString()
            },
            fuelLevel = bluetoothMessageFrame.getMessage()[FUEL_LEVEL_BYTE_POSITION].toInt(),
            fuelTemperature = bluetoothMessageFrame.getMessage()[FUEL_TEMPERATURE_BYTE_POSITION].toInt(),
            batteryVoltage = if ((
                        bytesToUnsingedInt(
                            bluetoothMessageFrame.getMessage()
                                .sliceArray(ACCUMULATOR_VOLTGE_FIRST_BYTE_POSITION..ACCUMULATOR_VOLTAGE_SECOND_BYTE_POSITION)
                        )
                        ).toString().length > FOUR_DIGIT_NUMBER
            ) {
                (
                        (
                                bytesToUnsingedInt(
                                    bluetoothMessageFrame.getMessage()
                                        .sliceArray(ACCUMULATOR_VOLTGE_FIRST_BYTE_POSITION..ACCUMULATOR_VOLTAGE_SECOND_BYTE_POSITION)
                                )
                                ) * BATTERY_VOLTAGE_RATIO + VOLTAGE_DROP
                        ).toString()
                    .slice(FIRST_DIGIT..FOURTH_DIGIT)
            } else {
                (
                        (
                                bytesToUnsingedInt(
                                    bluetoothMessageFrame.getMessage()
                                        .sliceArray(ACCUMULATOR_VOLTGE_FIRST_BYTE_POSITION..ACCUMULATOR_VOLTAGE_SECOND_BYTE_POSITION)
                                )
                                ) * BATTERY_VOLTAGE_RATIO + VOLTAGE_DROP
                        ).toString()
            }
        )

         */
    }

    private fun bytesToUnsingedInt(array: ByteArray): Int {
        return bytesToUnsingedInt(array, true)
    }

    private fun bytesToUnsingedInt(array: ByteArray, bigEndian: Boolean): Int {
        var result = 0
        if (bigEndian)
            for (i in array.indices)
                result += (array[array.size - 1 - i].toInt() and DUMMY_BYTE.toInt()) shl ARRAY_BIT_SHIFT * i
        else
            for (i in array.indices)
                result += (array[i].toInt() and DUMMY_BYTE.toInt()) shl ARRAY_BIT_SHIFT * i
        return result
    }

    private fun handleHandshakeMessage() {
        writeBluetoothMessageManager.stopHandshakeScope()
        heartbeatManager.sendFirstHeartbeat()
    }

    private fun handleHeartbeatMessage() {
        heartbeatManager.restartHeartbeatCounter()
    }

}
