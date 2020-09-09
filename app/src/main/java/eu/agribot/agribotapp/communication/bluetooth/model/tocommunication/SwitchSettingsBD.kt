package eu.agribot.agribotapp.communication.bluetooth.model.tocommunication

data class SwitchSettingsBD(
    val switch_number: Int,
    val is_switch_activate: Boolean,
    val switch_parametr_value: Int
)
