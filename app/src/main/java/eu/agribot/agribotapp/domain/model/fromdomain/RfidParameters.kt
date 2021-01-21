package eu.agribot.agribotapp.domain.model.fromdomain

import eu.agribot.agribotapp.communication.bluetooth.model.tocommunication.RfidParametersBD

data class RfidParameters(
    val comNumber: Int,
    val antennaNumber: Int
) : BaseDomainDataModel<RfidParametersBD> {
    override fun toCommunicationModel() = RfidParametersBD(
        comNumber = this.comNumber,
        antennaNumber = this.antennaNumber
    )
}
