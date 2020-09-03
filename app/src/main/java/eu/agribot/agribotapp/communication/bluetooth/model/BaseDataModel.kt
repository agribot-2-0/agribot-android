package eu.agribot.agribotapp.communication.bluetooth.model

interface BaseDataModel<T> {
    fun toDomainModel(): T
}