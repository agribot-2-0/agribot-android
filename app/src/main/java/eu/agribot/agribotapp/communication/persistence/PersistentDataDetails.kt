package eu.agribot.agribotapp.communication.persistence

enum class PersistentDataDetails(val persistentName: String, val data: String, val defaultValue : String) {
    VIDEOSTREAM("videostream_sp", "videostream_persistent_data","rtmp://192.168.0.139/live"),
    BLUETOOTH_ADDRESS("last_used_bt_address_sp", "bluetoothAddress_persistent_data","")
}
