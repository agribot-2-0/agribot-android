package eu.agribot.agribotapp.communication.bluetooth.const

import java.util.UUID

object ConstatntConnectionValue {

    const val MESSAGE_BUFFER_SIZE = 1024
    val AGRIBOT_UUID_SECURE: UUID =
        UUID.fromString("00001101-0000-1000-8000-00805f9b34fb")
    val AGRIBOT_UUID_INSECURE =
        UUID.fromString("8ce255c0-200a-11e0-ac64-0800200c9a63")
}
