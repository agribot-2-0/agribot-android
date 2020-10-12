package eu.agribot.agribotapp.domain.model.fromdomain

interface BaseDomainDataModel<T> {
    fun toCommunicationModel(): T
}
