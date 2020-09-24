package eu.agribot.agribotapp.communication.bluetooth.error.connection

import eu.agribot.agribotapp.communication.bluetooth.error.BluetothErrorCode.BLUETOOTH_CONNECTION_ERROR_CODE
import eu.agribot.agribotapp.communication.bluetooth.error.BluetothErrorCode.CREATE_BLUETOOTH_SERVER_CODE
import eu.agribot.agribotapp.communication.bluetooth.model.fromcommunication.BluetoothErrorInfoBD

class AndroidBluetoothConnectionErrorHandler : BluetoothConnectionErrorHandler {
    private var connectionErrors: BluetoothErrorInfoBD? = null

    override fun onCreateBluetoothServerError() {
        connectionErrors = BluetoothErrorInfoBD(CREATE_BLUETOOTH_SERVER_CODE)
    }

    override fun onBluetoothConnectionError() {
        connectionErrors = BluetoothErrorInfoBD(BLUETOOTH_CONNECTION_ERROR_CODE)
    }

    override fun getConnectionsError(): BluetoothErrorInfoBD? {
        return connectionErrors
    }
}
