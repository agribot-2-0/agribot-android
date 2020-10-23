package eu.agribot.agribotapp.ui.fragment.taskcreator.weatherselect

enum class PickersMinMaxValues(val value: Int) {
    MIN_TEMP_VALUE(0),
    MAX_TEMP_VALUE(70),

    MIN_HUMIDITY_VALUE(0),
    MAX_HUMIDITY_VALUE(100),

    MIN_PRESSURE_VALUE(950),
    MAX_PRESSURE_VALUE(1050),

    MIN_WIND_VALUE(0),
    MAX_WIND_VALUE(150),

    LESS_POSITION(0),
    MORE_POSITION(1),
    EQUAL_POSITION(2),

    NO_WEATHER_VALUE_SYMBOL(-1)
}
