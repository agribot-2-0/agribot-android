package eu.agribot.agribotapp.communication.bluetooth.message.writemessage

import android.bluetooth.BluetoothSocket
import android.os.Handler
import android.os.HandlerThread
import android.os.Process
import android.util.Log
import eu.agribot.agribotapp.communication.bluetooth.const.BluetoothAdressCodeModel
import eu.agribot.agribotapp.communication.bluetooth.const.ConstatntConnectionValue
import eu.agribot.agribotapp.communication.bluetooth.error.message.BluetoothMessageWriteErrorHandler
import eu.agribot.agribotapp.communication.bluetooth.message.frame.BluetoothMessageFrame
import eu.agribot.agribotapp.communication.bluetooth.model.CANmessageData
import java.io.IOException
import java.io.OutputStream

class AndroidWriteBluetoothMessageManager(
    private val bluetoothWriteMessageHandler: Handler,
    private val bluetoothMessageWriteErrorHandler: BluetoothMessageWriteErrorHandler
) :
    WriteBluetoothMessageManager {
    private var writeSocket: BluetoothSocket? = null
    private val EMPTY_CAN_DATA = ByteArray(1)
    private val singleArray = ByteArray(1) { 255.toByte() }
    private var heartbeatHandlerThread =
        HandlerThread("HeartBeatThread", Process.THREAD_PRIORITY_DISPLAY)
    private lateinit var heartbeatHandler: Handler
    private val HEARTBEAT_DELAY_TIME = 50L
    private var heartbeatArray = ByteArray(1)

    var heartbeatFrame =
        BluetoothMessageFrame(
            CANmessageData(BluetoothAdressCodeModel.MOBILE_HEARTBEAT, heartbeatArray)
        )

    private var heartbeatRunnable = object : Runnable {

        override fun run() {
            writeBluetoothMessage(heartbeatFrame.bluetoothMessageArray)
            heartbeatArray[0]++
            heartbeatFrame = BluetoothMessageFrame(
                CANmessageData(BluetoothAdressCodeModel.MOBILE_HEARTBEAT, heartbeatArray)
            )
            heartbeatHandler.postDelayed(this, HEARTBEAT_DELAY_TIME)

        }
    }


    private fun writeBluetoothMessage(
        message: ByteArray
    ) {
        val writeMessageBuffer = ByteArray(ConstatntConnectionValue.MESSAGE_BUFFER_SIZE)
        val outputStream: OutputStream = writeSocket!!.outputStream
        try {
            outputStream.write(message)
            Log.e("xd", "write" + message)
        } catch (writeMessageException: IOException) {
            bluetoothMessageWriteErrorHandler.onWriteMessageError(writeMessageException)
            return
        }
        val writtenMsg = bluetoothWriteMessageHandler.obtainMessage(
            ConstatntConnectionValue.MESSAGE_WRITE, -1, -1, writeMessageBuffer
        )
        writtenMsg.sendToTarget()
    }

    private fun initializeHeartbeat() {
        heartbeatHandlerThread = HandlerThread("HBThread", Process.THREAD_PRIORITY_DISPLAY)
        heartbeatHandlerThread.start()
        heartbeatHandler = Handler(heartbeatHandlerThread.looper)
    }

    override fun sendHandshakeMessage(bluetoothSocket: BluetoothSocket?) {
        initializeHeartbeat()
        writeSocket = bluetoothSocket
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
        arcParametrsArray: ByteArray
    ) {
        val arcParametrsMessage = BluetoothMessageFrame(
            CANmessageData(
                BluetoothAdressCodeModel.RIDE_THE_CURVE,
                arcParametrsArray
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

        writeBluetoothMessage(
            autonomusModeInitializationMessage.bluetoothMessageArray

        )
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


    override fun sendOverrideModeVerticalSteeringMessage() {
        TODO("Not yet implemented")
    }

    override fun sendOverrideModeHorizontalSteeringMessage() {
        TODO("Not yet implemented")
    }

    override fun startSendingHeartbeat() {
        heartbeatArray[0] = 0.toByte()
        BluetoothMessageFrame(
            CANmessageData(BluetoothAdressCodeModel.MOBILE_HEARTBEAT, heartbeatArray)
        )
        heartbeatHandler.post(heartbeatRunnable)
    }
}