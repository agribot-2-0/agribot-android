package eu.agribot.agribotapp.domain.model

import eu.agribot.agribotapp.ui.fragment.taskcreator.weatherselect.data.PickerSymbols

data class WeatherSelectDataModel(
    val id: Int = -1,

    val conditionArray: Array<String> = arrayOf(
        PickerSymbols.PICKER_CONDITION_LESS.symbol,
        PickerSymbols.PICKER_CONDITION_BIGGER.symbol,
        PickerSymbols.PICKER_CONDITION_EQUAL.symbol
    ),

    val minimumPickerValue: Int = 0,

    val maximumPickerValue: Int = 0,

    val currentValue: String = PickerSymbols.PICKER_DEFAULT_VALUE.symbol,

    val isSwitchActive: Boolean = false,

    val settedValue: Int = 0,

    val settedCondition: String = PickerSymbols.PICKER_DEFAULT_VALUE.symbol
)
