package eu.agribot.agribotapp.communication.bluetooth.error.message

import android.util.Log

class AndroidBluetoothMessageWriteErrorHandler : BluetoothMessageWriteErrorHandler {

    override fun onWriteMessageError(messageException: Exception) {
        Log.e("error", "onSendMessageError : " + messageException)
    }

}