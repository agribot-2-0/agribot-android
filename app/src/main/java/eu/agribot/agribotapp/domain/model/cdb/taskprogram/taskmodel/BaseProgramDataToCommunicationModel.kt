package eu.agribot.agribotapp.domain.model.cdb.taskprogram.taskmodel

interface BaseProgramDataToCommunicationModel<T> {

    fun toCommunicationModel(): T
}
