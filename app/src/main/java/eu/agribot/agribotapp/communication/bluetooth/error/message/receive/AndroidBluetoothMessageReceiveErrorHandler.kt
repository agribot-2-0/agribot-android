package eu.agribot.agribotapp.communication.bluetooth.error.message.receive

import eu.agribot.agribotapp.common.livedata.MutableLiveEvent

class AndroidBluetoothMessageReceiveErrorHandler() :
    BluetoothMessageReceiveErrorHandler {
    val receiveMessageError: MutableLiveEvent<String> = MutableLiveEvent()

    override fun onReceiveBluetoothMessageError() {
        receiveMessageError.postValue("Receive Message Error")
    }

    override fun handleAgribotError() {
        receiveMessageError.postValue("Agribot Error")
    }

    override fun observeMessageReceiveError(): MutableLiveEvent<String> {
        return receiveMessageError
    }
}
