package eu.agribot.agribotapp.ui.fragment.taskcreator.weatherselect.recycler.weatherattributes

import androidx.annotation.DrawableRes

data class WindAttribute(
    @DrawableRes val windIcon: Int,
    val windCondition: Array<String>,
    val minimumWind: Int,
    val maximumWind: Int,
    val currentWind: String
)
