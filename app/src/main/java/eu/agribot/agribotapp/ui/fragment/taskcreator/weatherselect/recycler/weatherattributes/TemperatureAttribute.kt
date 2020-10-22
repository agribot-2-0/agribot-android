package eu.agribot.agribotapp.ui.fragment.taskcreator.weatherselect.recycler.weatherattributes

import androidx.annotation.DrawableRes

data class TemperatureAttribute(
    @DrawableRes val temperatureIcon: Int,
    val temperatureCondition: Array<String>,
    val minimumTemperature: Int,
    val maximumTemperature: Int,
    val currentTemperature: String
)