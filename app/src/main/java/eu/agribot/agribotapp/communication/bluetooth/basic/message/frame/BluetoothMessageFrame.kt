package eu.agribot.agribotapp.communication.bluetooth.basic.message.frame

import eu.agribot.agribotapp.communication.bluetooth.basic.message.frame.data.FrameByteSize
import eu.agribot.agribotapp.communication.bluetooth.basic.message.frame.data.FrameByteValue
import eu.agribot.agribotapp.communication.bluetooth.basic.message.frame.data.FrameConstant.ADDRESS_FLAG
import eu.agribot.agribotapp.communication.bluetooth.basic.message.frame.data.FrameConstant.BIT_SHIFT
import eu.agribot.agribotapp.communication.bluetooth.basic.message.frame.data.FrameConstant.CONSTANT_ELEMENTS_OF_FRAME_SIZE
import eu.agribot.agribotapp.communication.bluetooth.basic.message.frame.data.FrameConstant.CONST_ELEMENT_OF_FRAME_SIZE
import eu.agribot.agribotapp.communication.bluetooth.basic.message.frame.data.FrameConstant.DUMMY_BYTE
import eu.agribot.agribotapp.communication.bluetooth.basic.message.frame.data.FrameConstant.EMPTY_ARRAY
import eu.agribot.agribotapp.communication.bluetooth.basic.message.frame.data.FrameConstant.TWO_ELEMENT_ADDRES
import eu.agribot.agribotapp.communication.bluetooth.basic.message.frame.data.FramePositions
import eu.agribot.agribotapp.communication.bluetooth.basic.message.frame.data.StatusCode
import eu.agribot.agribotapp.communication.bluetooth.basic.message.receivedmessage.data.ConstantReceivedMessage.START_OF_FRAME_POSITION
import eu.agribot.agribotapp.communication.bluetooth.model.CANmessageData
import kotlin.experimental.xor

/**
 * Class to construct base bluetooth frame
 */

class BluetoothMessageFrame() {
    var bluetoothMessageArray = ByteArray(EMPTY_ARRAY)

    constructor(canMessageData: CANmessageData) : this() {
        bluetoothMessageArray =
            ByteArray(getWriteMessageSize(getCanDataSize(canMessageData))) { EMPTY_ARRAY.toByte() }

        bluetoothMessageArray[FramePositions.START_OF_FRAME.position] =
            FrameByteValue.START_OF_FRAME.value

        bluetoothMessageArray[FramePositions.LENGTH.position] =
            (getCanDataSize(canMessageData) + CONSTANT_ELEMENTS_OF_FRAME_SIZE).toByte()

        bluetoothMessageArray[FramePositions.COMMAND_BYTE.position] =
            FrameByteValue.COMMAND_BYTE.value

        bluetoothMessageArray[FramePositions.FIRST_BYTE_OF_ADDRESS.position] =
            getFirstWriteMessageAdressElement(canMessageData)

        bluetoothMessageArray[FramePositions.SECOND_BYTE_OF_ADDRESS.position] =
            getSecondWriteMessageAdressElement(canMessageData)

        if (getCanDataSize(canMessageData) > EMPTY_ARRAY) {
            for (x in EMPTY_ARRAY until getCanDataSize(canMessageData))
                bluetoothMessageArray[getCanDataBytePosition(x)] = canMessageData.canData[x]
        }

        bluetoothMessageArray[getChecksumPosition(getLength())] =
            getChecksumByte(bluetoothMessageArray.sliceArray(START_OF_FRAME_POSITION..getLength() + 1))

        bluetoothMessageArray[getEndOfFramePosition(getLength())] =
            FrameByteValue.END_OF_FRAME.value
    }

    private fun getCanDataBytePosition(currentElement: Int): Int {
        return currentElement + FramePositions.START_CAN_DATA.position
    }

    private fun getChecksumPosition(messageLength: Int): Int {
        return messageLength + FrameByteSize.START_OF_FRAME_BYTE.size + FrameByteSize.LENGTH_BYTE.size
    }

