package eu.agribot.agribotapp.communication.bluetooth.error.message

interface BluetoothMessageWriteErrorHandler {

    fun onWriteMessageError(messageException: Exception)
}