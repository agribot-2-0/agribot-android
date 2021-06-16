package eu.agribot.agribotapp.communication.bluetooth.basic.message.writemessage

import eu.agribot.agribotapp.communication.bluetooth.basic.connection.socket.BluetoothSocketManager
import eu.agribot.agribotapp.communication.bluetooth.basic.message.frame.BluetoothMessageFrame
import eu.agribot.agribotapp.communication.bluetooth.basic.message.frame.data.FrameConstant.DUMMY_BYTE
import eu.agribot.agribotapp.communication.bluetooth.basic.message.frame.data.FrameConstant.EMPTY_CAN_ARRAY_SIZE
import eu.agribot.agribotapp.communication.bluetooth.const.BluetoothAdressCodeModel
import eu.agribot.agribotapp.communication.bluetooth.error.message.write.BluetoothMessageWriteErrorHandler
import eu.agribot.agribotapp.communication.bluetooth.model.CANmessageData
import eu.agribot.agribotapp.communication.crashlystic.CrashlysticService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.IOException
import java.io.OutputStream

class AndroidWriteBluetoothMessageManager(
    private val bluetoothMessageWriteErrorHandler: BluetoothMessageWriteErrorHandler,
    private val bluetoothSocketManager: BluetoothSocketManager,
    private val crashlysticService: CrashlysticService
) : WriteBluetoothMessageManager {
    private val emptyCanData = ByteArray(EMPTY_CAN_ARRAY_SIZE) { 0 }
    private val singleArray = ByteArray(1) { DUMMY_BYTE }
    private var handshakeScope = CoroutineScope(Job() + Dispatchers.IO)
    private var manualModeScope = CoroutineScope(Job() + Dispatchers.IO)
    private val handshakeDelay = 500L
    private fun writeBluetoothMessage(
        message: ByteArray
    ) {
        if (bluetoothSocketManager.getBluetoothSocket() != null) {
            val outputStream: OutputStream =
                bluetoothSocketManager.getBluetoothSocket()!!.outputStream
            try {
                outputStream.write(message)
            } catch (writeMessageException: IOException) {
                crashlysticService.recordException(
                    CrashlysticService.RegisterExceptionModel(
                        message = "Bluetooth message write exception",
                        exception = writeMessageException
                    )
                )
                bluetoothMessageWriteErrorHandler.onWriteMessageError()
                return
            }
        }
    }

    override fun sendHandshakeMessage() {
        handshakeScope.launch {
            while (true) {
                delay(handshakeDelay)
                writeBluetoothMessage(
                    BluetoothMessageFrame(
                        CANmessageData(
                            BluetoothAdressCodeModel.MOBILE_HANDSHAKE.number, emptyCanData
                        )
                    ).bluetoothMessageArray
                )
            }
        }
    }

    override fun sendManualControlInitMessage() {
        manualModeScope.launch {
            while (true) {
                delay(handshakeDelay)
                writeBluetoothMessage(
                    BluetoothMessageFrame(
                        CANmessageData(
                            BluetoothAdressCodeModel.MANUAL_STARTING_INIT.number,
                            emptyCanData
                        )
                    ).bluetoothMessageArray
                )
            }
        }
    }

    override fun sendManualControlStartMessage() {
        writeBluetoothMessage(
            BluetoothMessageFrame(
                CANmessageData(
                    BluetoothAdressCodeModel.MANUAL_START.number, emptyCanData
                )
            ).bluetoothMessageArray
        )
    }

    override fun sendManualSteeringDataMessage(
        arcArray: ByteArray
    ) {
        writeBluetoothMessage(
            BluetoothMessageFrame(
                CANmessageData(
                    BluetoothAdressCodeModel.RIDE_THE_CURVE.number,
                    arcArray
                )
            ).bluetoothMessageArray
        )
    }

    override fun sendAutonomusModeInitMessage() {
        val autonomusModeInitializationMessage =
            BluetoothMessageFrame(
                CANmessageData(
                    BluetoothAdressCodeModel.AUTO_START.number,
                    emptyCanData
                )
            )
        writeBluetoothMessage(autonomusModeInitializationMessage.bluetoothMessageArray)
    }

    override fun sendStartEngineMessage() {
        writeBluetoothMessage(
            BluetoothMessageFrame(
                CANmessageData(
                    BluetoothAdressCodeModel.START_ENGINE.number,
                    singleArray
                )
            ).bluetoothMessageArray
        )
    }

    override fun sendStopThreadMessage() {
        writeBluetoothMessage(
            BluetoothMessageFrame(
                CANmessageData(
                    BluetoothAdressCodeModel.STOP_SIGNAL.number,
                    singleArray
                )
            ).bluetoothMessageArray
        )
    }

    override fun sendSwitchSettingsMessage(
        switchSettingsArray: ByteArray

    ) {
        writeBluetoothMessage(
            BluetoothMessageFrame(
                CANmessageData(
                    BluetoothAdressCodeModel.AGRIBOT_SET_SWITCH.number,
                    switchSettingsArray
                )
            ).bluetoothMessageArray
        )
    }

    override fun sendDiagnosticDataRequestMessage() {
        val diagnosticDataRequestMessage = BluetoothMessageFrame(
            CANmessageData(
                BluetoothAdressCodeModel.AGRIBOT_DIAGNOSTIC_DATA_1.number, emptyCanData
            )
        )
        writeBluetoothMessage(
            diagnosticDataRequestMessage.bluetoothMessageArray
        )
    }

    override fun sendTTControlForwardMessage(
        moveForwardArray: ByteArray
    ) {
        writeBluetoothMessage(
            BluetoothMessageFrame(
                CANmessageData(
                    BluetoothAdressCodeModel.MOVE_FORWARD_BACKWARD.number,
                    moveForwardArray
                )
            ).bluetoothMessageArray
        )
    }

    override fun sendTTControlBackwardMessage(
        moveBackArray: ByteArray
    ) {
        writeBluetoothMessage(
            BluetoothMessageFrame(
                CANmessageData(
                    BluetoothAdressCodeModel.MOVE_FORWARD_BACKWARD.number,
                    moveBackArray
                )
            ).bluetoothMessageArray
        )
    }

    override fun sendTTControlTurnLeftMessage(
        moveLeftArray: ByteArray
    ) {
        writeBluetoothMessage(
            BluetoothMessageFrame(
                CANmessageData(BluetoothAdressCodeModel.ROTATE_CARRIAGES.number, moveLeftArray)
            ).bluetoothMessageArray
        )
    }

    override fun sendTTControlTurnRightMessage(
        moveRightArray: ByteArray
    ) {
        writeBluetoothMessage(
            BluetoothMessageFrame(
                CANmessageData(BluetoothAdressCodeModel.ROTATE_CARRIAGES.number, moveRightArray)
            ).bluetoothMessageArray
        )
    }

    override fun stopHandshakeScope() {
        handshakeScope.cancel()
        handshakeScope = CoroutineScope(Job() + Dispatchers.IO)
    }

    override fun stopManualInitScope() {
        manualModeScope.cancel()
        manualModeScope = CoroutineScope(Job() + Dispatchers.IO)
    }

    override fun writeHeartbeatMessage(heartbeatArray: ByteArray) {
        writeBluetoothMessage(
            BluetoothMessageFrame(
                CANmessageData(
                    BluetoothAdressCodeModel.MOBILE_HEARTBEAT.number,
                    heartbeatArray
                )
            ).bluetoothMessageArray
        )
    }

    override fun sendRfidScanRequest(rfidArray: ByteArray) {
        writeBluetoothMessage(
            BluetoothMessageFrame(
                CANmessageData(
                    BluetoothAdressCodeModel.RFID_TAG.number,
                    rfidArray
                )
            ).bluetoothMessageArray
        )
    }

    override fun sendSafeModeRequestMessage() {
        writeBluetoothMessage(
            BluetoothMessageFrame(
                CANmessageData(
                    BluetoothAdressCodeModel.SAFE_MODE.number, emptyCanData
                )
            ).bluetoothMessageArray
        )
    }

    override fun sendToolControlMessage(toolControlArray: ByteArray) {
        writeBluetoothMessage(
            BluetoothMessageFrame(
                CANmessageData(
                    BluetoothAdressCodeModel.TOOL_REQUEST.number,
                    toolControlArray
                )
            ).bluetoothMessageArray
        )
    }
}
