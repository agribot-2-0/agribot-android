package eu.agribot.agribotapp.communication.bluetooth.error.message.receive

import eu.agribot.agribotapp.domain.model.todomain.BluetoothErrorInfo

interface BluetoothMessageReceiveErrorHandler {
    fun onReceiveBluetoothMessageError()

    fun handleAgribotError()

    fun getMessageReceiveError(): BluetoothErrorInfo?
}
