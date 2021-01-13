package eu.agribot.agribotapp.communication.persistence

enum class PersistentDataDetails(val persistentName: String, val data: String) {
    VIDEOSTREAM("videostream_sp", "videostream_persistent_data"),
    BLUETOOTH_ADDRESS("last_used_bt_address_sp", "bluetoothAddress_persistent_data")
}
