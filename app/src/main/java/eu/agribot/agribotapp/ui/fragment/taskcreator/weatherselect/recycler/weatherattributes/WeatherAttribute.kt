package eu.agribot.agribotapp.ui.fragment.taskcreator.weatherselect.recycler.weatherattributes

import eu.agribot.agribotapp.domain.model.WeatherSelectDataModel

sealed class WeatherAttribute(val weatherSelectDataModel: WeatherSelectDataModel?) {
    /*
    data class TemperatureAttribute(val temperatureAttribute: WeatherSelectDataModel) :
        WeatherAttribute(temperatureAttribute?.weatherSelectDataModel)

    data class HumidityAttribute(val humidityAttribute: HumidityAttribute?) :
        WeatherAttribute(humidityAttribute?.weatherSelectDataModel)

    data class WindAttribute(val windAttribute: WindAttribute?) :
        WeatherAttribute(windAttribute?.weatherSelectDataModel)

    data class PressureAttribute(val pressureAttribute: PressureAttribute?) :
        WeatherAttribute(pressureAttribute?.weatherSelectDataModel)

     */
}