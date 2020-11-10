package eu.agribot.agribotapp.domain.model.cdb

interface BaseCloudDatabaseModel<T> {

    fun toCommunicationModel(): T
}
