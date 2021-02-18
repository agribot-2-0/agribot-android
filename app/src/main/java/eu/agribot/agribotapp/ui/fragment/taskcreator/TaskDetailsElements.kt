package eu.agribot.agribotapp.ui.fragment.taskcreator

enum class TaskDetailsElements(val detailsElement: String) {
    SEPARATOR(" "),

    TEMPERATURE_UNIT("°C"),

    SPEED_UNIT("km/h"),

    PRESSURE_UNIT("hPa"),

    PERCENT("%"),

    EMPTY_DETAIL_VALUE("*"),

    LESS_TEXT("less"),

    MORE_TEXT("more"),

    EQUAL_TEXT("equal"),

    LESS_SYMBOL("<"),

    MORE_SYMBOL(">"),

    EQUAL_SYMBOL("="),

    DATE_FORMAT("dd/MM/yyyy HH:mm"),

    TIMEZONE("GMT"),

    NO_UNIT_VALUE(""),

    EMPTY_TASK("")
}
