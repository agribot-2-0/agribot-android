package eu.agribot.agribotapp.ui.fragment.taskcreator.weatherselect.recycler.weatherattributes

import androidx.annotation.DrawableRes

data class PressureAttribute(
    @DrawableRes val pressureIcon: Int,
    val pressureCondition: Array<String>,
    val minimumPressure: Int,
    val maximumPressure: Int,
    val currentPressure: String
)