package eu.agribot.agribotapp.domain.model

interface BaseCloudDatabaseModel<T> {

    fun toCommunicationModel(): T
}
