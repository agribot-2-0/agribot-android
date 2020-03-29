package eu.agribot.agribotapp.communication.persistence

/**
 * Interface used to store key-value data
 * @see SharedPreferencesPersistentStorage
 */
interface PersistentStorage {

    // TODO: Remove sample methods
    fun getExampleEntry(): String

    fun saveExampleEntry(entry: String)
}
