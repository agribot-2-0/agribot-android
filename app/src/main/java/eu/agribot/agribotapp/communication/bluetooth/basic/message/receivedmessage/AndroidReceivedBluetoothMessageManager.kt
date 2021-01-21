package eu.agribot.agribotapp.communication.bluetooth.basic.message.receivedmessage

import eu.agribot.agribotapp.communication.bluetooth.basic.connection.socket.BluetoothSocketManager
import eu.agribot.agribotapp.communication.bluetooth.basic.message.frame.BluetoothMessageFrame
import eu.agribot.agribotapp.communication.bluetooth.basic.message.frame.data.FrameConstant.DUMMY_BYTE
import eu.agribot.agribotapp.communication.bluetooth.basic.message.heartbeat.HeartbeatManager
import eu.agribot.agribotapp.communication.bluetooth.basic.message.receivedmessage.data.ConstantReceivedMessage.ARRAY_BIT_SHIFT
import eu.agribot.agribotapp.communication.bluetooth.basic.message.receivedmessage.data.ConstantReceivedMessage.BATTERY_VOLTAGE_RATIO
import eu.agribot.agribotapp.communication.bluetooth.basic.message.receivedmessage.data.ConstantReceivedMessage.EIGHT_BYTE_MESSAGE
import eu.agribot.agribotapp.communication.bluetooth.basic.message.receivedmessage.data.ConstantReceivedMessage.FIRST_DIGIT
import eu.agribot.agribotapp.communication.bluetooth.basic.message.receivedmessage.data.ConstantReceivedMessage.FOURTH_DIGIT
import eu.agribot.agribotapp.communication.bluetooth.basic.message.receivedmessage.data.ConstantReceivedMessage.FOUR_DIGIT_NUMBER
import eu.agribot.agribotapp.communication.bluetooth.basic.message.receivedmessage.data.ConstantReceivedMessage.FUEL_RATE_RATIO
import eu.agribot.agribotapp.communication.bluetooth.basic.message.receivedmessage.data.ConstantReceivedMessage.LENGHT_BYTE_POSITION
import eu.agribot.agribotapp.communication.bluetooth.basic.message.receivedmessage.data.ConstantReceivedMessage.START_FRAME_MESSAGE
import eu.agribot.agribotapp.communication.bluetooth.basic.message.receivedmessage.data.ConstantReceivedMessage.START_OF_FRAME_POSITION
import eu.agribot.agribotapp.communication.bluetooth.basic.message.receivedmessage.data.DiagnosticDataArrayPosition.ACCUMULATOR_VOLTAGE_SECOND_BYTE_POSITION
import eu.agribot.agribotapp.communication.bluetooth.basic.message.receivedmessage.data.DiagnosticDataArrayPosition.ACCUMULATOR_VOLTGE_FIRST_BYTE_POSITION
import eu.agribot.agribotapp.communication.bluetooth.basic.message.receivedmessage.data.DiagnosticDataArrayPosition.ENGINE_FUEL_RATE_BYTE_POSITION
import eu.agribot.agribotapp.communication.bluetooth.basic.message.receivedmessage.data.DiagnosticDataArrayPosition.ENGINE_ROTATIONS_FIRST_BYTE_POSITION
import eu.agribot.agribotapp.communication.bluetooth.basic.message.receivedmessage.data.DiagnosticDataArrayPosition.ENGINE_ROTATIONS_SECOND_BYTE_POSITION
import eu.agribot.agribotapp.communication.bluetooth.basic.message.receivedmessage.data.DiagnosticDataArrayPosition.FUEL_LEVEL_BYTE_POSITION
import eu.agribot.agribotapp.communication.bluetooth.basic.message.receivedmessage.data.DiagnosticDataArrayPosition.FUEL_TEMPERATURE_BYTE_POSITION
import eu.agribot.agribotapp.communication.bluetooth.basic.message.receivedmessage.data.DiagnosticDataArrayPosition.TORQUE_OF_ENGINE_BYTE_POSITION
import eu.agribot.agribotapp.communication.bluetooth.basic.message.receivedmessage.rfid.RfidMessageManager
import eu.agribot.agribotapp.communication.bluetooth.basic.message.writemessage.WriteBluetoothMessageManager
import eu.agribot.agribotapp.communication.bluetooth.const.BluetoothAdressCodeModel
import eu.agribot.agribotapp.communication.bluetooth.const.PersistentConnectionParameters
import eu.agribot.agribotapp.communication.bluetooth.error.message.receive.BluetoothMessageReceiveErrorHandler
import eu.agribot.agribotapp.communication.bluetooth.model.fromcommunication.DiagnosticBluetoothDataBD
import eu.agribot.agribotapp.communication.bluetooth.model.fromcommunication.RfidDataBD
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import java.io.IOException

