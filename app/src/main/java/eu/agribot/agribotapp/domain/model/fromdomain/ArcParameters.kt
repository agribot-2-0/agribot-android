package eu.agribot.agribotapp.domain.model.fromdomain

import eu.agribot.agribotapp.communication.bluetooth.model.tocommunication.ArcParametrsBD

class ArcParameters(
    val radius: Double,
    val direction: Int,
    val forward: Int,
    val speed: Double
) : BaseDomainDataModel<ArcParametrsBD> {
    override fun toCommunicationModel() = ArcParametrsBD(
        radius = this.radius,
        direction = this.direction,
        forward = this.forward,
        speed = this.speed
    )
}
