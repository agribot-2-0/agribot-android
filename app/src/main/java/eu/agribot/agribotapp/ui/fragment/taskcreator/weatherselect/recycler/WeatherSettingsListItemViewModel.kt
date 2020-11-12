package eu.agribot.agribotapp.ui.fragment.taskcreator.weatherselect.recycler

import eu.agribot.agribotapp.base.BaseViewModel

data class WeatherSettingsListItemViewModel(
    val id: Int,
    val conditionArray: Array<String>,
    val minimumPickerValue: Int,
    val maximumPickerValue: Int,
    var currentValue: String,
    val isSwitchActive: Boolean,
    val settedValue: String,
    val presetCondition: String
) : BaseViewModel()
