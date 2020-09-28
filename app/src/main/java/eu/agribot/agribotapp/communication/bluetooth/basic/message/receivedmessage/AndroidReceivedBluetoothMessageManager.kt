package eu.agribot.agribotapp.communication.bluetooth.basic.message.receivedmessage

import eu.agribot.agribotapp.communication.bluetooth.const.BluetoothAdressCodeModel
import eu.agribot.agribotapp.communication.bluetooth.const.PersistentConnectionParameters
import eu.agribot.agribotapp.communication.bluetooth.error.message.receive.BluetoothMessageReceiveErrorHandler
import eu.agribot.agribotapp.communication.bluetooth.basic.message.frame.BluetoothMessageFrame
import eu.agribot.agribotapp.communication.bluetooth.basic.message.frame.FrameConstant.DUMMY_BYTE
import eu.agribot.agribotapp.communication.bluetooth.basic.message.receivedmessage.ConstantReceivedMessage.ARRAY_BIT_SHIFT
import eu.agribot.agribotapp.communication.bluetooth.basic.message.receivedmessage.ConstantReceivedMessage.BATTERY_VOLTAGE_RATIO
import eu.agribot.agribotapp.communication.bluetooth.basic.message.receivedmessage.ConstantReceivedMessage.EIGHT_BYTE_MESSAGE
import eu.agribot.agribotapp.communication.bluetooth.basic.message.receivedmessage.ConstantReceivedMessage.FIRST_DIGIT
import eu.agribot.agribotapp.communication.bluetooth.basic.message.receivedmessage.ConstantReceivedMessage.FOURTH_DIGIT
import eu.agribot.agribotapp.communication.bluetooth.basic.message.receivedmessage.ConstantReceivedMessage.FOUR_DIGIT_NUMBER
import eu.agribot.agribotapp.communication.bluetooth.basic.message.receivedmessage.ConstantReceivedMessage.FUEL_LEVEL_RATIO
import eu.agribot.agribotapp.communication.bluetooth.basic.message.receivedmessage.ConstantReceivedMessage.HEARTBEAT_COUNTER_DELAY
import eu.agribot.agribotapp.communication.bluetooth.basic.message.receivedmessage.ConstantReceivedMessage.HEARTBEAT_COUNTER_MAX_VALUE
import eu.agribot.agribotapp.communication.bluetooth.basic.message.receivedmessage.ConstantReceivedMessage.HEARTBEAT_DIAGNOSTIC_DATA
import eu.agribot.agribotapp.communication.bluetooth.basic.message.receivedmessage.ConstantReceivedMessage.HEARTBEAT_INIT_VALUE
import eu.agribot.agribotapp.communication.bluetooth.basic.message.receivedmessage.ConstantReceivedMessage.LENGHT_BYTE_POSITION
import eu.agribot.agribotapp.communication.bluetooth.basic.message.receivedmessage.ConstantReceivedMessage.START_FRAME_MESSAGE
import eu.agribot.agribotapp.communication.bluetooth.basic.message.receivedmessage.ConstantReceivedMessage.START_OF_FRAME_POSITION
import eu.agribot.agribotapp.communication.bluetooth.basic.message.receivedmessage.ConstantReceivedMessage.VOLTAGE_DROP
import eu.agribot.agribotapp.communication.bluetooth.basic.message.receivedmessage.DiagnosticDataArrayPosition.ACCUMULATOR_VOLTAGE_SECOND_BYTE_POSITION
import eu.agribot.agribotapp.communication.bluetooth.basic.message.receivedmessage.DiagnosticDataArrayPosition.ACCUMULATOR_VOLTGE_FIRST_BYTE_POSITION
import eu.agribot.agribotapp.communication.bluetooth.basic.message.receivedmessage.DiagnosticDataArrayPosition.ENGINE_FUEL_RATE_BYTE_POSITION
import eu.agribot.agribotapp.communication.bluetooth.basic.message.receivedmessage.DiagnosticDataArrayPosition.ENGINE_ROTATIONS_FIRST_BYTE_POSITION
import eu.agribot.agribotapp.communication.bluetooth.basic.message.receivedmessage.DiagnosticDataArrayPosition.ENGINE_ROTATIONS_SECOND_BYTE_POSITION
import eu.agribot.agribotapp.communication.bluetooth.basic.message.receivedmessage.DiagnosticDataArrayPosition.FUEL_LEVEL_BYTE_POSITION
import eu.agribot.agribotapp.communication.bluetooth.basic.message.receivedmessage.DiagnosticDataArrayPosition.FUEL_TEMPERATURE_BYTE_POSITION
import eu.agribot.agribotapp.communication.bluetooth.basic.message.receivedmessage.DiagnosticDataArrayPosition.TORQUE_OF_ENGINE_BYTE_POSITION
import eu.agribot.agribotapp.communication.bluetooth.basic.message.writemessage.WriteBluetoothMessageManager
import eu.agribot.agribotapp.communication.bluetooth.model.fromcommunication.DiagnosticCommunicationDataBD
import eu.agribot.agribotapp.communication.bluetooth.basic.connection.socket.BluetoothSocketManager
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.IOException

