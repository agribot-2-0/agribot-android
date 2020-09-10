package eu.agribot.agribotapp.communication.bluetooth.error.message.write

import eu.agribot.agribotapp.common.livedata.MutableLiveEvent

class AndroidBluetoothMessageWriteErrorHandler() :
    BluetoothMessageWriteErrorHandler {
    val writeMessageError: MutableLiveEvent<String> = MutableLiveEvent()

    override fun onHeartBeatError() {
        writeMessageError.postValue("Heartbeat Error")
    }

    override fun onWriteMessageError() {
        writeMessageError.postValue("Send message Error")
    }

    override fun observeWriteMessageError(): MutableLiveEvent<String> {
        return writeMessageError
    }
}
