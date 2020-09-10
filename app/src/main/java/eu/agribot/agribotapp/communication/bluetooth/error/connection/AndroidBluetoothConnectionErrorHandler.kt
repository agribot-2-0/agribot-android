package eu.agribot.agribotapp.communication.bluetooth.error.connection

import eu.agribot.agribotapp.common.livedata.MutableLiveEvent

class AndroidBluetoothConnectionErrorHandler() : BluetoothConnectionErrorHandler {
    private val connectionErrors: MutableLiveEvent<String> = MutableLiveEvent()

    override fun onCreateBluetoothServerError() {
        connectionErrors.postValue("Create Server Error")
    }

    override fun onBluetoothConnectionError() {
        connectionErrors.postValue("Connection Error")
    }

    override fun observeConnectionsError(): MutableLiveEvent<String> {
        return connectionErrors
    }
}
