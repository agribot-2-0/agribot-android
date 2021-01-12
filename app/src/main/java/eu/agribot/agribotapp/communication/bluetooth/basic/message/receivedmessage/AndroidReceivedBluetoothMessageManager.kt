package eu.agribot.agribotapp.communication.bluetooth.basic.message.receivedmessage

import eu.agribot.agribotapp.communication.bluetooth.basic.connection.socket.BluetoothSocketManager
import eu.agribot.agribotapp.communication.bluetooth.basic.message.frame.BluetoothMessageFrame
import eu.agribot.agribotapp.communication.bluetooth.basic.message.frame.FrameConstant.DUMMY_BYTE
import eu.agribot.agribotapp.communication.bluetooth.basic.message.heartbeat.HeartbeatManager
import eu.agribot.agribotapp.communication.bluetooth.basic.message.receivedmessage.ConstantReceivedMessage.ARRAY_BIT_SHIFT
import eu.agribot.agribotapp.communication.bluetooth.basic.message.receivedmessage.ConstantReceivedMessage.BATTERY_VOLTAGE_RATIO
import eu.agribot.agribotapp.communication.bluetooth.basic.message.receivedmessage.ConstantReceivedMessage.EIGHT_BYTE_MESSAGE
import eu.agribot.agribotapp.communication.bluetooth.basic.message.receivedmessage.ConstantReceivedMessage.FIRST_DIGIT
import eu.agribot.agribotapp.communication.bluetooth.basic.message.receivedmessage.ConstantReceivedMessage.FOURTH_DIGIT
import eu.agribot.agribotapp.communication.bluetooth.basic.message.receivedmessage.ConstantReceivedMessage.FOUR_DIGIT_NUMBER
import eu.agribot.agribotapp.communication.bluetooth.basic.message.receivedmessage.ConstantReceivedMessage.FUEL_RATE_RATIO
import eu.agribot.agribotapp.communication.bluetooth.basic.message.receivedmessage.ConstantReceivedMessage.LENGHT_BYTE_POSITION
import eu.agribot.agribotapp.communication.bluetooth.basic.message.receivedmessage.ConstantReceivedMessage.START_FRAME_MESSAGE
import eu.agribot.agribotapp.communication.bluetooth.basic.message.receivedmessage.ConstantReceivedMessage.START_OF_FRAME_POSITION
import eu.agribot.agribotapp.communication.bluetooth.basic.message.receivedmessage.DiagnosticDataArrayPosition.ACCUMULATOR_VOLTAGE_SECOND_BYTE_POSITION
import eu.agribot.agribotapp.communication.bluetooth.basic.message.receivedmessage.DiagnosticDataArrayPosition.ACCUMULATOR_VOLTGE_FIRST_BYTE_POSITION
import eu.agribot.agribotapp.communication.bluetooth.basic.message.receivedmessage.DiagnosticDataArrayPosition.ENGINE_FUEL_RATE_BYTE_POSITION
import eu.agribot.agribotapp.communication.bluetooth.basic.message.receivedmessage.DiagnosticDataArrayPosition.ENGINE_ROTATIONS_FIRST_BYTE_POSITION
import eu.agribot.agribotapp.communication.bluetooth.basic.message.receivedmessage.DiagnosticDataArrayPosition.ENGINE_ROTATIONS_SECOND_BYTE_POSITION
import eu.agribot.agribotapp.communication.bluetooth.basic.message.receivedmessage.DiagnosticDataArrayPosition.FUEL_LEVEL_BYTE_POSITION
import eu.agribot.agribotapp.communication.bluetooth.basic.message.receivedmessage.DiagnosticDataArrayPosition.FUEL_TEMPERATURE_BYTE_POSITION
import eu.agribot.agribotapp.communication.bluetooth.basic.message.receivedmessage.DiagnosticDataArrayPosition.TORQUE_OF_ENGINE_BYTE_POSITION
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
    private val heartbeatManager: HeartbeatManager
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
                handleRfidTagMessage(bluetoothMessageFrame)
            }
        }
    }

    @ExperimentalCoroutinesApi
    private fun handleRfidTagMessage(bluetoothMessageFrame: BluetoothMessageFrame) {
        if (bluetoothMessageFrame.getMessage()[2].toInt() != 0) {
            rfidDataBDFlow.value = RfidDataBD(
                antennaNumber = bluetoothMessageFrame.getMessage()[1].toInt(),
                tagIDNumber = bytesToUnsingedInt(
                    bluetoothMessageFrame.getMessage().sliceArray(3..4)
                )
            )
        } else {
            rfidDataBDFlow.value = RfidDataBD(
                antennaNumber = 0,
                tagIDNumber = 0
            )
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
        if (bluetoothMessageFrame.getMessage().size < EIGHT_BYTE_MESSAGE)
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
                bluetoothMessageFrame.getMessage()
                        .sliceArray(ENGINE_ROTATIONS_FIRST_BYTE_POSITION..ENGINE_ROTATIONS_SECOND_BYTE_POSITION))
    }

    private fun getTorqueOfEngine(bluetoothMessageFrame: BluetoothMessageFrame): Int {
        return bluetoothMessageFrame.getMessage()[TORQUE_OF_ENGINE_BYTE_POSITION].toInt()
    }

    private fun getEngineFuelRate(bluetoothMessageFrame: BluetoothMessageFrame): String {
        return (bluetoothMessageFrame.getMessage()[ENGINE_FUEL_RATE_BYTE_POSITION] / FUEL_RATE_RATIO).toString()
    }

    private fun getFuelLevel(bluetoothMessageFrame: BluetoothMessageFrame): Int {
        return bluetoothMessageFrame.getMessage()[FUEL_LEVEL_BYTE_POSITION].toInt()
    }

    private fun getFuelTemperature(bluetoothMessageFrame: BluetoothMessageFrame): Int {
        return bluetoothMessageFrame.getMessage()[FUEL_TEMPERATURE_BYTE_POSITION].toInt()
    }

    private fun getBatteryVoltage(bluetoothMessageFrame: BluetoothMessageFrame): String {
        val batteryVoltage = bytesToUnsingedInt(bluetoothMessageFrame.getMessage().sliceArray(ACCUMULATOR_VOLTGE_FIRST_BYTE_POSITION..ACCUMULATOR_VOLTAGE_SECOND_BYTE_POSITION)) * BATTERY_VOLTAGE_RATIO
        return if (batteryVoltage.toString().length > FOUR_DIGIT_NUMBER)
            (batteryVoltage.toString().slice(FIRST_DIGIT..FOURTH_DIGIT).toDouble()).toString()
        else
            (batteryVoltage).toString()
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
        writeBluetoothMessageManager.sendManualControlInitMessage()
    }

    private fun handleHeartbeatMessage() {
        heartbeatManager.restartHeartbeatCounter()
    }
}
