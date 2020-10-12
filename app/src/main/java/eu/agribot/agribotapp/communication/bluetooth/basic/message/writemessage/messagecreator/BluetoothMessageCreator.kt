package eu.agribot.agribotapp.communication.bluetooth.basic.message.writemessage.messagecreator

import eu.agribot.agribotapp.communication.bluetooth.model.tocommunication.ArcParametrsBD
import eu.agribot.agribotapp.communication.bluetooth.model.tocommunication.SwitchSettingsBD
import eu.agribot.agribotapp.communication.bluetooth.model.tocommunication.TuzActivateBD

interface BluetoothMessageCreator {

    fun createARCMessage(arcParametrsBD: ArcParametrsBD)

    fun createSwitchesMessage(switchSettingsBD: SwitchSettingsBD)

    fun createTTControlForwardMessage(tuzActivateBD: TuzActivateBD)

    fun createTTControlBackwardMessage(tuzActivateBD: TuzActivateBD)

    fun createTTControlLeftMessage(tuzActivateBD: TuzActivateBD)

    fun createTTControlRightMessage(tuzActivateBD: TuzActivateBD)
}
