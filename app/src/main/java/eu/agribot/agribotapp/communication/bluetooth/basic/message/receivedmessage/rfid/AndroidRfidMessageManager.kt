package eu.agribot.agribotapp.communication.bluetooth.basic.message.receivedmessage.rfid

import eu.agribot.agribotapp.communication.bluetooth.basic.message.receivedmessage.rfid.data.RfidMessageParameters.RFID_DATA_SIZE
import eu.agribot.agribotapp.communication.bluetooth.basic.message.receivedmessage.rfid.data.RfidManagerParametersInitializationValue
import eu.agribot.agribotapp.communication.bluetooth.basic.message.receivedmessage.rfid.data.RfidMessageBytePosition
import eu.agribot.agribotapp.communication.bluetooth.basic.message.receivedmessage.rfid.data.RfidMessageParameters
import eu.agribot.agribotapp.communication.bluetooth.model.fromcommunication.RfidDataBD
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import java.nio.ByteBuffer

class AndroidRfidMessageManager : RfidMessageManager {

    private var rfidMessageCounter =
        RfidManagerParametersInitializationValue.RFID_MESSAGE_COUNTER_INITIALIZATION_VALUE.value
    private var rfidMessageArray =
        ByteArray(RfidManagerParametersInitializationValue.RFID_DATA_ARRAY_INITIALIZATION_VALUE.value)
    private var antennaNumber: Int =
        RfidManagerParametersInitializationValue.RFID_ANTENNA_NUMBER_INITIALIZATION_VALUE.value
    private var endOfRfIdPosition: Int =
        RfidManagerParametersInitializationValue.RFID_END_OF_FRAME_BYTE_INITIALIZATION_VALUE.value

    @ExperimentalCoroutinesApi
    private val rfidDataBDFlow = MutableStateFlow<RfidDataBD?>(null)

    @ExperimentalCoroutinesApi
    override fun handleRfidMessage(rfidMessage: ByteArray) {
        if (isRfidInformationMessage(rfidMessage)) {
            setRfidTagInformation(rfidMessage)
            if (endOfRfIdPosition <= RfidMessageParameters.RFID_NO_RFID_TAG.parameter) {
                appendRfidData(
                    RfidDataBD(
                        RfidMessageParameters.RFID_NO_RFID_TAG.parameter,
                        ByteArray(RfidMessageParameters.RFID_NO_RFID_TAG.parameter)
                    )
                )
            }
        } else {
            decrementRfidMessageCounter()
            if (rfidMessageCounter != RfidMessageParameters.COMPLETE_MESSAGE_COUNTER_VALUE.parameter && !isRfidInformationMessage(
                    rfidMessage
                )
            ) {
                mergeRfidArray(rfidMessage.sliceArray(RfidMessageBytePosition.START_OF_RFID_TAG_DATA_MESSAGE.position..RfidMessageBytePosition.END_OF_RFID_TAG_DATA_MESSAGE.position))
            } else {
                mergeRfidArray(rfidMessage.sliceArray(RfidMessageBytePosition.START_OF_RFID_TAG_DATA_MESSAGE.position..RfidMessageBytePosition.END_OF_RFID_TAG_DATA_MESSAGE.position))
                appendRfidData(
                    RfidDataBD(
                        antennaNumber,
                        rfidMessageArray.sliceArray(RfidMessageBytePosition.START_RFID_ARRAY_POSITION.position..endOfRfIdPosition)
                    )
                )
            }
        }
    }

    // ONLY FOR DEVELOP
    /*
    private fun logRfidMessage(message: ByteArray, messageName: String) {
        var mymessage = ""
        message.forEach {
            mymessage = mymessage + String.format("%02X", it) + " "
        }
        Timber.e("%s%s", messageName, mymessage)
    }

     */

    private fun setRfidTagInformation(rfidTagMessage: ByteArray) {
        if (isRfidInformationMessage(rfidTagMessage)) {
            rfidMessageArray =
                ByteArray(RfidMessageParameters.RFID_INITIALIZATION_ARRAY_SIZE.parameter)
            rfidMessageCounter = getMessageLength(rfidTagMessage)
            antennaNumber = getRfidAntennaNumber(rfidTagMessage)
            endOfRfIdPosition = getEndOfRfidTagBytePosition(rfidTagMessage)
        }
    }

    private fun getEndOfRfidTagBytePosition(rfidMessage: ByteArray): Int {
        return rfidMessage[RfidMessageBytePosition.RFID_MESSAGE_LENGTH.position].toInt() - RfidMessageParameters.DIFFERENCE_BETWEEN_SIZE_AND_LAST_ARRAY_POSITION.parameter
    }

    private fun isRfidInformationMessage(rfidMessage: ByteArray): Boolean {
        return rfidMessage[RfidMessageBytePosition.RFID_COM_NUMBER.position].toInt() == RfidMessageParameters.RFID_INFORMATION_MESSAGE.parameter
    }

    private fun getMessageLength(rfidTagMessage: ByteArray): Int {
        return if (rfidTagMessage[RfidMessageBytePosition.RFID_MESSAGE_LENGTH.position].toInt() % RFID_DATA_SIZE.parameter != RfidMessageParameters.RFID_ONE_MESSAGE_DATA.parameter) {
            (rfidTagMessage[RfidMessageBytePosition.RFID_MESSAGE_LENGTH.position].toInt() / RFID_DATA_SIZE.parameter) + RfidMessageParameters.DIFFERENCE_BETWEEN_SIZE_AND_LAST_ARRAY_POSITION.parameter
        } else {
            (rfidTagMessage[RfidMessageBytePosition.RFID_MESSAGE_LENGTH.position].toInt() / RFID_DATA_SIZE.parameter)
        }
    }

    private fun getRfidAntennaNumber(rfidTagMessage: ByteArray): Int {
        return rfidTagMessage[RfidMessageBytePosition.RFID_ANTENNA_NUMBER_BYTE.position].toInt()
    }

    private fun decrementRfidMessageCounter() {
        rfidMessageCounter -= RfidMessageParameters.DECREMENT_COUNTER_VALUE.parameter
    }

    private fun mergeRfidArray(nextRfidArrayElement: ByteArray): ByteArray {
        rfidMessageArray = ByteBuffer.allocate(rfidMessageArray.size + nextRfidArrayElement.size)
            .put(rfidMessageArray).put(nextRfidArrayElement).array()
        return rfidMessageArray
    }

    @ExperimentalCoroutinesApi
    private fun appendRfidData(rfidTagIDData: RfidDataBD) {
        rfidDataBDFlow.value = rfidTagIDData
    }

    @ExperimentalCoroutinesApi
    override fun observeRfidMessage(): MutableStateFlow<RfidDataBD?> {
        return rfidDataBDFlow
    }

    @ExperimentalCoroutinesApi
    override fun clearRfidDataFlow() {
        rfidDataBDFlow.value = null
    }
}
