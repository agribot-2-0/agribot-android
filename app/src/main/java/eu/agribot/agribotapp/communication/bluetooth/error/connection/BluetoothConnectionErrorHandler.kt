package eu.agribot.agribotapp.communication.bluetooth.error.connection

import eu.agribot.agribotapp.domain.model.todomain.BluetoothErrorInfo

interface BluetoothConnectionErrorHandler {

    fun onCreateBluetoothServerError()

    fun onBluetoothConnectionError()

    fun getConnectionsError(): BluetoothErrorInfo?
}
