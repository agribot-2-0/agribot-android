package eu.agribot.agribotapp.domain.service

import eu.agribot.agribotapp.communication.bluetooth.basic.connection.BluetoothConnectionManager
import eu.agribot.agribotapp.communication.bluetooth.basic.message.writemessage.WriteBluetoothMessageManager
import eu.agribot.agribotapp.communication.bluetooth.error.BluetothErrorCode.BLUETOOTH_CONNECTION_ERROR_CODE
import eu.agribot.agribotapp.communication.bluetooth.error.BluetothErrorCode.BLUETOOTH_HEARTBEAT_ERROR_CODE
import eu.agribot.agribotapp.communication.bluetooth.error.BluetothErrorCode.BLUETOOTH_RECEIVE_MESSAGE_CODE
import eu.agribot.agribotapp.communication.bluetooth.error.BluetothErrorCode.BLUETOOTH_WRITE_MESSAGE_ERROR_CODE
import eu.agribot.agribotapp.communication.bluetooth.error.BluetothErrorCode.CREATE_BLUETOOTH_SERVER_CODE
import eu.agribot.agribotapp.communication.bluetooth.error.BluetothErrorCode.ERROR_FROM_AGRIBOT_CODE
import eu.agribot.agribotapp.communication.bluetooth.error.connection.BluetoothConnectionErrorHandler
import eu.agribot.agribotapp.communication.bluetooth.error.message.receive.BluetoothMessageReceiveErrorHandler
import eu.agribot.agribotapp.communication.bluetooth.error.message.write.BluetoothMessageWriteErrorHandler
import eu.agribot.agribotapp.domain.model.todomain.BluetoothErrorInfo

/**
 * Domain Service used to basic bluetooth operations
 */

class BluetoothConnectionService(
    val bluetoothConnectionManager: BluetoothConnectionManager,
    private val bluetoothWriteBluetoothMessageManager: WriteBluetoothMessageManager,
    private val bluetoothConnectionErrorHandler: BluetoothConnectionErrorHandler,
    private val bluetoothMessageWriteErrorHandler: BluetoothMessageWriteErrorHandler,
    private val bluetoothMessageReceiveErrorHandler: BluetoothMessageReceiveErrorHandler
) {
    private val connectionErrors: BluetoothErrorInfo? =
        bluetoothConnectionErrorHandler.getConnectionsError()
    private val writeMessageErrors: BluetoothErrorInfo? =
        bluetoothMessageWriteErrorHandler.getMessageWriteError()
    private val receiveMessageErrors: BluetoothErrorInfo? =
        bluetoothMessageReceiveErrorHandler.getMessageReceiveError()

    fun checkBluetoothEnable(): Boolean {
        return bluetoothConnectionManager.checkIsBluetoothEnable()
    }

    fun enableBluetooth() {
        bluetoothConnectionManager.enableBluetooth()
    }

    suspend fun runServer(isSecurityConnection: Boolean) {
        bluetoothConnectionManager.registerBluetoothServer(isSecurityConnection)
    }

    suspend fun connect(bluetoothDeviceAddress: String) {
        bluetoothConnectionManager.connectBluetoothWithAgribot(
            bluetoothDeviceAddress
        )
    }

    fun disconnect() {
        bluetoothConnectionManager.disconnect()
    }

    fun getBluetoothConnectionErrors(): String? {
        return if (connectionErrors == BluetoothErrorInfo(CREATE_BLUETOOTH_SERVER_CODE)) {
            "Bluetooth Create Server Error"
        } else if (connectionErrors == BluetoothErrorInfo(BLUETOOTH_CONNECTION_ERROR_CODE)) {
            "Bluetooth Connection Error"
        } else if (connectionErrors == null) {
            null
        } else {
            "Connection Bluetooth"
        }
    }

    fun getBluetoothReceiveMessageErrors(): String? {
        return when (receiveMessageErrors) {
            BluetoothErrorInfo(BLUETOOTH_RECEIVE_MESSAGE_CODE) -> {
                "Receive Message Error"
            }
            BluetoothErrorInfo(ERROR_FROM_AGRIBOT_CODE) -> {
                "Error from Agribot"
            }
            BluetoothErrorInfo(BLUETOOTH_HEARTBEAT_ERROR_CODE) -> {
                "Heartbeat Error"
            }
            else -> {
                null
            }
        }
    }

    fun getBluetoothWriteMessageErrors(): String? {
        return if (writeMessageErrors == BluetoothErrorInfo(BLUETOOTH_WRITE_MESSAGE_ERROR_CODE)) {
            "Bluetooth Write Message Error"
        } else if (writeMessageErrors == null) {
            null
        } else {
            "Write Bluetooth Message Error"
        }
    }
}
