package eu.agribot.agribotapp.communication.bluetooth.message.receivedmessage

object DiagnosticDataArrayPosition {
    const val ACCUMULATOR_VOLTGE_FIRST_BYTE_POSITION = 6
    const val ACCUMULATOR_VOLTAGE_SECOND_BYTE_POSITION = 7

    const val ENGINE_ROTATIONS_FIRST_BYTE_POSITION = 0
    const val ENGINE_ROTATIONS_SECOND_BYTE_POSITION = 1

    const val TORQUE_OF_ENGINE_BYTE_POSITION = 2

    const val ENGINE_FUEL_RATE_BYTE_POSITION = 3

    const val FUEL_LEVEL_BYTE_POSITION = 4

    const val FUEL_TEMPERATURE_BYTE_POSITION = 5
}
