package eu.agribot.agribotapp.communication.bluetooth.message.frame

import eu.agribot.agribotapp.communication.bluetooth.message.frame.FrameConstant.COMMAND_BYTE
import eu.agribot.agribotapp.communication.bluetooth.message.frame.FrameConstant.CONSTANT_ELEMENTS_OF_FRAME_SIZE
import eu.agribot.agribotapp.communication.bluetooth.message.frame.FrameConstant.CONSTANT_FRAME_ELEMENTS_SIZE
import eu.agribot.agribotapp.communication.bluetooth.message.frame.FrameConstant.DUMMY_BYTE
import eu.agribot.agribotapp.communication.bluetooth.message.frame.FrameConstant.EMPTY_ARRAY
import eu.agribot.agribotapp.communication.bluetooth.message.frame.FrameConstant.END_OF_FRAME_BYTE
import eu.agribot.agribotapp.communication.bluetooth.message.frame.FrameConstant.START_FRAME_BYTE
import eu.agribot.agribotapp.communication.bluetooth.message.frame.FramePositions.COMMAND_BYTE_POSITION
import eu.agribot.agribotapp.communication.bluetooth.message.frame.FramePositions.FIRST_BYTE_OF_ADRESS_POSITION
import eu.agribot.agribotapp.communication.bluetooth.message.frame.FramePositions.LENGTH_POSITION
import eu.agribot.agribotapp.communication.bluetooth.message.frame.FramePositions.NEXT_POSITION
import eu.agribot.agribotapp.communication.bluetooth.message.frame.FramePositions.SECOND_BYTE_OF_ADRESS_POSITION
import eu.agribot.agribotapp.communication.bluetooth.message.frame.FramePositions.START_OF_FRAME_POSITION
import eu.agribot.agribotapp.communication.bluetooth.model.CANmessageData
import kotlin.experimental.xor

/**
 * Class to construct base bluetooth frame
 */

class BluetoothMessageFrame() {
    var bluetoothMessageArray = ByteArray(EMPTY_ARRAY)
    var checksumPosition = 0
    var endOfFramePosition = 0

    constructor(CANmessageData: CANmessageData) : this() {
        val canDataFrameSize = CANmessageData.canData.size
        bluetoothMessageArray =
            ByteArray(canDataFrameSize + CONSTANT_FRAME_ELEMENTS_SIZE) { EMPTY_ARRAY.toByte() }

        bluetoothMessageArray[START_OF_FRAME_POSITION] = START_FRAME_BYTE

        bluetoothMessageArray[LENGTH_POSITION] =
            (canDataFrameSize + CONSTANT_ELEMENTS_OF_FRAME_SIZE).toByte()

        bluetoothMessageArray[COMMAND_BYTE_POSITION] = COMMAND_BYTE

        bluetoothMessageArray[FIRST_BYTE_OF_ADRESS_POSITION] =
            (CANmessageData.canAdress shr 8).toByte()

        bluetoothMessageArray[SECOND_BYTE_OF_ADRESS_POSITION] =
            (CANmessageData.canAdress and DUMMY_BYTE.toInt()).toByte()

        if (canDataFrameSize > EMPTY_ARRAY) {
            for (x in EMPTY_ARRAY until canDataFrameSize)
                bluetoothMessageArray[x + 5] = CANmessageData.canData[x]
        }
        getChecksumByte()
        getEndOfFrameByte()
    }

    private fun getLength(): Int {
        return if (bluetoothMessageArray.isNotEmpty())
            bluetoothMessageArray[LENGTH_POSITION].toInt()
        else
            StatusCode.ERROR_CODE.code
    }

    private fun getChecksumByte() {
        var checksum = START_FRAME_BYTE
        for (checksumPosition in LENGTH_POSITION..bluetoothMessageArray[LENGTH_POSITION] + NEXT_POSITION) {
            checksum = checksum.xor(bluetoothMessageArray[checksumPosition])
        }
        checksumPosition = bluetoothMessageArray[LENGTH_POSITION] + 2
        bluetoothMessageArray[checksumPosition] = checksum
        endOfFramePosition = checksumPosition + NEXT_POSITION
    }

    private fun getEndOfFrameByte() {
        bluetoothMessageArray[endOfFramePosition] = END_OF_FRAME_BYTE
    }

    constructor(receivedData: ByteArray) : this() {
        if (receivedData[START_OF_FRAME_POSITION] == START_FRAME_BYTE) {
            val length = receivedData[LENGTH_POSITION].toInt()
            val tempArray = ByteArray(length + 4)
            for (x in tempArray.indices)
                tempArray[x] = receivedData[x]
            bluetoothMessageArray = tempArray
        }
    }

    fun getAddress(): Int {
        return if (getLength() > 2) {
            val firstElementAdressCode =
                (bluetoothMessageArray[FIRST_BYTE_OF_ADRESS_POSITION].toInt() shr 8)
            val secondElementAdressCode: Int =
                if (bluetoothMessageArray[SECOND_BYTE_OF_ADRESS_POSITION].toInt() < EMPTY_ARRAY) {
                    256 + bluetoothMessageArray[SECOND_BYTE_OF_ADRESS_POSITION]
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
