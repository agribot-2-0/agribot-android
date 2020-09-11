package eu.agribot.agribotapp.communication.bluetooth.message.receivedmessage

import android.bluetooth.BluetoothSocket
import eu.agribot.agribotapp.communication.bluetooth.const.BluetoothAdressCodeModel
import eu.agribot.agribotapp.communication.bluetooth.const.ConstatntConnectionValue
import eu.agribot.agribotapp.communication.bluetooth.error.message.receive.BluetoothMessageReceiveErrorHandler
import eu.agribot.agribotapp.communication.bluetooth.message.frame.BluetoothMessageFrame
import eu.agribot.agribotapp.communication.bluetooth.message.frame.FrameConstant.DUMMY_BYTE
import eu.agribot.agribotapp.communication.bluetooth.message.receivedmessage.ConstantReceivedMessage.BATTERY_VOLTAGE_RATIO
import eu.agribot.agribotapp.communication.bluetooth.message.receivedmessage.ConstantReceivedMessage.EIGHT_BYTE_MESSAGE
import eu.agribot.agribotapp.communication.bluetooth.message.receivedmessage.ConstantReceivedMessage.FIRST_DIGIT
import eu.agribot.agribotapp.communication.bluetooth.message.receivedmessage.ConstantReceivedMessage.FOURTH_DIGIT
import eu.agribot.agribotapp.communication.bluetooth.message.receivedmessage.ConstantReceivedMessage.FOUR_DIGIT_NUMBER
import eu.agribot.agribotapp.communication.bluetooth.message.receivedmessage.ConstantReceivedMessage.FUEL_LEVEL_RATIO
import eu.agribot.agribotapp.communication.bluetooth.message.receivedmessage.ConstantReceivedMessage.LENGHT_BYTE_POSITION
import eu.agribot.agribotapp.communication.bluetooth.message.receivedmessage.ConstantReceivedMessage.START_FRAME_MESSAGE
import eu.agribot.agribotapp.communication.bluetooth.message.receivedmessage.ConstantReceivedMessage.START_OF_FRAME_POSITION
import eu.agribot.agribotapp.communication.bluetooth.message.receivedmessage.ConstantReceivedMessage.VOLTAGE_DROP
import eu.agribot.agribotapp.communication.bluetooth.message.receivedmessage.DiagnosticDataArrayPosition.ACCUMULATOR_VOLTAGE_SECOND_BYTE_POSITION
import eu.agribot.agribotapp.communication.bluetooth.message.receivedmessage.DiagnosticDataArrayPosition.ACCUMULATOR_VOLTGE_FIRST_BYTE_POSITION
import eu.agribot.agribotapp.communication.bluetooth.message.receivedmessage.DiagnosticDataArrayPosition.ENGINE_FUEL_RATE_BYTE_POSITION
import eu.agribot.agribotapp.communication.bluetooth.message.receivedmessage.DiagnosticDataArrayPosition.ENGINE_ROTATIONS_FIRST_BYTE_POSITION
import eu.agribot.agribotapp.communication.bluetooth.message.receivedmessage.DiagnosticDataArrayPosition.ENGINE_ROTATIONS_SECOND_BYTE_POSITION
import eu.agribot.agribotapp.communication.bluetooth.message.receivedmessage.DiagnosticDataArrayPosition.FUEL_LEVEL_BYTE_POSITION
import eu.agribot.agribotapp.communication.bluetooth.message.receivedmessage.DiagnosticDataArrayPosition.FUEL_TEMPERATURE_BYTE_POSITION
import eu.agribot.agribotapp.communication.bluetooth.message.receivedmessage.DiagnosticDataArrayPosition.TORQUE_OF_ENGINE_BYTE_POSITION
import eu.agribot.agribotapp.communication.bluetooth.message.writemessage.WriteBluetoothMessageManager
import eu.agribot.agribotapp.communication.bluetooth.model.fromcommunication.DiagnosticCommunicationDataBD
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.IOException
import java.io.InputStream

