package eu.agribot.agribotapp.domain.model

data class SwitchSettings(
    val switchNumber: Int,
    val switchActive: Boolean,
    val switchValue: Int
)