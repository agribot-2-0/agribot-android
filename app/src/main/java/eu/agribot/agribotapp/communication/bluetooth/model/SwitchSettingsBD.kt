package eu.agribot.agribotapp.communication.bluetooth.model

import eu.agribot.agribotapp.domain.model.SwitchSettings

data class SwitchSettingsBD(
    val switch_number: Int = 1,
    val is_switch_activate: Boolean = false,
    val switch_parametr_value: Int = 1
) : BaseDataModel<SwitchSettings> {

    override fun toDomainModel() = SwitchSettings(
        switchNumber = this.switch_number,
        switchActive = this.is_switch_activate,
        switchValue = this.switch_parametr_value
    )

}