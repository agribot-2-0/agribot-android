package eu.agribot.agribotapp.domain.service.joystick.manualmode

enum class RadiusData(val value: Double) {

    CLOCKWISE(1.0),

    COUNTERCLOCKWISE(-1.0),

    GENTLE(4.0),

    SHARP(2.0),

    STRAIGHT(11.0)
}
