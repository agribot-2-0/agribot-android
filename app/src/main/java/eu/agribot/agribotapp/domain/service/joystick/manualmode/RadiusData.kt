package eu.agribot.agribotapp.domain.service.joystick.manualmode

enum class RadiusData(val value: Double) {

    FRONT(11.toDouble()),

    LOW_FRONT_RIGHT((-4).toDouble()),

    HARD_FRONT_RIGHT((-2).toDouble()),

    HARD_BACK_RIGHT((-2).toDouble()),

    LOW_BACK_RIGHT((-4).toDouble()),

    BACK(11.toDouble()),

    LOW_BACK_LEFT(4.toDouble()),

    HARD_BACK_LEFT(2.toDouble()),

    HARD_FRONT_LEFT(2.toDouble()),

    LOW_FRONT_LEFT(4.toDouble())
}
