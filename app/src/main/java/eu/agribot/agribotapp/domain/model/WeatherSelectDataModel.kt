package eu.agribot.agribotapp.domain.model

import androidx.annotation.DrawableRes

data class WeatherSelectDataModel(
    @DrawableRes val weatherAttributeIcon: Int,
    val conditionArray: Array<String>,
    val minimumPickerValue: Int,
    val maximumPickerValue: Int,
    val currentValue: String
)
