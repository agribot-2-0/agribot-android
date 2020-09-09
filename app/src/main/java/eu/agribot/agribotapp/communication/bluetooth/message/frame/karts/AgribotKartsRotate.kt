package eu.agribot.agribotapp.communication.bluetooth.message.frame.karts

enum class AgribotKartsRotate(val rotate: Byte) {
    LEFT(1.toByte()),
    RIGHT(0.toByte())
}
