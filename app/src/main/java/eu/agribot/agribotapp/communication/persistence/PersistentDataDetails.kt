package eu.agribot.agribotapp.communication.persistence

enum class PersistentDataDetails(val persistentName: String, val data: String, val defaultValue: String) {

    VIDEOSTREAM("videostream_sp", "videostream_persistent_data", "rtmp://10.0.1.2/live"),

    ROBOT_SERIAL_NUMBER("serial_number_sp", "serial_number_data", ""),

    FORECAST_WEATHER_DATA("weather_forecast_sp", "weather_forecast_data", ""),
}
