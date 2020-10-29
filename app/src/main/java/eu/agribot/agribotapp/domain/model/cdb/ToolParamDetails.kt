package eu.agribot.agribotapp.domain.model.cdb


data class ToolParamDetails(
    val sliderNumber: Int,
    val value: Int,
    val minValue: Int = 0,
    val maxValues: Int = 100
)
