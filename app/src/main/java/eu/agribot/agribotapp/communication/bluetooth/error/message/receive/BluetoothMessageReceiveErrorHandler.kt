package eu.agribot.agribotapp.communication.bluetooth.error.message.receive

import eu.agribot.agribotapp.common.livedata.MutableLiveEvent

interface BluetoothMessageReceiveErrorHandler {
    fun onReceiveBluetoothMessageError()

    fun handleAgribotError()

    fun observeMessageReceiveError(): MutableLiveEvent<String>
}
