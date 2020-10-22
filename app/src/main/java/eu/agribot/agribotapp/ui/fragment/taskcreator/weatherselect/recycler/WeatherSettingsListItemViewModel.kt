package eu.agribot.agribotapp.ui.fragment.taskcreator.weatherselect.recycler

import androidx.annotation.DrawableRes
import eu.agribot.agribotapp.base.BaseViewModel

data class WeatherSettingsListItemViewModel(
    val id: Int,
    @DrawableRes
val weatherAttributeIcon: Int,
    val conditionArray: Array<String>,
    val minimumPickerValue: Int,
    val maximumPickerValue: Int,
    var currentValue: String,
    val isSwitchActive: Boolean
) : BaseViewModel()
