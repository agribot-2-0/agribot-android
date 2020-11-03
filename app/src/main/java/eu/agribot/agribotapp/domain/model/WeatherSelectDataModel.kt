package eu.agribot.agribotapp.domain.model

data class WeatherSelectDataModel(
    val id: Int,

    val conditionArray: Array<String>,

    val minimumPickerValue: Int,

    val maximumPickerValue: Int,

    val currentValue: String,

    val isSwitchActive: Boolean,

    val settedValue: Int,

    val settedCondition: String
)
