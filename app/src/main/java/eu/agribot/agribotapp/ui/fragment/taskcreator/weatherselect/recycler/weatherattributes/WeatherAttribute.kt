package eu.agribot.agribotapp.ui.fragment.taskcreator.weatherselect.recycler.weatherattributes

data class WeatherAttribute(
    val attributeID: Int,
    val weatherValue: String,
    val weatherCondition: String,
    val isWeatherItemActive: Boolean
)