class AndroidReceivedBluetoothMessageManager(
    private val receiveErrorHandler: BluetoothMessageReceiveErrorHandler,
    private val writeBluetoothMessageManager: WriteBluetoothMessageManager
) : ReceivedBluetoothMessageManager {
    var counterScope = MainScope()
    var bluetoothSocket: BluetoothSocket? = null
    private val HEARTBEAT_INIT_VALUE = 0
    private val HEARTBEAT_COUNTER_MAX_VALUE = 20
    private val HEARTBEAT_COUNTER_DELAY = 30L
    var agribotDiagnosticData: DiagnosticCommunicationDataBD? = null

    override fun registerMessageReceiver(bluetoothSocket: BluetoothSocket?) {
        this.bluetoothSocket = bluetoothSocket
        writeBluetoothMessageManager.sendHandshakeMessage(this.bluetoothSocket)
        val inputStream: InputStream = bluetoothSocket!!.inputStream
        val receiveMessageBuffer = ByteArray(ConstatntConnectionValue.MESSAGE_BUFFER_SIZE)
        while (true) {
            try {
                inputStream.read(receiveMessageBuffer)
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

        val accumulatorVoltage =
            (bytesToUnsingedInt(
                bluetoothMessageFrame.getMessage()
                    .sliceArray(ACCUMULATOR_VOLTGE_FIRST_BYTE_POSITION..ACCUMULATOR_VOLTAGE_SECOND_BYTE_POSITION)
            ))

        val batteryVoltageInfo: String
        batteryVoltageInfo = if (accumulatorVoltage.toString().length > FOUR_DIGIT_NUMBER) {
            (accumulatorVoltage * BATTERY_VOLTAGE_RATIO + VOLTAGE_DROP).toString()
                .slice(FIRST_DIGIT..FOURTH_DIGIT)
        } else {
            (accumulatorVoltage * BATTERY_VOLTAGE_RATIO + VOLTAGE_DROP).toString()
        }

        val engineRotations =
            bytesToUnsingedInt(
                bluetoothMessageFrame.getMessage()
                    .sliceArray(ENGINE_ROTATIONS_FIRST_BYTE_POSITION..ENGINE_ROTATIONS_SECOND_BYTE_POSITION)
            )
        val torqueOfEngine = bluetoothMessageFrame.getMessage()[TORQUE_OF_ENGINE_BYTE_POSITION]

        val engineFuelRate =
            bluetoothMessageFrame.getMessage()[ENGINE_FUEL_RATE_BYTE_POSITION] / FUEL_LEVEL_RATIO

        val engineFuelRateInfo: String
        engineFuelRateInfo = if (engineFuelRate.toString().length > FOUR_DIGIT_NUMBER) {
            engineFuelRate.toString().slice(FIRST_DIGIT..FOURTH_DIGIT)
        } else {
            engineFuelRate.toString()
        }

        val fuelLevel = bluetoothMessageFrame.getMessage()[FUEL_LEVEL_BYTE_POSITION]
        val fuelTemperature = bluetoothMessageFrame.getMessage()[FUEL_TEMPERATURE_BYTE_POSITION]

        agribotDiagnosticData = DiagnosticCommunicationDataBD(
            engineRotationsPerMinute = engineRotations,
            torqueOfEngine = torqueOfEngine.toInt(),
            engineFuelRate = engineFuelRateInfo,
            fuelLevel = fuelLevel.toInt(),
            fuelTemperature = fuelTemperature.toInt(),
            batteryVoltage = batteryVoltageInfo
        )
    }

    private fun bytesToUnsingedInt(array: ByteArray): Int {
        return bytesToUnsingedInt(array, true)
    }

    private fun bytesToUnsingedInt(array: ByteArray, bigEndian: Boolean): Int {
        var result = 0
        if (bigEndian)
            for (i in array.indices)
                result += (array[array.size - 1 - i].toInt() and DUMMY_BYTE.toInt()) shl 8 * i
        else
            for (i in array.indices)
                result += (array[i].toInt() and DUMMY_BYTE.toInt()) shl 8 * i
        return result
    }

    private fun handleHandshakeMessage() {
        writeBluetoothMessageManager.startSendingHeartbeat()
    }

    private fun handleHeartbeatMessage(bluetoothMessageFrame: BluetoothMessageFrame) {
        restartHeartbearCounter()
        if (bluetoothMessageFrame.getMessage()[0].toInt() % 4 == 0) {
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
            writeBluetoothMessageManager.sendHandshakeMessage(bluetoothSocket)
        }
    }
}
