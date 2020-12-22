package eu.agribot.agribotapp.communication.database.clouddatabase.model

interface BaseCloudDatabaseModel<T> {

    fun toDomainModel(): T
}
