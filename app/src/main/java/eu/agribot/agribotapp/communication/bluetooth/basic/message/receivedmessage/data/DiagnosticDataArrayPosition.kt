package eu.agribot.agribotapp.communication.bluetooth.basic.message.receivedmessage.data

enum class DiagnosticDataArrayPosition(val position: Int) {

    ACCUMULATOR_VOLTGE_FIRST_BYTE(6),

    ACCUMULATOR_VOLTAGE_SECOND_BYTE(7),

    ENGINE_ROTATIONS_FIRST_BYTE(0),

    ENGINE_ROTATIONS_SECOND_BYTE(1),

    TORQUE_OF_ENGINE_BYTE(2),

    ENGINE_FUEL_RATE_BYTE(3),

    FUEL_LEVEL_BYTE(4),

    FUEL_TEMPERATURE_BYTE(5),

    HUDRAULIC_OIL_TEMPERATURE_BYTE(2),
}
