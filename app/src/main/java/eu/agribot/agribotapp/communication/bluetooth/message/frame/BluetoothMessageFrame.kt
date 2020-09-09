package eu.agribot.agribotapp.communication.bluetooth.message.frame

import eu.agribot.agribotapp.communication.bluetooth.message.frame.FrameConstant.CONSTANT_FRAME_ELEMENTS_SIZE
import eu.agribot.agribotapp.communication.bluetooth.message.frame.FrameConstant.DUMMY_BYTE
import eu.agribot.agribotapp.communication.bluetooth.message.frame.FrameConstant.EMPTY_ARRAY
import eu.agribot.agribotapp.communication.bluetooth.message.frame.FrameConstant.START_FRAME
import eu.agribot.agribotapp.communication.bluetooth.model.CANmessageData
import kotlin.experimental.xor

/**
 * Class to construct base bluetooth frame
 */

class BluetoothMessageFrame() {
    var bluetoothMessageArray = ByteArray(EMPTY_ARRAY)

    constructor(CANmessageData: CANmessageData) : this() {
        val bluetoothFrameLength = CANmessageData.canData.size
        bluetoothMessageArray =
            ByteArray(bluetoothFrameLength + CONSTANT_FRAME_ELEMENTS_SIZE) { EMPTY_ARRAY.toByte() }
        bluetoothMessageArray[0] = START_FRAME
        bluetoothMessageArray[1] = (bluetoothFrameLength + 3).toByte()
        bluetoothMessageArray[2] = FrameConstant.COMMAND
        bluetoothMessageArray[3] = (CANmessageData.canAdress shr 8).toByte()
        bluetoothMessageArray[4] = (CANmessageData.canAdress and DUMMY_BYTE.toInt()).toByte()
        if (bluetoothFrameLength > EMPTY_ARRAY) {
            for (x in EMPTY_ARRAY until bluetoothFrameLength)
                bluetoothMessageArray[x + 5] = CANmessageData.canData[x]
        }
        countChecksum()
    }

    constructor(receivedData: ByteArray) : this() {
        if (receivedData[0] == START_FRAME) {
            val length = receivedData[1].toInt()
            val newArray = ByteArray(length + 4)
            for (x in newArray.indices)
                newArray[x] = receivedData[x]
            bluetoothMessageArray = newArray
        }
    }

    private fun getLength(): Int {
        return if (bluetoothMessageArray.isNotEmpty())
            bluetoothMessageArray[1].toInt()
        else
            StatusCode.ERROR_CODE.code
    }

    fun getAddress(): Int {
        return if (getLength() > 2) {
            val firstElementAdressCode = (bluetoothMessageArray[3].toInt() * 256)
            val secondElementAdressCode: Int = if (bluetoothMessageArray[4].toInt() < EMPTY_ARRAY) {
                256 + bluetoothMessageArray[4]
            } else
                bluetoothMessageArray[4].toInt()
            firstElementAdressCode + secondElementAdressCode
        } else
            StatusCode.ERROR_CODE.code
    }

    fun getMessage(): ByteArray {
        val len = bluetoothMessageArray[1].toInt() - 3
        return bluetoothMessageArray.sliceArray(5..(5 + len))
    }

    private fun countChecksum() {
        var checksum = START_FRAME
        val length = bluetoothMessageArray[1]
        for (x in 1..length + 1) {
            checksum = checksum.xor(bluetoothMessageArray[x])
        }
        bluetoothMessageArray[length + 2] = checksum
        bluetoothMessageArray[length + 3] = 0x0D
    }
}
