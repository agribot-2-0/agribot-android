package eu.agribot.agribotapp.communication.bluetooth.basic.message.frame

import eu.agribot.agribotapp.communication.bluetooth.basic.message.frame.data.FrameByteSize
import eu.agribot.agribotapp.communication.bluetooth.basic.message.frame.data.FrameConstant.ADDRESS_FLAG
import eu.agribot.agribotapp.communication.bluetooth.basic.message.frame.data.FrameConstant.BIT_SHIFT
import eu.agribot.agribotapp.communication.bluetooth.basic.message.frame.data.FrameConstant.COMMAND_BYTE
import eu.agribot.agribotapp.communication.bluetooth.basic.message.frame.data.FrameConstant.CONSTANT_ELEMENTS_OF_FRAME_SIZE
import eu.agribot.agribotapp.communication.bluetooth.basic.message.frame.data.FrameConstant.CONST_ELEMENT_OF_FRAME_SIZE
import eu.agribot.agribotapp.communication.bluetooth.basic.message.frame.data.FrameConstant.DUMMY_BYTE
import eu.agribot.agribotapp.communication.bluetooth.basic.message.frame.data.FrameConstant.EMPTY_ARRAY
import eu.agribot.agribotapp.communication.bluetooth.basic.message.frame.data.FrameConstant.END_OF_FRAME_BYTE
import eu.agribot.agribotapp.communication.bluetooth.basic.message.frame.data.FrameConstant.START_FRAME_BYTE
import eu.agribot.agribotapp.communication.bluetooth.basic.message.frame.data.FrameConstant.TWO_ELEMENT_ADDRES
import eu.agribot.agribotapp.communication.bluetooth.basic.message.frame.data.FramePositions
import eu.agribot.agribotapp.communication.bluetooth.basic.message.frame.data.StatusCode
import eu.agribot.agribotapp.communication.bluetooth.model.CANmessageData
import kotlin.experimental.xor

/**
 * Class to construct base bluetooth frame
 */

class BluetoothMessageFrame() {
    var bluetoothMessageArray = ByteArray(EMPTY_ARRAY)

    constructor(canMessageData: CANmessageData) : this() {

        bluetoothMessageArray =
            ByteArray(getWriteBluetoothArraySize(getWriteCanDataFrameSize(canMessageData))) { EMPTY_ARRAY.toByte() }

        bluetoothMessageArray[FramePositions.START_OF_FRAME.position] = START_FRAME_BYTE

        bluetoothMessageArray[FramePositions.LENGTH.position] =
            (getWriteCanDataFrameSize(canMessageData) + CONSTANT_ELEMENTS_OF_FRAME_SIZE).toByte()

        bluetoothMessageArray[FramePositions.COMMAND_BYTE.position] = COMMAND_BYTE

        bluetoothMessageArray[FramePositions.FIRST_BYTE_OF_ADRESS.position] =
            getFirstWriteAdressElement(canMessageData)

        bluetoothMessageArray[FramePositions.SECOND_BYTE_OF_ADRESS.position] =
            getSecondWriteAdressElement(canMessageData)

        if (getWriteCanDataFrameSize(canMessageData) > EMPTY_ARRAY) {
            for (x in EMPTY_ARRAY until getWriteCanDataFrameSize(canMessageData))
                bluetoothMessageArray[getWriteCanDataBytePosition(x)] = canMessageData.canData[x]
        }

        bluetoothMessageArray[getChecksumBytePosition()] =
            getChecksumByte(bluetoothMessageArray.sliceArray(FramePositions.START_OF_FRAME.position..getWriteMessageDataLength() + FrameByteSize.DIFFERENCE_BETWEEN_SIZE_AND_POSITION.size))

        bluetoothMessageArray[getEndOfFrameBytePosition()] = END_OF_FRAME_BYTE
    }

    private fun getWriteCanDataFrameSize(canMessageData: CANmessageData): Int {
        return canMessageData.canData.size
    }

    private fun getWriteBluetoothArraySize(canDataFrameSize: Int): Int {
        return canDataFrameSize + getSumOfAllConstantSizeFrameElements()
    }

    private fun getWriteCanDataBytePosition(canDataElementNumber: Int): Int {
        return canDataElementNumber + FrameByteSize.START_OF_FRAME_BYTE.size +
                FrameByteSize.LENGTH_BYTE.size +
                FrameByteSize.COMMNAD_BYTE.size +
                FrameByteSize.ADDRESS_BYTE.size
    }

