package eu.agribot.agribotapp.communication.bluetooth.error.connection

import eu.agribot.agribotapp.communication.bluetooth.model.fromcommunication.BluetoothErrorInfoBD

interface BluetoothConnectionErrorHandler {

    fun onCreateBluetoothServerError()

    fun onBluetoothConnectionError()

    fun getConnectionsError(): BluetoothErrorInfoBD?
}
