package eu.agribot.agribotapp.communication.bluetooth.error.message.receive

interface BluetoothMessageReceiveErrorHandler {
    fun onReceiveBluetoothMessageError(messageException: Exception)
}