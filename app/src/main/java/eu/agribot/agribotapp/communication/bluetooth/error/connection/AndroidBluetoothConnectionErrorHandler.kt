package eu.agribot.agribotapp.communication.bluetooth.error.connection

import android.util.Log

class AndroidBluetoothConnectionErrorHandler : BluetoothConnectionErrorHandler {

    override fun onCreateBluetoothServerError(createServerException: Exception) {
        Log.e("error", "onCreateBluetoothServerError : " + createServerException)
    }

    override fun onBluetoothConnectionError(connectionException: Exception) {
        Log.e("error", "onBluetoothConnectionError : " + connectionException)
    }

    override fun onSendBluetoothMessageError(sendBluetoohMessageException: Exception) {
        Log.e("error", "onSendBluetoothMessageError : " + sendBluetoohMessageException)
    }

    override fun onReceiveBluetoothMessageError(receiveMessageException: Exception) {
        Log.e("error", "onReceiveBluetoothMessageError : " + receiveMessageException)
    }

}