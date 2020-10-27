package eu.agribot.agribotapp.ui.fragment.taskcreator.weatherselect.recycler.weatherattributes

data class WeatherAttribute(
    val attributeID: Int,
    val weatherValue: Int,
    val weatherCondition: String,
    val isWeatherActive: Boolean
)
