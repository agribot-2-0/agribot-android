package eu.agribot.agribotapp.communication.bluetooth.basic.message.receivedmessage

enum class MinimumMessageLength(val size: Int) {

    HYDRAULIC_DATA(2),

    DIAGNOSTIC_DATA(7)
}
