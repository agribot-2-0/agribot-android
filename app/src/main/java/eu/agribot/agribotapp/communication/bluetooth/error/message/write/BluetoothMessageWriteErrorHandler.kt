package eu.agribot.agribotapp.communication.bluetooth.error.message.write

import eu.agribot.agribotapp.communication.bluetooth.model.fromcommunication.BluetoothErrorInfoBD

interface BluetoothMessageWriteErrorHandler {

    fun onHeartBeatError()
    fun onWriteMessageError()
    fun getMessageWriteError(): BluetoothErrorInfoBD?
}
