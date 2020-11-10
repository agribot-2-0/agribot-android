package eu.agribot.agribotapp.domain.model

import eu.agribot.agribotapp.ui.fragment.taskcreator.weatherselect.data.PickerSymbols

data class WeatherSelectDataModel(
    val id: Int,

    val conditionArray: Array<String> = arrayOf(
        PickerSymbols.PICKER_CONDITION_LESS.symbol,
        PickerSymbols.PICKER_CONDITION_BIGGER.symbol,
        PickerSymbols.PICKER_CONDITION_EQUAL.symbol
    ),

    val minimumPickerValue: Int,

    val maximumPickerValue: Int,

    val currentValue: String = PickerSymbols.PICKER_DEFAULT_VALUE.symbol,

    val isSwitchActive: Boolean = false,

    val presetValue: String = PickerSymbols.PICKER_DEFAULT_VALUE.symbol,

    val presetCondition: String = PickerSymbols.PICKER_DEFAULT_VALUE.symbol
)