    private fun getSumOfAllConstantSizeFrameElements(): Int {
        return (FrameByteSize.START_OF_FRAME_BYTE.size +
                FrameByteSize.LENGTH_BYTE.size +
                FrameByteSize.COMMNAD_BYTE.size +
                FrameByteSize.ADDRESS_BYTE.size +
                FrameByteSize.CHECKSUM_BYTE.size +
                FrameByteSize.END_OF_FRAME_BYTE.size
                )
    }

    private fun getChecksumBytePosition(): Int {
        return getWriteMessageDataLength() + FrameByteSize.START_OF_FRAME_BYTE.size +
                FrameByteSize.LENGTH_BYTE.size +
                FrameByteSize.COMMNAD_BYTE.size - FrameByteSize.DIFFERENCE_BETWEEN_SIZE_AND_POSITION.size
    }

    private fun getEndOfFrameBytePosition(): Int {
        return getWriteMessageDataLength() +
                FrameByteSize.START_OF_FRAME_BYTE.size +
                FrameByteSize.LENGTH_BYTE.size +
                FrameByteSize.COMMNAD_BYTE.size +
                FrameByteSize.CHECKSUM_BYTE.size -
                FrameByteSize.DIFFERENCE_BETWEEN_SIZE_AND_POSITION.size
    }

    private fun getWriteMessageDataLength(): Int {
        return if (bluetoothMessageArray.isNotEmpty())
            bluetoothMessageArray[FramePositions.LENGTH.position].toInt()
        else
            StatusCode.ERROR_CODE.code
    }

    private fun getFirstWriteAdressElement(canMessageData: CANmessageData): Byte {
        return (canMessageData.canAdress shl BIT_SHIFT).toByte()
    }

    private fun getSecondWriteAdressElement(canMessageData: CANmessageData): Byte {
        return (canMessageData.canAdress and DUMMY_BYTE.toInt()).toByte()
    }

    private fun getChecksumByte(checksumByteArray: ByteArray): Byte {
        var checksum =
            checksumByteArray[FramePositions.INITIALIZATION_CHECKSUM_BYTE.position]
        for (checksumPosition in FramePositions.START_OF_CHECKSUM_ARRAY.position..checksumByteArray.lastIndex) {
            checksum = checksum.xor(checksumByteArray[checksumPosition])
        }
        return checksum
    }

    constructor(receivedData: ByteArray) : this() {
        if (receivedData[FramePositions.START_OF_FRAME.position] == START_FRAME_BYTE) {
            val tempArray =
                ByteArray(receivedData[FramePositions.LENGTH.position].toInt() + CONST_ELEMENT_OF_FRAME_SIZE)
            for (x in tempArray.indices)
                tempArray[x] = receivedData[x]
            bluetoothMessageArray = tempArray
        }
    }

    fun getAddress(): Int {
        return if (getWriteMessageDataLength() > TWO_ELEMENT_ADDRES) {
            getFirstReceivedAddressElement() + getSecondReceivedAdressElement()
        } else
            StatusCode.ERROR_CODE.code
    }

    private fun getFirstReceivedAddressElement(): Int {
        return (bluetoothMessageArray[FramePositions.FIRST_BYTE_OF_ADRESS.position].toInt() shl BIT_SHIFT)
    }

    private fun getSecondReceivedAdressElement(): Int {
        return if (bluetoothMessageArray[FramePositions.SECOND_BYTE_OF_ADRESS.position].toInt() < EMPTY_ARRAY) {
            ADDRESS_FLAG + bluetoothMessageArray[FramePositions.SECOND_BYTE_OF_ADRESS.position]
        } else
            bluetoothMessageArray[FramePositions.SECOND_BYTE_OF_ADRESS.position].toInt()
    }

    fun getCanDataFromReceivedMessage(): ByteArray {
        return bluetoothMessageArray.sliceArray(FramePositions.START_MESSAGE_DATA.position until getReceivedCanDataEndPosition())
    }

    private fun getReceivedCanDataEndPosition(): Int {
        return FramePositions.START_MESSAGE_DATA.position + bluetoothMessageArray[FramePositions.LENGTH.position].toInt() - (FrameByteSize.ADDRESS_BYTE.size + FrameByteSize.DIFFERENCE_BETWEEN_SIZE_AND_POSITION.size)
    }

}