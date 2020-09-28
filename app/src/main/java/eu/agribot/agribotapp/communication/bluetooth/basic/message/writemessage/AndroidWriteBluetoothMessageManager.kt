package eu.agribot.agribotapp.communication.bluetooth.basic.connection.message.writemessage

import eu.agribot.agribotapp.communication.bluetooth.basic.message.frame.BluetoothMessageFrame
import eu.agribot.agribotapp.communication.bluetooth.basic.message.frame.FrameConstant.DUMMY_BYTE
import eu.agribot.agribotapp.communication.bluetooth.basic.message.frame.FrameConstant.EMPTY_CAN_ARRAY_SIZE
import eu.agribot.agribotapp.communication.bluetooth.basic.connection.socket.BluetoothSocketManager
import eu.agribot.agribotapp.communication.bluetooth.basic.message.writemessage.WriteBluetoothMessageManager
import eu.agribot.agribotapp.communication.bluetooth.const.BluetoothAdressCodeModel
import eu.agribot.agribotapp.communication.bluetooth.error.message.write.BluetoothMessageWriteErrorHandler
import eu.agribot.agribotapp.communication.bluetooth.model.CANmessageData
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.IOException
import java.io.OutputStream

class AndroidWriteBluetoothMessageManager(
    private val bluetoothMessageWriteErrorHandler: BluetoothMessageWriteErrorHandler,
    private val bluetoothSocketManager: BluetoothSocketManager
) :
    WriteBluetoothMessageManager {
    private val EMPTY_CAN_DATA = ByteArray(EMPTY_CAN_ARRAY_SIZE) { 1 }
    private val singleArray = ByteArray(1) { DUMMY_BYTE }
    var heartbeatScope = MainScope()
    private val HEARTBEAT_DELAY_TIME = 150L
    private var heartbeatArray = ByteArray(EMPTY_CAN_ARRAY_SIZE)
    var heartbeatFrame =
        BluetoothMessageFrame(
            CANmessageData(BluetoothAdressCodeModel.MOBILE_HEARTBEAT, heartbeatArray)
        )

    private fun writeBluetoothMessage(
        message: ByteArray
    ) {
        if (bluetoothSocketManager.getBluetoothSocket() != null) {
            val outputStream: OutputStream =
                bluetoothSocketManager.getBluetoothSocket()!!.outputStream
            try {
                outputStream.write(message)
            } catch (writeMessageException: IOException) {
                bluetoothMessageWriteErrorHandler.onWriteMessageError()
                stopSendingHeartbeat()
                return
            }
        }
    }

    override fun sendHandshakeMessage() {
        val handshakeFrame = BluetoothMessageFrame(
            CANmessageData(
                BluetoothAdressCodeModel.MOBILE_HANDSHAKE, EMPTY_CAN_DATA
            )
        )
        writeBluetoothMessage(
            handshakeFrame.bluetoothMessageArray
        )
    }

    override fun sendManualControlInitMessage() {
        val manualControlInitFrame = BluetoothMessageFrame(
            CANmessageData(
                BluetoothAdressCodeModel.MANUAL_STARTING_INIT,
                EMPTY_CAN_DATA
            )
        )
        writeBluetoothMessage(
            manualControlInitFrame.bluetoothMessageArray
        )
    }

    override fun sendManualControlStartMessage() {
        val manualControlStartMessage =
            BluetoothMessageFrame(
                CANmessageData(
                    BluetoothAdressCodeModel.MANUAL_START, EMPTY_CAN_DATA
                )
            )
        writeBluetoothMessage(
            manualControlStartMessage.bluetoothMessageArray
        )
    }

    override fun sendManualSteeringDataMessage(
        arcArray: ByteArray
    ) {
        val arcParametrsMessage = BluetoothMessageFrame(
            CANmessageData(
                BluetoothAdressCodeModel.RIDE_THE_CURVE,
                arcArray
            )
        )
        writeBluetoothMessage(
            arcParametrsMessage.bluetoothMessageArray
        )
    }

    override fun sendAutonomusModeInitMessage() {
        val autonomusModeInitializationMessage =
            BluetoothMessageFrame(
                CANmessageData(
                    BluetoothAdressCodeModel.AUTO_START,
                    EMPTY_CAN_DATA
                )
            )
        writeBluetoothMessage(autonomusModeInitializationMessage.bluetoothMessageArray)
    }

    override fun sendStartEngineMessage() {
        val engineStartMessage =
            BluetoothMessageFrame(
                CANmessageData(
                    BluetoothAdressCodeModel.START_ENGINE,
                    singleArray
                )
            )
        writeBluetoothMessage(
            engineStartMessage.bluetoothMessageArray
        )
    }

    override fun sendStopThreadMessage() {
        val stopThreadMessage =
            BluetoothMessageFrame(
                CANmessageData(
                    BluetoothAdressCodeModel.STOP_SIGNAL,
                    singleArray
                )
            )
        writeBluetoothMessage(
            stopThreadMessage.bluetoothMessageArray
        )
    }

    override fun sendSwitchSettingsMessage(
        switchSettingsArray: ByteArray

    ) {

        val switchesMessageFrame = BluetoothMessageFrame(
            CANmessageData(
                BluetoothAdressCodeModel.AGRIBOT_SET_SWITCH,
                switchSettingsArray
            )
        )
        writeBluetoothMessage(
            switchesMessageFrame.bluetoothMessageArray
        )
    }

    override fun sendDiagnosticDataRequestMessage() {
        val diagnosticDataRequestMessage = BluetoothMessageFrame(
            CANmessageData(
                BluetoothAdressCodeModel.AGRIBOT_DIAGNOSTIC_DATA_1, EMPTY_CAN_DATA
            )
        )
        writeBluetoothMessage(
            diagnosticDataRequestMessage.bluetoothMessageArray
        )
    }

    override fun sendTTControlForwardMessage(
        moveForwardArray: ByteArray
    ) { val forwardMessageFrame = BluetoothMessageFrame(
            CANmessageData(
                BluetoothAdressCodeModel.MOVE_FORWARD_BACKWARD,
                moveForwardArray
            )
        )
        writeBluetoothMessage(forwardMessageFrame.bluetoothMessageArray)
    }

    override fun sendTTControlBackwardMessage(
        moveBackArray: ByteArray
    ) {
        val backwardMessageFrame = BluetoothMessageFrame(
            CANmessageData(
                BluetoothAdressCodeModel.MOVE_FORWARD_BACKWARD,
                moveBackArray
            )
        )
        writeBluetoothMessage(backwardMessageFrame.bluetoothMessageArray)
    }

    override fun sendTTControlTurnLeftMessage(
        moveLeftArray: ByteArray
    ) {
        val rotateFrame = BluetoothMessageFrame(
            CANmessageData(BluetoothAdressCodeModel.ROTATE_CARRIAGES, moveLeftArray)
        )

        writeBluetoothMessage(rotateFrame.bluetoothMessageArray)
    }

    override fun sendTTControlTurnRightMessage(
        moveRightArray: ByteArray
    ) {
        val rotateFrame = BluetoothMessageFrame(
            CANmessageData(BluetoothAdressCodeModel.ROTATE_CARRIAGES, moveRightArray)
        )

        writeBluetoothMessage(rotateFrame.bluetoothMessageArray)
    }

    override fun startSendingHeartbeat() {
        heartbeatArray[0] = 0.toByte()
        heartbeatScope.launch {
            while (true) {
                heartbeatFrame = BluetoothMessageFrame(
                    CANmessageData(BluetoothAdressCodeModel.MOBILE_HEARTBEAT, heartbeatArray)
                )
                delay(HEARTBEAT_DELAY_TIME)
                writeBluetoothMessage(heartbeatFrame.bluetoothMessageArray)
                if (heartbeatArray[0].toInt() % 4 == 0) {
                    sendDiagnosticDataRequestMessage()
                }
                heartbeatArray[0]++
            }
        }
    }

    override fun stopSendingHeartbeat() {
        heartbeatScope.cancel()
        heartbeatScope = MainScope()
    }
}
