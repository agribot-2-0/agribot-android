package eu.agribot.agribotapp.domain.service

import android.os.Handler
import android.os.HandlerThread
import android.os.Process
import eu.agribot.agribotapp.domain.error.AgribotErrorHandler
import eu.agribot.agribotapp.domain.model.BluetoothAdressCodeModel
import eu.agribot.agribotapp.domain.model.BluetoothMessageModel
import timber.log.Timber
/**
 * Service used to basic message-communication with Agribot
 */

class BluetoothUtils(
    private val bluetoothConnectionService: BluetoothConnectionService,
    private val diagnosticDataService: DiagnosticDataService,
    private val agribotErrorHandler: AgribotErrorHandler
) {
    var isAgribotConnect: Boolean = false
    var heartbeatState: Boolean = false

    companion object {
        const val MOBILE_HEARTBEAT = 1
        private const val HEARTBEAT_DELAY_TIME = 50L
        private const val WRONG_HEARTBEAT_DELAY_TIME = 1000L
    }

    private var byteArray = ByteArray(0)
    private var heartbeatArray = ByteArray(1)
    var heartbeatFrame =
        BluetoothMessageModel(
            BluetoothMessageModel.CMD_ONE,
            BluetoothAdressCodeModel.MOBILE_HEARTBEAT,
            heartbeatArray
        )

    private var heartbeatHandlerThread =
        HandlerThread("HeartBeatThread", Process.THREAD_PRIORITY_DISPLAY)
    internal lateinit var heartbeatHandler: Handler

    // Todo corutine
    private var heartbeatRunnable = object : Runnable {

        override fun run() {
            writeMessage(heartbeatFrame.byteArray)
            heartbeatArray[0]++
            heartbeatFrame =
                BluetoothMessageModel(
                    BluetoothMessageModel.CMD_ONE,
                    BluetoothAdressCodeModel.MOBILE_HEARTBEAT,
                    heartbeatArray
                )

            if (heartbeatArray[0].toInt() % 4 == 0) {
                sendStatusRequest()
            }

            if (heartbeatArray[0].toInt() % 50 == 0) {
                Timber.e("modulo 50")
            }

            if (isAgribotConnect) {
                heartbeatHandler.postDelayed(this, HEARTBEAT_DELAY_TIME)
            }
        }
    }

    private fun sendStatusRequest() {
        writeMessage(
            BluetoothMessageModel(
                BluetoothMessageModel.CMD_ONE,
                BluetoothAdressCodeModel.AGRIBOT_DIAGNOSTIC_DATA_1,
                ByteArray(1)
            ).byteArray
        )
    }

    suspend fun runServer(isSecurityConnection: Boolean) {
        bluetoothConnectionService.runServer(isSecurityConnection)
    }

    suspend fun connect(bluetoothDeviceAddress: String) {
        bluetoothConnectionService.connect(bluetoothDeviceAddress)
    }

    fun disconnect() {
        bluetoothConnectionService.disconnect()
        isAgribotConnect = false
    }

    fun writeMessage(byteMessage: ByteArray) {
        bluetoothConnectionService.writeMessage(byteMessage)
    }

    suspend fun getMessage() {
        bluetoothConnectionService.getMessage(handleMessage = this::handleMessage)
    }

    fun setWrongHeartbeat() {
        val handler = Handler()
        handler.postDelayed(object : Runnable {
            override fun run() {
                heartbeatState = false
                handler.postDelayed(this, WRONG_HEARTBEAT_DELAY_TIME)
            }
        }, 0)
    }

    private fun handleMessage(message: ByteArray) {
        byteArray = message
        readFrames(message)
    }

    private fun readFrames(readBuf: ByteArray) {
        try {
            if (readBuf[0] == 67.toByte()) {
                val length = readBuf[1].toInt()
                if (length <= 0)
                    return
                if (readBuf.isEmpty())
                    return
                val btFrame =
                    BluetoothMessageModel(
                        readBuf
                    )
                handleMessageFrame(btFrame)
            } else {
                return
            }
        } catch (error: NegativeArraySizeException) {
            Timber.e("READ FRAMES ERROR %s", error.toString())
        }
    }

    private fun handleMessageFrame(messageFrame: BluetoothMessageModel) {
        when {
            messageFrame.getAddress() == BluetoothAdressCodeModel.RIDE_THE_CURVE -> {
                Timber.e("Joystick Action Request")
            }
            messageFrame.getAddress() == BluetoothAdressCodeModel.MANUAL_STARTING_INIT -> {
                Timber.e("Manual stering init request")
            }
            messageFrame.getAddress() == BluetoothAdressCodeModel.MANUAL_START -> {
                Timber.e("Manual stering start request")
            }
            messageFrame.getAddress() == BluetoothAdressCodeModel.AGRIBOT_DIAGNOSTIC_DATA_1 -> {
                readFirstDiagnosticData(messageFrame)
            }
            messageFrame.getAddress() == BluetoothAdressCodeModel.MOBILE_HEARTBEAT -> {
                heartbeatState = true
            }
            messageFrame.getAddress() == BluetoothAdressCodeModel.AGRIBOT_ERROR -> {
                agribotErrorHandler.handleAgribotError()
            }
        }
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

    private fun readFirstDiagnosticData(messageFrame: BluetoothMessageModel) {
        if (messageFrame.getMessage().size < 8)
            return
        val engineRpm = bytesToUnsingedInt(messageFrame.getMessage().sliceArray(0..1))
        val batteryVoltage =
            bytesToUnsingedInt(messageFrame.getMessage().sliceArray(6..7)) * 0.001
        diagnosticDataService.readFirstEngineData(
            engineRpm,
            messageFrame.getMessage()[2].toInt(),
            messageFrame.getMessage()[3].toInt(),
            messageFrame.getMessage()[4].toInt(),
            messageFrame.getMessage()[5].toInt(),
            batteryVoltage
        )
    }

    fun sendHandshake() {
        val frame = BluetoothMessageModel(
            BluetoothAdressCodeModel.MOBILE_HANDSHAKE,
            ByteArray(1) { 1 }
        )
        writeMessage(frame.byteArray)
    }

    fun startSendingHeartBeat() {
        heartbeatArray[0] = 0.toByte()
        heartbeatFrame =
            BluetoothMessageModel(
                BluetoothMessageModel.CMD_ONE,
                MOBILE_HEARTBEAT,
                heartbeatArray
            )
        if (isAgribotConnect)
            heartbeatHandler.post(heartbeatRunnable)
    }

    fun initHeartbeat() {
        heartbeatHandlerThread = HandlerThread("HBThread", Process.THREAD_PRIORITY_DISPLAY)
        heartbeatHandlerThread.start()
        heartbeatHandler = Handler(heartbeatHandlerThread.looper)
    }

    fun stopSendingHeartbeat() {
        heartbeatHandler.removeCallbacks(heartbeatRunnable)
    }

    fun restartHandshakeAndHeartbeat() {
        sendHandshake()
        Handler().postDelayed({
            startSendingHeartBeat()
        }, HEARTBEAT_DELAY_TIME)
    }
}
