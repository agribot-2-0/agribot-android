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
    var heartbeatState: Boolean = false

    companion object {
        const val MOBILE_HEARTBEAT = 1
        private const val HEARTBEAT_DELAY_TIME = 50L
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

    fun disconnect() {
        bluetoothService.disconnect()
    }

    suspend fun getMessage() {
        bluetoothService.receiveBluetoothMessage(handleMessage = this::handleMessage)
    }

    fun setWrongHeartbeat() {
        val handler = Handler()
        handler.postDelayed(object : Runnable {
            override fun run() {
                heartbeatState = false
                handler.postDelayed(this, 1000) // 1 sec delay
            }
        }, 0)
    }

    @SuppressLint("LogNotTimber")
    private fun handleMessage(message: ByteArray) {
        byteArray = message
        readFrames(message)
    }

    private fun readFrames(readBuf: ByteArray) {
        heartbeatState = true
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
        if (messageFrame.getAddress() == BluetoothAdressCodeModel.RIDE_THE_CURVE) {
            Log.e("handleMessage", "POTWIERDZENIE JOYA!!!!!")
        }
        if (messageFrame.getAddress() == BluetoothAdressCodeModel.MANUAL_STARTING_INIT) {
            Log.e("handleMessage", "manual stering init request")
            // manualModeService.sendStartManualControlFrame()
        } else if (messageFrame.getAddress() == BluetoothAdressCodeModel.MANUAL_START) {
            Log.e("handleMessage", "manual stering start request")
        }
    }

    @SuppressLint("LogNotTimber")
    fun writeMessage(byteMessage: ByteArray) {
        bluetoothService.writeBluetoothMessage(byteMessage)
    }

    private fun getAllMessageInHex(): String {
        var message = ""
        for (element in byteArray)
            message += " " + String.format("%02X", element)
        return message
    }

<<<<<<< HEAD
    fun sendHandshake(
        sendMessage: (textMessage: ByteArray) -> Unit
    ) {
=======
    fun sendHandshake() {
>>>>>>> 0bd8dbd... static code analyze fix
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
