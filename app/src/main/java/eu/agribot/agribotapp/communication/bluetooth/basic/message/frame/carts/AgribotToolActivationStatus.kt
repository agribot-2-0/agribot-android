package eu.agribot.agribotapp.communication.bluetooth.basic.message.frame.carts

enum class AgribotToolActivationStatus(val active: Byte) {
    ACTIVE(1.toByte()),
    INACTIVE(0.toByte())
}
