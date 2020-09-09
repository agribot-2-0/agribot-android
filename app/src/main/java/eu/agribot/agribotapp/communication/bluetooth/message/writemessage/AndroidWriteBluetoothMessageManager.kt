package eu.agribot.agribotapp.communication.bluetooth.message.writemessage

import android.bluetooth.BluetoothSocket
import eu.agribot.agribotapp.communication.bluetooth.const.BluetoothAdressCodeModel
import eu.agribot.agribotapp.communication.bluetooth.message.frame.FrameConstant.DUMMY_BYTE
import eu.agribot.agribotapp.communication.bluetooth.error.message.write.BluetoothMessageWriteErrorHandler
import eu.agribot.agribotapp.communication.bluetooth.message.frame.BluetoothMessageFrame
import eu.agribot.agribotapp.communication.bluetooth.message.frame.FrameConstant.ARC_MESSAGE_ARRAY_SIZE
import eu.agribot.agribotapp.communication.bluetooth.message.frame.FrameConstant.EMPTY_CAN_ARRAY_SIZE
import eu.agribot.agribotapp.communication.bluetooth.message.frame.FrameConstant.TTCONTROL_MOVE_BACK_FRONT_ARRAY_SIZE
import eu.agribot.agribotapp.communication.bluetooth.message.frame.FrameConstant.TTCONTROL_MOVE_LEFT_RIGHT_ARRAY_SIZE
import eu.agribot.agribotapp.communication.bluetooth.message.frame.karts.AgribotKartsRotate
import eu.agribot.agribotapp.communication.bluetooth.message.frame.karts.AgribotToolActivationStatus
import eu.agribot.agribotapp.communication.bluetooth.model.CANmessageData
import eu.agribot.agribotapp.communication.bluetooth.model.tocommunication.ArcParametrsBD
import eu.agribot.agribotapp.communication.bluetooth.model.tocommunication.SwitchSettingsBD
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.IOException
import java.io.OutputStream
import kotlin.math.abs