class AndroidReceivedBluetoothMessageManager(
    private val receiveErrorHandler: BluetoothMessageReceiveErrorHandler,
    private val writeBluetoothMessageManager: WriteBluetoothMessageManager,
    private val bluetoothSocketManager: BluetoothSocketManager,
    private val heartbeatManager: HeartbeatManager,
    private val rfidMessageManager: RfidMessageManager
) : ReceivedBluetoothMessageManager {
    @ExperimentalCoroutinesApi
    private val agribotDiagnosticDataFlow = MutableStateFlow<DiagnosticBluetoothDataBD?>(null)

    @ExperimentalCoroutinesApi
    private val rfidDataBDFlow = MutableStateFlow<RfidDataBD?>(null)

    @ExperimentalCoroutinesApi
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

    @ExperimentalCoroutinesApi
    override fun observeDiagnosticData(): MutableStateFlow<DiagnosticBluetoothDataBD?> {
        return agribotDiagnosticDataFlow
    }

    @ExperimentalCoroutinesApi
    private fun handleMessage(
        bluetoothMessage: ByteArray
    ) {
        try {
            if (bluetoothMessage[START_OF_FRAME_POSITION] == START_FRAME_MESSAGE) {
                val bluetoothMessageLength = bluetoothMessage[LENGHT_BYTE_POSITION].toInt()
                if (bluetoothMessageLength <= 0)
                    return
                if (bluetoothMessage.isEmpty())
                    return
                handleMessageFrame(
                    BluetoothMessageFrame(
                        bluetoothMessage
                    )
                )
            } else {
                return
            }
        } catch (handleMessageException: IOException) {
            receiveErrorHandler.onReceiveBluetoothMessageError()
        }
    }

    @ExperimentalCoroutinesApi
    private fun handleMessageFrame(bluetoothMessageFrame: BluetoothMessageFrame) {
        when (bluetoothMessageFrame.getAddress()) {
            BluetoothAdressCodeModel.AGRIBOT_HANDSHAKE.number -> {
                handleHandshakeMessage()
            }

            BluetoothAdressCodeModel.MOBILE_HEARTBEAT.number -> {
                handleHeartbeatMessage()
            }

            BluetoothAdressCodeModel.AGRIBOT_ERROR.number -> {
                receiveErrorHandler.handleAgribotError()
            }

            BluetoothAdressCodeModel.MANUAL_START.number -> {
                writeBluetoothMessageManager.sendManualControlStartMessage()
            }

            BluetoothAdressCodeModel.AGRIBOT_DIAGNOSTIC_DATA_1.number -> {
                handleDiagnosticData(bluetoothMessageFrame)
            }

            BluetoothAdressCodeModel.RFID_TAG.number -> {
                rfidMessageManager.handleRfidMessage(bluetoothMessageFrame.getCanDataFromReceivedMessage())
            }
        }
    }

    @ExperimentalCoroutinesApi
    override fun observe(): MutableStateFlow<RfidDataBD?> {
        return rfidDataBDFlow
    }

    @ExperimentalCoroutinesApi
    override fun clearRfidDataFlow() {
        rfidDataBDFlow.value = null
    }

    @ExperimentalCoroutinesApi
    private fun handleDiagnosticData(bluetoothMessageFrame: BluetoothMessageFrame) {
        if (bluetoothMessageFrame.getCanDataFromReceivedMessage().size < EIGHT_BYTE_MESSAGE)
            return
        bluetoothMessageFrame.let { diagnosticData ->
            agribotDiagnosticDataFlow.value = DiagnosticBluetoothDataBD(
                engineFuelRate = getEngineFuelRate(diagnosticData),
                batteryVoltage = getBatteryVoltage(diagnosticData),
                engineRotationsPerMinute = getEngineRotations(diagnosticData),
                torqueOfEngine = getTorqueOfEngine(diagnosticData),
                fuelLevel = getFuelLevel(diagnosticData),
                fuelTemperature = getFuelTemperature(diagnosticData)
            )
        }
    }

    private fun getEngineRotations(bluetoothMessageFrame: BluetoothMessageFrame): Int {
        return bytesToUnsingedInt(
            bluetoothMessageFrame.getCanDataFromReceivedMessage()
                .sliceArray(ENGINE_ROTATIONS_FIRST_BYTE_POSITION..ENGINE_ROTATIONS_SECOND_BYTE_POSITION)
        )
    }

    private fun getTorqueOfEngine(bluetoothMessageFrame: BluetoothMessageFrame): Int {
        return bluetoothMessageFrame.getCanDataFromReceivedMessage()[TORQUE_OF_ENGINE_BYTE_POSITION].toInt()
    }

    private fun getEngineFuelRate(bluetoothMessageFrame: BluetoothMessageFrame): String {
        return (bluetoothMessageFrame.getCanDataFromReceivedMessage()[ENGINE_FUEL_RATE_BYTE_POSITION] / FUEL_RATE_RATIO).toString()
    }

    private fun getFuelLevel(bluetoothMessageFrame: BluetoothMessageFrame): Int {
        return bluetoothMessageFrame.getCanDataFromReceivedMessage()[FUEL_LEVEL_BYTE_POSITION].toInt()
    }

    private fun getFuelTemperature(bluetoothMessageFrame: BluetoothMessageFrame): Int {
        return bluetoothMessageFrame.getCanDataFromReceivedMessage()[FUEL_TEMPERATURE_BYTE_POSITION].toInt()
    }

    private fun getBatteryVoltage(bluetoothMessageFrame: BluetoothMessageFrame): String {
        val batteryVoltage = bytesToUnsingedInt(
            bluetoothMessageFrame.getCanDataFromReceivedMessage()
                .sliceArray(ACCUMULATOR_VOLTGE_FIRST_BYTE_POSITION..ACCUMULATOR_VOLTAGE_SECOND_BYTE_POSITION)
        ) * BATTERY_VOLTAGE_RATIO
        return if (batteryVoltage.toString().length > FOUR_DIGIT_NUMBER)
            (batteryVoltage.toString().slice(FIRST_DIGIT..FOURTH_DIGIT).toDouble()).toString()
        else
            (batteryVoltage).toString()
    }

    private fun bytesToUnsingedInt(array: ByteArray): Int {
        var result = 0
        for (i in array.indices)
            result += (array[array.size - 1 - i].toInt() and DUMMY_BYTE.toInt()) shl ARRAY_BIT_SHIFT * i
        return result
    }

    private fun handleHandshakeMessage() {
        writeBluetoothMessageManager.stopHandshakeScope()
        heartbeatManager.sendFirstHeartbeat()
        writeBluetoothMessageManager.sendManualControlInitMessage()
    }

    private fun handleHeartbeatMessage() {
        heartbeatManager.restartHeartbeatCounter()
    }
}
