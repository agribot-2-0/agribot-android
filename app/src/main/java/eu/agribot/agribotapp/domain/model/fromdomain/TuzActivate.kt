package eu.agribot.agribotapp.domain.model.fromdomain

import eu.agribot.agribotapp.communication.bluetooth.model.tocommunication.TuzActivateBD

class TuzActivate(
    val isFrontTuzActivate: Boolean = false,
    val isBackTuzActivate: Boolean = false
) : BaseDomainDataModel<TuzActivateBD> {
    override fun toCommunicationModel() = TuzActivateBD(
        isFrontTuzActivate = this.isFrontTuzActivate,
        isBackTuzActivate = this.isBackTuzActivate
    )
}
