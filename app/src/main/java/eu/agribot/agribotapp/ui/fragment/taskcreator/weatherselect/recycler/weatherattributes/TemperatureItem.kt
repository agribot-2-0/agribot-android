package eu.agribot.agribotapp.ui.fragment.taskcreator.weatherselect.recycler.weatherattributes

import eu.agribot.agribotapp.domain.model.WeatherSelectDataModel
import eu.agribot.agribotapp.ui.fragment.taskcreator.weatherselect.data.PickerSymbols
import eu.agribot.agribotapp.ui.fragment.taskcreator.weatherselect.data.PickersMinMaxValues
import eu.agribot.agribotapp.ui.fragment.taskcreator.weatherselect.data.WeatherItemNumber

data class TemperatureItem(
    val id: Int = WeatherItemNumber.TEMPERATURE_ITEM.itemNumber,
    val minimumPickerValue: Int = PickersMinMaxValues.MIN_TEMP_VALUE.value,
    val maximumPickerValue: Int = PickersMinMaxValues.MAX_TEMP_VALUE.value,
    val currentValue: String = PickerSymbols.PICKER_DEFAULT_VALUE.symbol,
    val isSwitchActive: Boolean = false,
    val presetValue: String = PickerSymbols.PICKER_DEFAULT_VALUE.symbol,
    val settedCondition: String = PickerSymbols.PICKER_DEFAULT_VALUE.symbol
) : WeatherAttribureMapper<WeatherSelectDataModel> {
    override fun toWeatherDataModel() = WeatherSelectDataModel(
        id = this.id,
        minimumPickerValue = this.minimumPickerValue,
        maximumPickerValue = this.maximumPickerValue,
        currentValue = this.currentValue,
        isSwitchActive = this.isSwitchActive,
        settedValue = this.presetValue
    )
}
