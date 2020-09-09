package eu.agribot.agribotapp.domain.model.fromdomain

import eu.agribot.agribotapp.communication.bluetooth.model.tocommunication.SwitchSettingsBD

class SwitchSettings(
    val switchNumber: Int = -1,
    val switchActive: Boolean = false,
    val switchValue: Int = -1
) : BaseDomainDataModel<SwitchSettingsBD> {
    override fun toCommunicationModel() = SwitchSettingsBD(
        switch_number = this.switchNumber,
        is_switch_activate = this.switchActive,
        switch_parametr_value = this.switchValue
    )
}
