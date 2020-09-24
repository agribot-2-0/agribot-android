package eu.agribot.agribotapp.communication.bluetooth.error.message.write

import eu.agribot.agribotapp.communication.bluetooth.error.BluetothErrorCode.BLUETOOTH_HEARTBEAT_ERROR_CODE
import eu.agribot.agribotapp.communication.bluetooth.error.BluetothErrorCode.BLUETOOTH_WRITE_MESSAGE_ERROR_CODE
import eu.agribot.agribotapp.communication.bluetooth.model.fromcommunication.BluetoothErrorInfoBD

class AndroidBluetoothMessageWriteErrorHandler :
    BluetoothMessageWriteErrorHandler {
    private var writeMessageError: BluetoothErrorInfoBD? = null

    override fun onHeartBeatError() {
        writeMessageError = BluetoothErrorInfoBD(BLUETOOTH_HEARTBEAT_ERROR_CODE)
    }

    override fun onWriteMessageError() {
        writeMessageError = BluetoothErrorInfoBD(BLUETOOTH_WRITE_MESSAGE_ERROR_CODE)
    }

    override fun getMessageWriteError(): BluetoothErrorInfoBD? {
        return writeMessageError
    }
}
