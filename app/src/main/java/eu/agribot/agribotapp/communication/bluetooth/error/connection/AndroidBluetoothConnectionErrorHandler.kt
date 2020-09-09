package eu.agribot.agribotapp.communication.bluetooth.error.connection

import eu.agribot.agribotapp.common.livedata.MutableLiveEvent

class AndroidBluetoothConnectionErrorHandler : BluetoothConnectionErrorHandler {
    private val connectionErrors: MutableLiveEvent<String> = MutableLiveEvent()

    override fun onCreateBluetoothServerError() {
        connectionErrors.postValue("Create Bluetooth Server Error")
    }

    override fun onBluetoothConnectionError() {
        connectionErrors.postValue("Bluetooth Connection Error")
    }

    override fun onSendBluetoothMessageError() {
        connectionErrors.postValue("Send Bluetooth Message Error")
    }

    override fun onReceiveBluetoothMessageError() {
        connectionErrors.postValue("Receive Bluetooth Message Error")
    }

    override fun observeConnectionsError(): MutableLiveEvent<String> {
        return connectionErrors
    }
}
