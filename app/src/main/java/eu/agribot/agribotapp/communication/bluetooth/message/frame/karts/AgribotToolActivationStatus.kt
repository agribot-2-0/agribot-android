package eu.agribot.agribotapp.communication.bluetooth.message.frame.karts

enum class AgribotToolActivationStatus(val active: Byte) {
    ACTIVE(1.toByte()),
    INACTIVE(0.toByte())
}
