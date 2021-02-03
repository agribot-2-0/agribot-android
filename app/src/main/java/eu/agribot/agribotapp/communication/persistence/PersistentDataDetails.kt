package eu.agribot.agribotapp.communication.persistence

enum class PersistentDataDetails(val persistentName: String, val data: String, val defaultValue: String) {

    VIDEOSTREAM("videostream_sp", "videostream_persistent_data", "rtmp://10.0.1.2/live"),
}
