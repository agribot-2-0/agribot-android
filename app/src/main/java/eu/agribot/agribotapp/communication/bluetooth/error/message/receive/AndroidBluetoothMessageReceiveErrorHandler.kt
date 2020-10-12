package eu.agribot.agribotapp.communication.bluetooth.error.message.receive

import eu.agribot.agribotapp.communication.bluetooth.error.BluetothErrorCode.BLUETOOTH_RECEIVE_MESSAGE_CODE
import eu.agribot.agribotapp.communication.bluetooth.error.BluetothErrorCode.ERROR_FROM_AGRIBOT_CODE
import eu.agribot.agribotapp.communication.bluetooth.model.fromcommunication.BluetoothErrorInfoBD
import eu.agribot.agribotapp.domain.model.todomain.BluetoothErrorInfo

class AndroidBluetoothMessageReceiveErrorHandler :
    BluetoothMessageReceiveErrorHandler {
    private var receiveMessageError: BluetoothErrorInfoBD? = null

    override fun onReceiveBluetoothMessageError() {
        receiveMessageError = BluetoothErrorInfoBD(BLUETOOTH_RECEIVE_MESSAGE_CODE)
    }

    override fun handleAgribotError() {
        receiveMessageError = BluetoothErrorInfoBD(ERROR_FROM_AGRIBOT_CODE)
    }

    override fun getMessageReceiveError(): BluetoothErrorInfo? {
        return receiveMessageError?.toDomainModel()
    }
}
