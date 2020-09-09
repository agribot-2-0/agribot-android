package eu.agribot.agribotapp.communication.bluetooth.error.message.receive

import eu.agribot.agribotapp.common.livedata.MutableLiveEvent

class AndroidBluetoothMessageReceiveErrorHandler : BluetoothMessageReceiveErrorHandler {
    val writeMessageErrors: MutableLiveEvent<String> = MutableLiveEvent()

    override fun onReceiveBluetoothMessageError() {
        writeMessageErrors.postValue("Send Message Error")
    }

    override fun observeMessageReceiveError(): MutableLiveEvent<String> {
        return writeMessageErrors
    }
}
