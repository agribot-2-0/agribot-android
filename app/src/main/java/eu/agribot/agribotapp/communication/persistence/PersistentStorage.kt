package eu.agribot.agribotapp.communication.persistence

/**
 * Interface used to store key-value data
 * @see SharedPreferencesPersistentStorage
 */
interface PersistentStorage {

    fun saveVideoStreamUrl(url: String)

    fun getVideoStreamUrl(): String

    fun saveRobotSerialNumber(serial: String)

    fun getRobotSerialNumber(): String

    fun saveWeatherForecast(serial: String)

    fun getWeatherForecast(): String
}