class AndroidReceivedBluetoothMessageManager(
    private val receiveErrorHandler: BluetoothMessageReceiveErrorHandler,
    private val writeBluetoothMessageManager: WriteBluetoothMessageManager,
    private val bluetoothSocketManager: BluetoothSocketManager
) : ReceivedBluetoothMessageManager {
    private var counterScope = MainScope()
    private var agribotDiagnosticData: DiagnosticCommunicationDataBD? = null

    override fun registerMessageReceiver() {
        writeBluetoothMessageManager.sendHandshakeMessage()
        val receiveMessageBuffer = ByteArray(PersistentConnectionParameters.MESSAGE_BUFFER_SIZE)
        while (true) {
            try {
                bluetoothSocketManager.getBluetoothSocket()?.inputStream?.read(receiveMessageBuffer)
            } catch (receiveMessageException: IOException) {
                receiveErrorHandler.onReceiveBluetoothMessageError()
                break
            }
            handleMessage(receiveMessageBuffer)
        }
    }

    override fun getDiagnosticData(): DiagnosticCommunicationDataBD? {
        return agribotDiagnosticData
    }

    private fun handleMessage(
        bluetoothMessage: ByteArray
    ) {
        try {
            if (bluetoothMessage[START_OF_FRAME_POSITION] == START_FRAME_MESSAGE.toByte()) {
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
                handleHeartbeatMessage(bluetoothMessageFrame)
            }

            BluetoothAdressCodeModel.AGRIBOT_ERROR -> {
                receiveErrorHandler.handleAgribotError()
            }

            BluetoothAdressCodeModel.AGRIBOT_DIAGNOSTIC_DATA_1 -> {
                handleDiagnosticData(bluetoothMessageFrame)
            }
        }
    }

    private fun handleDiagnosticData(bluetoothMessageFrame: BluetoothMessageFrame) {
        if (bluetoothMessageFrame.getMessage().size < EIGHT_BYTE_MESSAGE)
            return
        agribotDiagnosticData = DiagnosticCommunicationDataBD(
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
            batteryVoltage = if ((bytesToUnsingedInt(
                    bluetoothMessageFrame.getMessage()
                        .sliceArray(ACCUMULATOR_VOLTGE_FIRST_BYTE_POSITION..ACCUMULATOR_VOLTAGE_SECOND_BYTE_POSITION)
                )).toString().length > FOUR_DIGIT_NUMBER
            ) {
                ((bytesToUnsingedInt(
                    bluetoothMessageFrame.getMessage()
                        .sliceArray(ACCUMULATOR_VOLTGE_FIRST_BYTE_POSITION..ACCUMULATOR_VOLTAGE_SECOND_BYTE_POSITION)
                )) * BATTERY_VOLTAGE_RATIO + VOLTAGE_DROP).toString()
                    .slice(FIRST_DIGIT..FOURTH_DIGIT)
            } else {
                ((bytesToUnsingedInt(
                    bluetoothMessageFrame.getMessage()
                        .sliceArray(ACCUMULATOR_VOLTGE_FIRST_BYTE_POSITION..ACCUMULATOR_VOLTAGE_SECOND_BYTE_POSITION)
                )) * BATTERY_VOLTAGE_RATIO + VOLTAGE_DROP).toString()
            }
        )
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
        writeBluetoothMessageManager.startSendingHeartbeat()
    }

    private fun handleHeartbeatMessage(bluetoothMessageFrame: BluetoothMessageFrame) {
        restartHeartbearCounter()
        if (bluetoothMessageFrame.getMessage()[0].toInt() % HEARTBEAT_DIAGNOSTIC_DATA == 0) {
            writeBluetoothMessageManager.sendDiagnosticDataRequestMessage()
        }
    }

    private fun restartHeartbearCounter() {
        counterScope.cancel()
        counterScope = MainScope()
        startHeartbeatCounter()
    }

    private fun startHeartbeatCounter() {
        counterScope.launch {
            var heartbeatCounter = HEARTBEAT_INIT_VALUE
            while (heartbeatCounter < HEARTBEAT_COUNTER_MAX_VALUE) {
                delay(HEARTBEAT_COUNTER_DELAY)
                heartbeatCounter++
            }
            agribotDiagnosticData = null
            writeBluetoothMessageManager.sendHandshakeMessage()
        }
    }
}
