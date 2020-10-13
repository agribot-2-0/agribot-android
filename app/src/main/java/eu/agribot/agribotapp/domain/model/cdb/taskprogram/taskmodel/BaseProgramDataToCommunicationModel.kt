package eu.agribot.agribotapp.domain.model.program.weather.toCommunication

interface BaseProgramDataToCommunicationModel<T> {

    fun toCommunicationModel(): T
}
