package eu.agribot.agribotapp.domain.model.cdb

import com.google.firebase.Timestamp
import eu.agribot.agribotapp.ui.fragment.taskcreator.weatherselect.PickerSymbols

data class TaskDetails(

    val task_name: String = "",
    val path_name: String = "",
    val start_time: Timestamp? = Timestamp(946728000L, 0),
    val start_time_estimated: Boolean = false,

    val temperature_value: String = PickerSymbols.PICKER_DEFAULT_VALUE.symbol,
    val temperature_condition: String = PickerSymbols.PICKER_DEFAULT_VALUE.symbol,

    val wind_value: String = PickerSymbols.PICKER_DEFAULT_VALUE.symbol,
    val wind_condition: String = PickerSymbols.PICKER_DEFAULT_VALUE.symbol,

    val humidity_value: String = PickerSymbols.PICKER_DEFAULT_VALUE.symbol,
    val humidity_condition: String = PickerSymbols.PICKER_DEFAULT_VALUE.symbol,

    val pressure_value: String = PickerSymbols.PICKER_DEFAULT_VALUE.symbol,
    val pressure_condition: String = PickerSymbols.PICKER_DEFAULT_VALUE.symbol
)
