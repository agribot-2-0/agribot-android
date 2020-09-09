package eu.agribot.agribotapp.communication.bluetooth.error.message.write

import eu.agribot.agribotapp.common.livedata.MutableLiveEvent

class AndroidBluetoothMessageWriteErrorHandler : BluetoothMessageWriteErrorHandler {
    val receiveMessageErrors: MutableLiveEvent<String> = MutableLiveEvent()

    override fun onWriteMessageError() {
        receiveMessageErrors.postValue("on Send Message Error")
    }

    override fun observeWriteMessageError(): MutableLiveEvent<String> {
        return receiveMessageErrors
    }
}
