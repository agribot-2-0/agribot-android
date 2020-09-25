package eu.agribot.agribotapp.communication.bluetooth.basic.connection.message.frame.carts

enum class AgribotCartsRotate(val rotate: Byte) {
    LEFT(1.toByte()),
    RIGHT(0.toByte())
}
