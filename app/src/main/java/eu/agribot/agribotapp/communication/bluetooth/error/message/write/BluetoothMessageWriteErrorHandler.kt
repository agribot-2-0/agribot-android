package eu.agribot.agribotapp.communication.bluetooth.error.message.write

import eu.agribot.agribotapp.common.livedata.MutableLiveEvent

interface BluetoothMessageWriteErrorHandler {

    fun onHeartBeatError()
    fun onWriteMessageError()
    fun observeWriteMessageError(): MutableLiveEvent<String>
}
