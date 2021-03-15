package eu.agribot.agribotapp.domain.model.cdb

import com.google.firebase.Timestamp
import eu.agribot.agribotapp.ui.fragment.taskcreator.weatherselect.data.PickerSymbols

data class TaskDetails(
    // TODO: Naming refactor from snake_case to camelCase
    val task_name: String = "",
    val pathName: String = PickerSymbols.PICKER_DEFAULT_VALUE.symbol,
    val startTime: Timestamp? = Timestamp(946728000L, 0),
    val startTimeEstimated: Boolean = true,

    val temperatureValue: String = PickerSymbols.PICKER_DEFAULT_VALUE.symbol,
    val temperatureCondition: String = PickerSymbols.PICKER_DEFAULT_VALUE.symbol,

    val windValue: String = PickerSymbols.PICKER_DEFAULT_VALUE.symbol,
    val windCondition: String = PickerSymbols.PICKER_DEFAULT_VALUE.symbol,

    val humidityValue: String = PickerSymbols.PICKER_DEFAULT_VALUE.symbol,
    val humidityCondition: String = PickerSymbols.PICKER_DEFAULT_VALUE.symbol,

    val pressureValue: String = PickerSymbols.PICKER_DEFAULT_VALUE.symbol,
    val pressureCondition: String = PickerSymbols.PICKER_DEFAULT_VALUE.symbol,

    val frontToolName: String = "*",
    val frontToolMode: Int = 0,
    val backToolName: String = "*",
    val backToolMode: Int = 0
)
