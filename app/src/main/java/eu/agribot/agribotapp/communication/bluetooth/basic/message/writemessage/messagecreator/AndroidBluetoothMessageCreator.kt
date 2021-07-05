package eu.agribot.agribotapp.communication.bluetooth.basic.message.writemessage.messagecreator

import eu.agribot.agribotapp.communication.bluetooth.basic.message.frame.carts.AgribotCartsRotate
import eu.agribot.agribotapp.communication.bluetooth.basic.message.frame.carts.AgribotToolActivationStatus
import eu.agribot.agribotapp.communication.bluetooth.basic.message.frame.data.FrameConstant
import eu.agribot.agribotapp.communication.bluetooth.basic.message.writemessage.WriteBluetoothMessageManager
import eu.agribot.agribotapp.communication.bluetooth.model.tocommunication.ArcParametrsBD
import eu.agribot.agribotapp.communication.bluetooth.model.tocommunication.RfidParametersBD
import eu.agribot.agribotapp.communication.bluetooth.model.tocommunication.SwitchSettingsBD
import eu.agribot.agribotapp.communication.bluetooth.model.tocommunication.TuzActivateBD
import kotlin.math.abs

class AndroidBluetoothMessageCreator(
    private val bluetoothMessageManager: WriteBluetoothMessageManager
) :
    BluetoothMessageCreator {
    private var switchesArray = ByteArray(8)
    private var ttControlForwardBackwardArray =
        ByteArray(FrameConstant.TTCONTROL_MOVE_BACK_FRONT_ARRAY_SIZE)
    private var ttControlLeftRightArray =
        ByteArray(FrameConstant.TTCONTROL_MOVE_LEFT_RIGHT_ARRAY_SIZE)
    private val rfidArray = ByteArray(2)

    override fun createARCMessage(arcParametrsBD: ArcParametrsBD) {
        val moveArcArray = ByteArray(FrameConstant.ARC_MESSAGE_ARRAY_SIZE)
        moveArcArray[0] = arcParametrsBD.forward.toByte()
        moveArcArray[1] = arcParametrsBD.direction.toByte()
        moveArcArray[2] = 0
        moveArcArray[3] = (abs(arcParametrsBD.radius).toInt() * 10).toByte()
        moveArcArray[4] = 0
        moveArcArray[5] = (abs(arcParametrsBD.speed) * 100).toInt().toByte()
        moveArcArray[6] = FrameConstant.DUMMY_BYTE
        moveArcArray[7] = FrameConstant.DUMMY_BYTE
        bluetoothMessageManager.sendManualSteeringDataMessage(moveArcArray)
    }

    override fun createSwitchesMessage(switchSettingsBD: SwitchSettingsBD) {
        switchesArray[0] = switchSettingsBD.switch_number.toByte()
        switchesArray[1] =
            if (switchSettingsBD.is_switch_activate) AgribotToolActivationStatus.ACTIVE.active else AgribotToolActivationStatus.INACTIVE.active
        switchesArray[2] = switchSettingsBD.switch_parametr_value.toByte()
        bluetoothMessageManager.sendSwitchSettingsMessage(switchesArray)
    }

    override fun createTTControlForwardMessage(tuzActivateBD: TuzActivateBD) {
        ttControlForwardBackwardArray[0] = 1.toByte()
        ttControlForwardBackwardArray[1] = FrameConstant.DUMMY_BYTE
        ttControlForwardBackwardArray[2] =
            if (tuzActivateBD.isFrontTuzActivate) AgribotToolActivationStatus.ACTIVE.active else AgribotToolActivationStatus.INACTIVE.active
        ttControlForwardBackwardArray[3] =
            if (tuzActivateBD.isBackTuzActivate) AgribotToolActivationStatus.ACTIVE.active else AgribotToolActivationStatus.INACTIVE.active

        val moveForwardArray = ttControlForwardBackwardArray
        moveForwardArray[0] = 1.toByte()
        moveForwardArray[1] = FrameConstant.DUMMY_BYTE
        bluetoothMessageManager.sendTTControlForwardMessage(moveForwardArray)
    }

    override fun createTTControlBackwardMessage(tuzActivateBD: TuzActivateBD) {
        ttControlForwardBackwardArray[0] = 0.toByte()
        ttControlForwardBackwardArray[1] = FrameConstant.DUMMY_BYTE
        ttControlForwardBackwardArray[2] =
            if (tuzActivateBD.isFrontTuzActivate) AgribotToolActivationStatus.ACTIVE.active else AgribotToolActivationStatus.INACTIVE.active
        ttControlForwardBackwardArray[3] =
            if (tuzActivateBD.isBackTuzActivate) AgribotToolActivationStatus.ACTIVE.active else AgribotToolActivationStatus.INACTIVE.active

        val moveBackArray = ttControlForwardBackwardArray
        moveBackArray[0] = 0.toByte()
        moveBackArray[1] = FrameConstant.DUMMY_BYTE
        bluetoothMessageManager.sendTTControlBackwardMessage(moveBackArray)
    }

    override fun createTTControlLeftMessage(tuzActivateBD: TuzActivateBD) {
        ttControlLeftRightArray[0] =
            if (tuzActivateBD.isFrontTuzActivate) AgribotToolActivationStatus.ACTIVE.active else AgribotToolActivationStatus.INACTIVE.active
        ttControlLeftRightArray[1] = AgribotCartsRotate.LEFT.rotate
        ttControlLeftRightArray[2] =
            if (tuzActivateBD.isBackTuzActivate) AgribotToolActivationStatus.ACTIVE.active else AgribotToolActivationStatus.INACTIVE.active
        ttControlLeftRightArray[3] = AgribotCartsRotate.LEFT.rotate
        bluetoothMessageManager.sendTTControlTurnLeftMessage(ttControlLeftRightArray)
    }

    override fun createTTControlRightMessage(tuzActivateBD: TuzActivateBD) {
        ttControlLeftRightArray[0] =
            if (tuzActivateBD.isFrontTuzActivate) AgribotToolActivationStatus.ACTIVE.active else AgribotToolActivationStatus.INACTIVE.active
        ttControlLeftRightArray[1] = AgribotCartsRotate.RIGHT.rotate
        ttControlLeftRightArray[2] =
            if (tuzActivateBD.isBackTuzActivate) AgribotToolActivationStatus.ACTIVE.active else AgribotToolActivationStatus.INACTIVE.active
        ttControlLeftRightArray[3] = AgribotCartsRotate.RIGHT.rotate
        bluetoothMessageManager.sendTTControlTurnRightMessage(ttControlLeftRightArray)
    }

    override fun createRfifRequestMessage(rfidParametersBD: RfidParametersBD) {
        rfidArray[0] = rfidParametersBD.comNumber.toByte()
        rfidArray[1] = rfidParametersBD.antennaNumber.toByte()
        bluetoothMessageManager.sendRfidScanRequest(rfidArray)
    }
}