    private fun getEndOfFramePosition(messageLength: Int): Int {
        return messageLength + FrameByteSize.START_OF_FRAME_BYTE.size +
                FrameByteSize.LENGTH_BYTE.size +
                FrameByteSize.COMMNAD_BYTE.size
    }

    private fun getConstantFrameELementsSize(): Int {
        return (FrameByteSize.START_OF_FRAME_BYTE.size +
                FrameByteSize.LENGTH_BYTE.size +
                FrameByteSize.COMMNAD_BYTE.size +
                FrameByteSize.ADDRESS_BYTE.size +
                FrameByteSize.CHECKSUM_BYTE.size +
                FrameByteSize.END_OF_FRAME_BYTE.size)
    }

    private fun getCanDataSize(canMessageData: CANmessageData): Int {
        return canMessageData.canData.size
    }

    private fun getWriteMessageSize(canDataFrameSize: Int): Int {
        return canDataFrameSize + getConstantFrameELementsSize()
    }

    private fun getLength(): Int {
        return if (bluetoothMessageArray.isNotEmpty())
            bluetoothMessageArray[FramePositions.LENGTH.position].toInt()
        else
            StatusCode.ERROR_CODE.code
    }

    private fun getFirstWriteMessageAdressElement(canMessageData: CANmessageData): Byte {
        return (canMessageData.canAdress shr BIT_SHIFT).toByte()
    }

    private fun getSecondWriteMessageAdressElement(canMessageData: CANmessageData): Byte {
        return (canMessageData.canAdress and DUMMY_BYTE.toInt()).toByte()
    }

    private fun getChecksumByte(checksumByteArray: ByteArray): Byte {
        var checksum = checksumByteArray[0]
        for (checksumPosition in FramePositions.START_OF_CHECKSUM_ARRAY.position..checksumByteArray.lastIndex) {
            checksum = checksum.xor(checksumByteArray[checksumPosition])
        }
        return checksum
    }

    constructor(receivedData: ByteArray) : this() {
        if (receivedData[START_OF_FRAME_POSITION] == FrameByteValue.START_OF_FRAME.value) {
            val tempArray =
                ByteArray(getReceivedMessageLength(receivedData) + CONST_ELEMENT_OF_FRAME_SIZE)
            for (x in tempArray.indices)
                tempArray[x] = receivedData[x]
            bluetoothMessageArray = tempArray
        }
    }

    private fun getReceivedMessageLength(receivedMessage: ByteArray): Int {
        return receivedMessage[FramePositions.LENGTH.position].toInt()
    }

    fun getReceivedMessageAddress(): Int {
        return if (getLength() > TWO_ELEMENT_ADDRES) {
            getFirstAddresElementFrameReceivedMessage() + getSecondAddresElementFrameReceivedMessage()
        } else
            StatusCode.ERROR_CODE.code
    }

    private fun getFirstAddresElementFrameReceivedMessage(): Int {
        return (bluetoothMessageArray[FramePositions.FIRST_BYTE_OF_ADDRESS.position].toInt() shl BIT_SHIFT)
    }

    private fun getSecondAddresElementFrameReceivedMessage(): Int {
        return if (bluetoothMessageArray[FramePositions.SECOND_BYTE_OF_ADDRESS.position].toInt() < EMPTY_ARRAY) {
            ADDRESS_FLAG + bluetoothMessageArray[FramePositions.SECOND_BYTE_OF_ADDRESS.position]
        } else
            bluetoothMessageArray[FramePositions.SECOND_BYTE_OF_ADDRESS.position].toInt()
    }

    fun getCanDataFromReceivedMessage(): ByteArray {
        return bluetoothMessageArray.sliceArray(
            FramePositions.START_CAN_DATA.position until
                    FramePositions.START_CAN_DATA.position +
                    bluetoothMessageArray[FramePositions.LENGTH.position].toInt() -
                    (FrameByteSize.ADDRESS_BYTE.size + FrameByteSize.DIFFERENCE_BETWEEN_SIZE_AND_LAST_BYTE_POSITION.size)
        )
    }
}
