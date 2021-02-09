package eu.agribot.agribotapp.domain.service.joystick.manualmode

enum class AngleRange(val minValue: Int, val maxValue: Int) {

    FRONT(81, 100),

    LOW_FRONT_RIGHT(41, 80),

    HARD_FRONT_RIGHT(0, 40),

    HARD_BACK_RIGHT(321, 360),

    LOW_BACK_RIGHT(281, 320),

    BACK(261, 280),

    LOW_BACK_LEFT(221, 260),

    HARD_BACK_LEFT(181, 220),

    HARD_FRONT_LEFT(141, 180),

    LOW_FRONT_LEFT(101, 140)
}
