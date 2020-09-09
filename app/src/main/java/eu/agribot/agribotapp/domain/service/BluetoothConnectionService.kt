package eu.agribot.agribotapp.domain.service

import eu.agribot.agribotapp.common.livedata.MutableLiveEvent
import eu.agribot.agribotapp.communication.bluetooth.connection.BluetoothConnectionManager
import eu.agribot.agribotapp.communication.bluetooth.error.connection.BluetoothConnectionErrorHandler
import eu.agribot.agribotapp.communication.bluetooth.error.message.receive.BluetoothMessageReceiveErrorHandler
import eu.agribot.agribotapp.communication.bluetooth.error.message.write.BluetoothMessageWriteErrorHandler

/**
 * Domain Service used to basic bluetooth operations
 */

class BluetoothConnectionService(
    val bluetoothConnectionManager: BluetoothConnectionManager,
    private val bluetoothConnectionErrorHandler: BluetoothConnectionErrorHandler,
    private val bluetoothMessageWriteErrorHandler: BluetoothMessageWriteErrorHandler,
    private val bluetoothMessageReceiveErrorHandler: BluetoothMessageReceiveErrorHandler
) {
    val connectionErrors: MutableLiveEvent<String> =
        bluetoothConnectionErrorHandler.observeConnectionsError()
    val writeMessageErrors: MutableLiveEvent<String> =
        bluetoothMessageWriteErrorHandler.observeWriteMessageError()
    val receiveMessageErrors: MutableLiveEvent<String> =
        bluetoothMessageReceiveErrorHandler.observeMessageReceiveError()

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

    fun observeBluetoothConnectionErrors(): MutableLiveEvent<String> {
        return connectionErrors
    }

    fun observeBluetoothWriteMessageErrors(): MutableLiveEvent<String> {
        return writeMessageErrors
    }

    fun observeBluetoothReceiveMessageErrors(): MutableLiveEvent<String> {
        return receiveMessageErrors
    }
}
