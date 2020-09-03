package eu.agribot.agribotapp.communication.bluetooth.model

import eu.agribot.agribotapp.domain.model.ArcParametrs

class ArcParametrsBD(
    val radius: Double = 0.0,
    val direction: Int = 0,
    val forward: Int = 0,
    val speed: Double = 0.0
) : BaseDataModel<ArcParametrs> {
    override fun toDomainModel() = ArcParametrs(
        radius = this.radius,
        direction = this.direction,
        forward = this.forward,
        speed = this.speed
    )


}