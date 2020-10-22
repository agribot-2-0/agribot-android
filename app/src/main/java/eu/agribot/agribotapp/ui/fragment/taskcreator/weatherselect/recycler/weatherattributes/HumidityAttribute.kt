package eu.agribot.agribotapp.ui.fragment.taskcreator.weatherselect.recycler.weatherattributes

import androidx.annotation.DrawableRes

data class HumidityAttribute(
    @DrawableRes val humidityIcon: Int,
    val humidityCondition: Array<String>,
    val minimumHumidity: Int,
    val maximumHumidity: Int,
    val currentHumidity: String
)