class AndroidWriteBluetoothMessageManager(
    private val bluetoothMessageWriteErrorHandler: BluetoothMessageWriteErrorHandler
) :
    WriteBluetoothMessageManager {
    private var bluetoothWriteSocket: BluetoothSocket? = null
    private val EMPTY_CAN_DATA = ByteArray(EMPTY_CAN_ARRAY_SIZE)
    private val singleArray = ByteArray(1) { DUMMY_BYTE }
    private var ttControlForwardBackwardArray = ByteArray(TTCONTROL_MOVE_BACK_FRONT_ARRAY_SIZE)
    private var ttControlLeftRightArray = ByteArray(TTCONTROL_MOVE_LEFT_RIGHT_ARRAY_SIZE)
    private var switchesArray = ByteArray(8)
    var heartbeatScope = MainScope()
    private val HEARTBEAT_DELAY_TIME = 50L
    private var heartbeatArray = ByteArray(EMPTY_CAN_ARRAY_SIZE)
    var heartbeatFrame =
        BluetoothMessageFrame(
            CANmessageData(BluetoothAdressCodeModel.MOBILE_HEARTBEAT, heartbeatArray)
        )

    private fun writeBluetoothMessage(
        message: ByteArray
    ) {
        if (bluetoothWriteSocket != null) {
            val outputStream: OutputStream = bluetoothWriteSocket!!.outputStream
            try {
                outputStream.write(message)
            } catch (writeMessageException: IOException) {
                bluetoothMessageWriteErrorHandler.onWriteMessageError()
                return
            }
        }
    }

    override fun sendHandshakeMessage(bluetoothSocket: BluetoothSocket?) {
        bluetoothWriteSocket = bluetoothSocket
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
        arcParametrs: ArcParametrsBD
    ) {
        val moveArcArray = ByteArray(ARC_MESSAGE_ARRAY_SIZE)
        moveArcArray[0] = arcParametrs.forward.toByte()
        moveArcArray[1] = arcParametrs.direction.toByte()
        moveArcArray[2] = 0
        moveArcArray[3] = (abs(arcParametrs.radius).toInt() * 10).toByte()
        moveArcArray[4] = 0
        moveArcArray[5] = (abs(arcParametrs.speed) * 100).toInt().toByte()
        moveArcArray[6] = DUMMY_BYTE
        moveArcArray[7] = DUMMY_BYTE
        val arcParametrsMessage = BluetoothMessageFrame(
            CANmessageData(
                BluetoothAdressCodeModel.RIDE_THE_CURVE,
                moveArcArray
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
        switchSettings: SwitchSettingsBD

    ) {
        switchesArray[0] = switchSettings.switch_number.toByte()
        switchesArray[1] =
            if (switchSettings.is_switch_activate) AgribotToolActivationStatus.ACTIVE.active else AgribotToolActivationStatus.INACTIVE.active
        switchesArray[2] = switchSettings.switch_parametr_value.toByte()

        val switchesMessageFrame = BluetoothMessageFrame(
            CANmessageData(
                BluetoothAdressCodeModel.AGRIBOT_SET_SWITCH,
                switchesArray
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
        isFrontKartActive: Boolean,
        isRearKartActive: Boolean
    ) {
        ttControlForwardBackwardArray[0] = 1.toByte()
        ttControlForwardBackwardArray[1] = DUMMY_BYTE
        ttControlForwardBackwardArray[2] =
            if (isFrontKartActive) AgribotToolActivationStatus.ACTIVE.active else AgribotToolActivationStatus.INACTIVE.active
        ttControlForwardBackwardArray[3] =
            if (isRearKartActive) AgribotToolActivationStatus.ACTIVE.active else AgribotToolActivationStatus.INACTIVE.active

        val moveForwardArray = ttControlForwardBackwardArray
        moveForwardArray[0] = 1.toByte()
        moveForwardArray[1] = DUMMY_BYTE
        val forwardMessageFrame = BluetoothMessageFrame(
            CANmessageData(
                BluetoothAdressCodeModel.MOVE_FORWARD_BACKWARD,
                moveForwardArray
            )
        )
        writeBluetoothMessage(forwardMessageFrame.bluetoothMessageArray)
    }

    override fun sendTTControlBackwardMessage(
        isFrontKartActive: Boolean,
        isRearKartActive: Boolean
    ) {
        ttControlForwardBackwardArray[0] = 0.toByte()
        ttControlForwardBackwardArray[1] = DUMMY_BYTE
        ttControlForwardBackwardArray[2] =
            if (isFrontKartActive) AgribotToolActivationStatus.ACTIVE.active else AgribotToolActivationStatus.INACTIVE.active
        ttControlForwardBackwardArray[3] =
            if (isRearKartActive) AgribotToolActivationStatus.ACTIVE.active else AgribotToolActivationStatus.INACTIVE.active

        val moveBackArray = ttControlForwardBackwardArray
        moveBackArray[0] = 0.toByte()
        moveBackArray[1] = DUMMY_BYTE

        val backwardMessageFrame = BluetoothMessageFrame(
            CANmessageData(
                BluetoothAdressCodeModel.MOVE_FORWARD_BACKWARD,
                moveBackArray
            )
        )
        writeBluetoothMessage(backwardMessageFrame.bluetoothMessageArray)
    }

    override fun sendTTControlTurnLeftMessage(
        isFrontKartActive: Boolean,
        isRearKartActive: Boolean
    ) {
        ttControlLeftRightArray[0] =
            if (isFrontKartActive) AgribotToolActivationStatus.ACTIVE.active else AgribotToolActivationStatus.INACTIVE.active
        ttControlLeftRightArray[1] = AgribotKartsRotate.LEFT.rotate
        ttControlLeftRightArray[2] =
            if (isRearKartActive) AgribotToolActivationStatus.ACTIVE.active else AgribotToolActivationStatus.INACTIVE.active
        ttControlLeftRightArray[3] = AgribotKartsRotate.LEFT.rotate

        val rotateFrame = BluetoothMessageFrame(
            CANmessageData(BluetoothAdressCodeModel.ROTATE_CARRIAGES, ttControlLeftRightArray)
        )

        writeBluetoothMessage(rotateFrame.bluetoothMessageArray)
    }

    override fun sendTTControlTurnRightMessage(
        isFrontKartActive: Boolean,
        isRearKartActive: Boolean
    ) {
        ttControlLeftRightArray[0] =
            if (isFrontKartActive) AgribotToolActivationStatus.ACTIVE.active else AgribotToolActivationStatus.INACTIVE.active
        ttControlLeftRightArray[1] = AgribotKartsRotate.RIGHT.rotate
        ttControlLeftRightArray[2] =
            if (isRearKartActive) AgribotToolActivationStatus.ACTIVE.active else AgribotToolActivationStatus.INACTIVE.active
        ttControlLeftRightArray[3] = AgribotKartsRotate.RIGHT.rotate

        val rotateFrame = BluetoothMessageFrame(
            CANmessageData(BluetoothAdressCodeModel.ROTATE_CARRIAGES, ttControlLeftRightArray)
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
                writeBluetoothMessage(heartbeatFrame.bluetoothMessageArray)
                if (heartbeatArray[0].toInt() % 4 == 0) {
                    sendDiagnosticDataRequestMessage()
                }
                delay(HEARTBEAT_DELAY_TIME)
                heartbeatArray[0]++
            }
        }
    }

    override fun stopSendingHeartbeat() {
        heartbeatScope.cancel()
        heartbeatScope = MainScope()
    }
}
