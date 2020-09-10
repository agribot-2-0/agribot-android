package eu.agribot.agribotapp.communication.bluetooth.error.connection

import eu.agribot.agribotapp.common.livedata.MutableLiveEvent

interface BluetoothConnectionErrorHandler {

    fun onCreateBluetoothServerError()

    fun onBluetoothConnectionError()

    fun observeConnectionsError(): MutableLiveEvent<String>
}
