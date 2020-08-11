package eu.agribot.agribotapp.domain.model

import android.annotation.SuppressLint
import android.util.Log
import kotlin.experimental.xor

class BluetoothMessageModel() {

    companion object {
        const val CMD_ONE = 0x00.toByte()
    }

    constructor(command: Byte, address: Int, message: ByteArray) : this() {
        val length = message.size
        byteArray = ByteArray(length + 7) { 0.toByte() }
        byteArray[0] = 0x43
        byteArray[1] = (length + 3).toByte()
        byteArray[2] = command
        byteArray[3] = (address shr 8).toByte()
        byteArray[4] = (address and 0xFF).toByte()
        if (length > 0) {
            for (x in 0 until length)
                byteArray[x + 5] = message[x]
        }
        countChecksum()
    }

    constructor(address: Int, message: ByteArray) : this(CMD_ONE, address, message)

    constructor(array: ByteArray) : this() {
        if (array[0] == 67.toByte()) {
            val length = array[1].toInt()
            /*if(length + 4 < 0)
                length = 0*/
            val newArray = ByteArray(length + 4)
            for (x in newArray.indices)
                newArray[x] = array[x]
            byteArray = newArray
        }
    }

    var byteArray = ByteArray(0)

    private fun getLength(): Int {
        return if (byteArray.isNotEmpty())
            byteArray[1].toInt()
        else
            -1
    }

    private fun getAllMessageInHex(): String {
        var message = ""
        for (element in byteArray)
            message += " " + String.format("%02X", element)
        return message
    }

    fun getAddress(): Int {
        return if (getLength() > 2) {
            val first = (byteArray[3].toInt() * 256)
            val second: Int = if (byteArray[4].toInt() < 0) {
                256 + byteArray[4]
            } else
                byteArray[4].toInt()
            first + second
        } else
            -1
    }

    fun getMessage(): ByteArray {
        val len = byteArray[1].toInt() - 3
        return byteArray.sliceArray(5..(5 + len))
    }

    fun getMessageFromFrame(): String {
        var message = ""
        for (x in 5..byteArray.size - 3)
            message += " " + byteArray[x].toString()
        return message
    }

    private fun getAllMessage(): String {
        var message = ""
        for (x in 0 until byteArray.size)
            message += " " + byteArray[x].toString()
        return message
    }

    private fun countChecksum() {
        var checksum = 0x43.toByte()
        val length = byteArray[1]
        for (x in 1..length + 1) {
            checksum = checksum.xor(byteArray[x])
        }
        byteArray[length + 2] = checksum
        byteArray[length + 3] = 0x0D
    }
<<<<<<< HEAD

    @SuppressLint("LogNotTimber")
    fun logBtFrame() {
        Log.e(
            "xd",
            " frame in hex:" + getAllMessageInHex()
        )
    }

    infix fun Byte.shl(shift: Int): Byte = (this.toInt() shl shift).toByte()

    fun getCommand(): Int {
        return if (byteArray.size > 2)
            byteArray[2].toInt()
        else
            0
    }
=======
>>>>>>> 0bd8dbd... static code analyze fix
}
