package eu.agribot.agribotapp.ui.fragment.taskcreator.weatherselect.recycler

import eu.agribot.agribotapp.domain.model.WeatherSelectDataModel

sealed class WeatherAttribute() {
    data class TemperatureAttribute(val weatherSelectDataModel: WeatherSelectDataModel) : WeatherAttribute()
    data class HumidityAttribute(val weatherSelectDataModel: WeatherSelectDataModel) : WeatherAttribute()
    data class WindAttribute(val weatherSelectDataModel: WeatherSelectDataModel) : WeatherAttribute()
    data class PressureAttribute(val weatherSelectDataModel: WeatherSelectDataModel) : WeatherAttribute()


}