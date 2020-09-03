package eu.agribot.agribotapp.communication.bluetooth.model

data class CANmessageData(
    val canAdress: Int,
    val canData: ByteArray
)
