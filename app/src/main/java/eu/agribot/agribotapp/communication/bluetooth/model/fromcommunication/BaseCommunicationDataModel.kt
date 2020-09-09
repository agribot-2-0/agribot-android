package eu.agribot.agribotapp.communication.bluetooth.model.fromcommunication

interface BaseCommunicationDataModel<T> {
    fun toDomainModel(): T
}
