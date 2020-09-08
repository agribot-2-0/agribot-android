package eu.agribot.agribotapp.communication.bluetooth.error.message.write

interface BluetoothMessageWriteErrorHandler {

    fun onWriteMessageError(messageException: Exception)
}