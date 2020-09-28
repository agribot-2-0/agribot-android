package eu.agribot.agribotapp.communication.bluetooth.basic.message.frame

import eu.agribot.agribotapp.communication.bluetooth.basic.message.frame.FrameConstant.ADDRESS_FLAG
import eu.agribot.agribotapp.communication.bluetooth.basic.message.frame.FrameConstant.BIT_SHIFT
import eu.agribot.agribotapp.communication.bluetooth.basic.message.frame.FrameConstant.COMMAND_BYTE
import eu.agribot.agribotapp.communication.bluetooth.basic.message.frame.FrameConstant.CONSTANT_ELEMENTS_OF_FRAME_SIZE
import eu.agribot.agribotapp.communication.bluetooth.basic.message.frame.FrameConstant.CONSTANT_FRAME_ELEMENTS_SIZE
import eu.agribot.agribotapp.communication.bluetooth.basic.message.frame.FrameConstant.CONST_ELEMENT_OF_FRAME_SIZE
import eu.agribot.agribotapp.communication.bluetooth.basic.message.frame.FrameConstant.DUMMY_BYTE
import eu.agribot.agribotapp.communication.bluetooth.basic.message.frame.FrameConstant.EMPTY_ARRAY
import eu.agribot.agribotapp.communication.bluetooth.basic.message.frame.FrameConstant.END_OF_FRAME_BYTE
import eu.agribot.agribotapp.communication.bluetooth.basic.message.frame.FrameConstant.START_FRAME_BYTE
import eu.agribot.agribotapp.communication.bluetooth.basic.message.frame.FrameConstant.TWO_ELEMENT_ADDRES
import eu.agribot.agribotapp.communication.bluetooth.basic.message.frame.FramePositions.COMMAND_BYTE_POSITION
import eu.agribot.agribotapp.communication.bluetooth.basic.message.frame.FramePositions.FIRST_BYTE_OF_ADRESS_POSITION
import eu.agribot.agribotapp.communication.bluetooth.basic.message.frame.FramePositions.LENGTH_POSITION
import eu.agribot.agribotapp.communication.bluetooth.basic.message.frame.FramePositions.SECOND_BYTE_OF_ADRESS_POSITION
import eu.agribot.agribotapp.communication.bluetooth.basic.message.frame.FramePositions.START_OF_FRAME_POSITION
import eu.agribot.agribotapp.communication.bluetooth.model.CANmessageData
import kotlin.experimental.xor

/**
 * Class to construct base bluetooth frame
 */

class BluetoothMessageFrame() {
    var bluetoothMessageArray = ByteArray(EMPTY_ARRAY)

    constructor(canMessageData: CANmessageData) : this() {
        val canDataFrameSize = canMessageData.canData.size
        bluetoothMessageArray =
            ByteArray(canDataFrameSize + CONSTANT_FRAME_ELEMENTS_SIZE) { EMPTY_ARRAY.toByte() }

        bluetoothMessageArray[START_OF_FRAME_POSITION] = START_FRAME_BYTE

        bluetoothMessageArray[LENGTH_POSITION] =
            (canDataFrameSize + CONSTANT_ELEMENTS_OF_FRAME_SIZE).toByte()

        bluetoothMessageArray[COMMAND_BYTE_POSITION] = COMMAND_BYTE

        bluetoothMessageArray[FIRST_BYTE_OF_ADRESS_POSITION] =
            getFirstAdressElement(canMessageData)
        bluetoothMessageArray[SECOND_BYTE_OF_ADRESS_POSITION] =
            getSecondAdressElement(canMessageData)

        if (canDataFrameSize > EMPTY_ARRAY) {
            for (x in EMPTY_ARRAY until canDataFrameSize)
                bluetoothMessageArray[x + 5] = canMessageData.canData[x]
        }
        bluetoothMessageArray[getLength() + 2] =
            getChecksumByte(bluetoothMessageArray.sliceArray(START_OF_FRAME_POSITION..getLength() + 1))
        bluetoothMessageArray[getLength() + 3] = END_OF_FRAME_BYTE
    }

    private fun getLength(): Int {
        return if (bluetoothMessageArray.isNotEmpty())
            bluetoothMessageArray[LENGTH_POSITION].toInt()
        else
            StatusCode.ERROR_CODE.code
    }

    private fun getFirstAdressElement(canMessageData: CANmessageData): Byte {
        return (canMessageData.canAdress shr BIT_SHIFT).toByte()
    }

    private fun getSecondAdressElement(canMessageData: CANmessageData): Byte {
        return (canMessageData.canAdress and DUMMY_BYTE.toInt()).toByte()
    }

    private fun getChecksumByte(checksumByteArray: ByteArray): Byte {
        var checksum = checksumByteArray[0]
        for (checksumPosition in 1..checksumByteArray.lastIndex) {
            checksum = checksum.xor(checksumByteArray[checksumPosition])
        }
        return checksum
    }

    constructor(receivedData: ByteArray) : this() {
        if (receivedData[START_OF_FRAME_POSITION] == START_FRAME_BYTE) {
            val length = receivedData[LENGTH_POSITION].toInt()
            val tempArray = ByteArray(length + CONST_ELEMENT_OF_FRAME_SIZE)
            for (x in tempArray.indices)
                tempArray[x] = receivedData[x]
            bluetoothMessageArray = tempArray
        }
    }

    fun getAddress(): Int {
        return if (getLength() > TWO_ELEMENT_ADDRES) {
            val firstElementAdressCode =
                (bluetoothMessageArray[FIRST_BYTE_OF_ADRESS_POSITION].toInt() shr BIT_SHIFT)
            val secondElementAdressCode: Int =
                if (bluetoothMessageArray[SECOND_BYTE_OF_ADRESS_POSITION].toInt() < EMPTY_ARRAY) {
                    ADDRESS_FLAG + bluetoothMessageArray[SECOND_BYTE_OF_ADRESS_POSITION]
                } else
                    bluetoothMessageArray[SECOND_BYTE_OF_ADRESS_POSITION].toInt()
            firstElementAdressCode + secondElementAdressCode
        } else
            StatusCode.ERROR_CODE.code
    }

    fun getMessage(): ByteArray {
        val len = bluetoothMessageArray[LENGTH_POSITION].toInt() - 3
        return bluetoothMessageArray.sliceArray(5..(5 + len))
    }
}
