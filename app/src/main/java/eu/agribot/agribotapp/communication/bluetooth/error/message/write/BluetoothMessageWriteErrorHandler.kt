package eu.agribot.agribotapp.communication.bluetooth.error.message.write

import eu.agribot.agribotapp.domain.model.todomain.BluetoothErrorInfo

interface BluetoothMessageWriteErrorHandler {

    fun onHeartBeatError()
    fun onWriteMessageError()
    fun getMessageWriteError(): BluetoothErrorInfo?
}
