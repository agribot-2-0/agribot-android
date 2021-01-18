package eu.agribot.agribotapp.communication.bluetooth.basic.message.receivedmessage.rfid

import android.util.Log
import eu.agribot.agribotapp.communication.bluetooth.basic.message.receivedmessage.rfid.RfidMessageObject.RFID_DATA_SIZE
import eu.agribot.agribotapp.communication.bluetooth.basic.message.receivedmessage.rfid.RfidMessageObject.RFID_INFORMATION_MESSAGE
import eu.agribot.agribotapp.communication.bluetooth.basic.message.receivedmessage.rfid.RfidMessageObject.RFID_MESSAGE_FORMAT
import eu.agribot.agribotapp.communication.bluetooth.basic.message.receivedmessage.rfid.RfidMessageObject.RFID_ONE_MESSAGE_DATA
import eu.agribot.agribotapp.communication.bluetooth.model.fromcommunication.RfidDataBD
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import java.nio.ByteBuffer

class AndroidRfidMessageManager : RfidMessageManager {

    var rfidMessageCounter = -1
    var rfidMessageArray = ByteArray(0)
    var antennaNumber: Int = -1
    var endOfRfIdPosition: Int = -1

    @ExperimentalCoroutinesApi
    private val rfidDataBDFlow = MutableStateFlow<RfidDataBD?>(null)

    @ExperimentalCoroutinesApi
    override fun handleRfidMessage(rfidMessage: ByteArray) {
        logRfidMessage(rfidMessage, "received message: ")
        if (isRfidInformationMessage(rfidMessage)) {
            setRfidTagInformation(rfidMessage)
        } else {
            decrementRfidMessageCounter()
            Log.e("xd", "message counter: " + rfidMessageCounter)

            if (rfidMessageCounter != 0 && !isRfidInformationMessage(rfidMessage)) {
                mergeRfidArray(rfidMessage.sliceArray(RfidMessageBytePosition.START_OF_RFID_TAG_DATA_MESSAGE.position..RfidMessageBytePosition.END_OF_RFID_TAG_DATA_MESSAGE.position))
            } else {
                Log.e("xd", "bytearray size : " + rfidMessageArray.size)
                mergeRfidArray(rfidMessage.sliceArray(RfidMessageBytePosition.START_OF_RFID_TAG_DATA_MESSAGE.position..RfidMessageBytePosition.END_OF_RFID_TAG_DATA_MESSAGE.position))
                appendRfidData(
                    RfidDataBD(
                        antennaNumber,
                        formatTagIDToHexString(rfidMessageArray.sliceArray(0..endOfRfIdPosition)).toString()
                    )
                )
            }
        }
    }

    private fun logRfidMessage(message: ByteArray, messageName: String) {
        var mymessage = ""
        message.forEach {
            mymessage = mymessage + String.format("%02X", it) + " "
        }
        Log.e("xd", messageName + mymessage)
    }

    private fun setRfidTagInformation(rfidTagMessage: ByteArray) {
        if (isRfidInformationMessage(rfidTagMessage)) {
            rfidMessageArray = ByteArray(0)
            rfidMessageCounter = getMessageLength(rfidTagMessage)
            antennaNumber = getRfidAntennaNumber(rfidTagMessage)
            endOfRfIdPosition = getEndOfRfidTagBytePosition(rfidTagMessage)
        }
    }

    private fun getEndOfRfidTagBytePosition(rfidMessage: ByteArray): Int {
        return rfidMessage[2].toInt() - 1
    }

    private fun isRfidInformationMessage(rfidMessage: ByteArray): Boolean {
        return rfidMessage[RfidMessageBytePosition.RFID_COM_NUMBER.position].toInt() == RFID_INFORMATION_MESSAGE
    }

    private fun getMessageLength(rfidTagMessage: ByteArray): Int {
        return if (rfidTagMessage[RfidMessageBytePosition.RFID_MESSAGE_LENGTH.position].toInt() % RFID_DATA_SIZE != RFID_ONE_MESSAGE_DATA) {
            (rfidTagMessage[RfidMessageBytePosition.RFID_MESSAGE_LENGTH.position].toInt() / RFID_DATA_SIZE) + 1
        } else {
            (rfidTagMessage[RfidMessageBytePosition.RFID_MESSAGE_LENGTH.position].toInt() / RFID_DATA_SIZE)
        }
    }

    private fun getRfidAntennaNumber(rfidTagMessage: ByteArray): Int {
        return rfidTagMessage[RfidMessageBytePosition.RFID_ANTENNA_NUMBER_BYTE.position].toInt()
    }

    private fun decrementRfidMessageCounter() {
        rfidMessageCounter -= 1
    }

    private fun mergeRfidArray(nextRfidArrayElement: ByteArray): ByteArray {
        logRfidMessage(nextRfidArrayElement, "array to merge : ")
        logRfidMessage(nextRfidArrayElement, "before merge: ")
        rfidMessageArray = ByteBuffer.allocate(rfidMessageArray.size + nextRfidArrayElement.size)
            .put(rfidMessageArray).put(nextRfidArrayElement).array()
        logRfidMessage(rfidMessageArray, "after merge: ")
        return rfidMessageArray
    }

    private fun formatTagIDToHexString(tagIDArray: ByteArray): String {
        var rfidTagHexString = ""
        tagIDArray.forEach {
            rfidTagHexString += String.format(RFID_MESSAGE_FORMAT, it) + " | "
        }
        return rfidTagHexString
    }

    @ExperimentalCoroutinesApi
    private fun appendRfidData(rfidTagIDData: RfidDataBD) {
        rfidDataBDFlow.value = rfidTagIDData
    }

    @ExperimentalCoroutinesApi
    override fun observeRfidMessage(): MutableStateFlow<RfidDataBD?> {
        return rfidDataBDFlow
    }
}
