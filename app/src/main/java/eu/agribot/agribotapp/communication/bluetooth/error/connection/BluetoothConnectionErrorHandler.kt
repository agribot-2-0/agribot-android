package eu.agribot.agribotapp.communication.bluetooth.error.connection

interface BluetoothConnectionErrorHandler {

    fun onCreateBluetoothServerError(createServerException: Exception)

    fun onBluetoothConnectionError(connectionException: Exception)

    fun onSendBluetoothMessageError(sendBluetoohMessageException: Exception)

    fun onReceiveBluetoothMessageError(receiveMessageException: Exception)
}