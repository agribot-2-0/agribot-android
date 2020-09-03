package eu.agribot.agribotapp.communication.bluetooth.error.message.receive

import android.util.Log

class AndroidBluetoothMessageReceiveErrorHandler : BluetoothMessageReceiveErrorHandler {

    override fun onReceiveBluetoothMessageError(messageException: Exception) {
        Log.e("error", "onSendMessageError : " + messageException)
    }
}