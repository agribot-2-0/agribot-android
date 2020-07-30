package eu.agribot.agribotapp.domain.service

import android.annotation.SuppressLint
import android.os.Handler
import android.os.HandlerThread
import android.os.Process
import android.util.Log
import eu.agribot.agribotapp.communication.bluetooth.BluetoothService
import eu.agribot.agribotapp.domain.model.BluetoothAdressCodeModel
import eu.agribot.agribotapp.domain.model.BluetoothMessageModel
import timber.log.Timber

class BluetoothUtils(private val bluetoothService: BluetoothService) {
    var isAgribotConnect: Boolean = false

    companion object {
        const val MOBILE_HEARTBEAT = 1
        private const val HEARTBEAT_DELAY_TIME = 250L
    }

    var connectionState: Boolean = true
    private var byteArray = ByteArray(0)
    private var heartbeatArray = ByteArray(1)
    var heartbeatFrame = BluetoothMessageModel(
        BluetoothMessageModel.CMD_ONE,
        BluetoothAdressCodeModel.MOBILE_HEARTBEAT,
        heartbeatArray
    )

    private var heartbeatHandlerThread =
        HandlerThread("HeartBeatThread", Process.THREAD_PRIORITY_DISPLAY)
    internal lateinit var heartbeatHandler: Handler

    private var heartbeatRunnable = object : Runnable {

        override fun run() {
            writeMessage(heartbeatFrame.byteArray)
            heartbeatArray[0]++
            heartbeatFrame = BluetoothMessageModel(
                BluetoothMessageModel.CMD_ONE,
                BluetoothAdressCodeModel.MOBILE_HEARTBEAT,
                heartbeatArray
            )

            if (heartbeatArray[0].toInt() % 4 == 0) {
                Timber.e("modulo 4")
                // sendStatusRequest()
            }
            if (heartbeatArray[0].toInt() % 50 == 0) {
                Timber.e("modulo 50")
            }

            if (connectionState) {
                heartbeatHandler.postDelayed(this, HEARTBEAT_DELAY_TIME)
            }
        }
    }

    // private fun setConnectionState() {
    //   bluetoothService.setConnectionState(connectionState)
    // }

    /*
    private fun sendStatusRequest() {
        writeMessage(
            BluetoothMessageModel(
                BluetoothMessageModel.CMD_ONE, BluetoothAdressCodeModel.AGRIBOT_DIAGNOSTIC_DATA_1,
                ByteArray(1)
            ).byteArray
        )
    }
    */

    fun checkBluetoothEnable(): Boolean {
        return bluetoothService.checkIsBluetoothEnable()
    }

    fun enableBluetooth() {
        bluetoothService.enableBluetooth()
    }

    suspend fun runServer(isSecurityConnection: Boolean) {
        bluetoothService.registerBluetoothServer(isSecurityConnection)
    }

    suspend fun connect(bluetoothDeviceAddress: String) {
        bluetoothService.connectBluetoothWithAgribot(bluetoothDeviceAddress)
        // setConnectionState()
    }

    suspend fun getMessage() {
        bluetoothService.receiveBluetoothMessage(handleMessage = this::handleMessage)
    }

    @SuppressLint("LogNotTimber")
    private fun handleMessage(message: ByteArray) {
        byteArray = message
        Log.e(
            "xd",
            "RECEIVED MESSAGE in HEX:" + getAllMessageInHex()
        )
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
                val btFrame = BluetoothMessageModel(readBuf)
                handleMessageFrame(btFrame)
                // readFrames(readBuf.slice((length + 4)..readBuf.size).toByteArray())
            } else {
                return
            }
        } catch (error: NegativeArraySizeException) {
            Timber.e("READ FRAMES ERROR %s", error.toString())
        }
    }

    private fun handleMessageFrame(messageFrame: BluetoothMessageModel) {
        Timber.e("messageFrame : %s", messageFrame)
        if (messageFrame.getAddress() == BluetoothAdressCodeModel.MANUAL_STARTING_INIT) {
            Timber.e("manual stering init request")
            // manualModeService.sendStartManualControlFrame()
        } else if (messageFrame.getAddress() == BluetoothAdressCodeModel.MANUAL_START) {
            Timber.e("manual stering start request")
        }
    }

    @SuppressLint("LogNotTimber")
    fun writeMessage(byteMessage: ByteArray) {
        bluetoothService.writeBluetoothMessage(byteMessage)
        Log.e(
            "xd",
            ", WROTE MESSAGE in HEX:" + getAllMessageInHex()
        )
    }

    private fun getAllMessageInHex(): String {
        var message = ""
        for (element in byteArray)
            message += " " + String.format("%02X", element)
        return message
    }

    fun sendHandshake(
        sendMessage: (textMessage: ByteArray) -> Unit
    ) {
        val frame = BluetoothMessageModel(
            BluetoothAdressCodeModel.MOBILE_HANDSHAKE,
            ByteArray(1) { 1 }
        )
        frame.logBtFrame()
        sendMessage(frame.byteArray)
    }

    fun startSendingHeartBeat() {
        heartbeatArray[0] = 0.toByte()
        heartbeatFrame = BluetoothMessageModel(
            BluetoothMessageModel.CMD_ONE, MOBILE_HEARTBEAT,
            heartbeatArray
        )
        if (connectionState)
            heartbeatHandler.post(heartbeatRunnable)
    }

    fun initHeartbeat() {
        heartbeatHandlerThread = HandlerThread("HBThread", Process.THREAD_PRIORITY_DISPLAY)
        heartbeatHandlerThread.start()
        heartbeatHandler = Handler(heartbeatHandlerThread.looper)
    }
}
