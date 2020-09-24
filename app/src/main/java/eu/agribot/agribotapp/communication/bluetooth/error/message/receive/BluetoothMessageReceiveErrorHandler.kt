package eu.agribot.agribotapp.communication.bluetooth.error.message.receive

import eu.agribot.agribotapp.communication.bluetooth.model.fromcommunication.BluetoothErrorInfoBD

interface BluetoothMessageReceiveErrorHandler {
    fun onReceiveBluetoothMessageError()

    fun handleAgribotError()

    fun getMessageReceiveError(): BluetoothErrorInfoBD?
}
