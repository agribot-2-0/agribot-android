package eu.agribot.agribotapp.ui.fragment.taskcreator.weatherselect.recycler.weatherattributes

import eu.agribot.agribotapp.domain.model.WeatherSelectDataModel
import eu.agribot.agribotapp.ui.fragment.taskcreator.weatherselect.data.PickerSymbols
import eu.agribot.agribotapp.ui.fragment.taskcreator.weatherselect.data.PickersMinMaxValues
import eu.agribot.agribotapp.ui.fragment.taskcreator.weatherselect.data.WeatherItemNumber

data class WindItem(
    val id: Int = WeatherItemNumber.WIND_ITEM.itemNumber,
    val minimumPickerValue: Int = PickersMinMaxValues.MIN_WIND_VALUE.value,
    val maximumPickerValue: Int = PickersMinMaxValues.MAX_WIND_VALUE.value,
    val currentValue: String = PickerSymbols.PICKER_DEFAULT_VALUE.symbol,
    val isSwitchActive: Boolean = false,
    val presetValue: String = PickerSymbols.PICKER_DEFAULT_VALUE.symbol,
    val presetCondition: String = PickerSymbols.PICKER_DEFAULT_VALUE.symbol
) : WeatherAttribureMapper<WeatherSelectDataModel> {
    override fun toWeatherDataModel() = WeatherSelectDataModel(
        id = this.id,
        minimumPickerValue = this.minimumPickerValue,
        maximumPickerValue = this.maximumPickerValue,
        presetCondition = this.presetCondition,
        currentValue = this.currentValue,
        isSwitchActive = this.isSwitchActive,
        presetValue = this.presetValue
    )
}
