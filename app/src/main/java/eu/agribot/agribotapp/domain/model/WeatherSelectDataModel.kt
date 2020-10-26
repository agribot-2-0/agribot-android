package eu.agribot.agribotapp.domain.model

import androidx.annotation.DrawableRes

data class WeatherSelectDataModel(
    val id: Int,

    @DrawableRes val weatherAttributeIcon: Int,

    val conditionArray: Array<String>,

    val minimumPickerValue: Int,

    val maximumPickerValue: Int,

    val currentValue: String,

    val isSwitchActive: Boolean,

    val settedValue: Int,

    val settedCondition: String
)
