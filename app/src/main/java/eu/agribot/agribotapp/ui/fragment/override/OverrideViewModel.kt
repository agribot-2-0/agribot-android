package eu.agribot.agribotapp.ui.fragment.override

import eu.agribot.agribotapp.base.BaseViewModel
import eu.agribot.agribotapp.common.livedata.MutableLiveEvent
import eu.agribot.agribotapp.domain.model.BluetoothAdressCodeModel
import eu.agribot.agribotapp.domain.model.BluetoothMessageModel
import eu.agribot.agribotapp.domain.service.BluetoothUtils

class OverrideViewModel(private val bluetoothUtils: BluetoothUtils) : BaseViewModel() {
    val onFrontClickEvent: MutableLiveEvent<Boolean> = MutableLiveEvent()
    val onRearClickEvent: MutableLiveEvent<Boolean> = MutableLiveEvent()
    var moveArray = ByteArray(8)
    var front: Int = 0
    var rear: Int = 0
    var rotateArray = ByteArray(4)

    var rotateFrame = BluetoothMessageModel(
        BluetoothMessageModel.CMD_ONE,
        BluetoothAdressCodeModel.ROTATE_CARRIAGES,
        rotateArray
    )

    companion object {
        private const val ROTATE_LEFT = 1
        private const val ROTATE_RIGHT = 0
    }

    fun onFrontClick() {
        if (onFrontClickEvent.value != true) {
            onFrontClickEvent.postValue(true)
        } else {
            onFrontClickEvent.postValue(false)
        }
    }

    fun setFront(boolean: Boolean) {
        front = if (boolean) {
            1
        } else {
            0
        }
    }

    fun setRear(boolean: Boolean) {
        rear = if (boolean) {
            1
        } else {
            0
        }
    }

    fun onJoystickParametrsChanged(angle: Int, strength: Int) {
        when (angle) {
            in 0..180 -> {
                moveForward()
            }
            else -> moveBack()
        }
    }

    fun onJoystickLeftParametrsChanged(angle: Int, strength: Int) {
        when (angle) {
            in 90..180 -> {
                rotateLeft()
            }
            else -> {
                rotateRight()
            }
        }
    }

    private fun moveForward() {
        moveArray[0] = 1.toByte()
        moveArray[1] = 0xFF.toByte()
        moveArray[2] = front.toByte()
        moveArray[3] = rear.toByte()

        val moveBackArray = moveArray
        moveBackArray[0] = 1.toByte()
        moveBackArray[1] = 0xFF.toByte()
        val moveBackFrame = BluetoothMessageModel(
            BluetoothMessageModel.CMD_ONE,
            BluetoothAdressCodeModel.MOVE_FORWARD_BACKWARD,
            moveBackArray
        )
        bluetoothUtils.writeMessage(moveBackFrame.byteArray)
    }

    private fun moveBack() {
        moveArray[0] = 0.toByte()
        moveArray[1] = 0xFF.toByte()
        moveArray[2] = front.toByte()
        moveArray[3] = rear.toByte()

        val moveBackArray = moveArray
        moveBackArray[0] = 0.toByte()
        moveBackArray[1] = 0xFF.toByte()
        val moveBackFrame = BluetoothMessageModel(
            BluetoothMessageModel.CMD_ONE,
            BluetoothAdressCodeModel.MOVE_FORWARD_BACKWARD,
            moveBackArray
        )
        bluetoothUtils.writeMessage(moveBackFrame.byteArray)
    }

    fun onRearClick() {
        if (onRearClickEvent.value != true) {
            onRearClickEvent.postValue(true)
        } else {
            onRearClickEvent.postValue(false)
        }
    }

    fun onProgressBarParametrChanges(progress: Int) {
        if (progress > 0) {
            rotateLeft()
        } else if (progress < 0) {
            rotateRight()
        }
    }

    private fun rotateLeft() {
        rotateArray[0] = front.toByte()
        rotateArray[1] = ROTATE_LEFT.toByte()
        rotateArray[2] = rear.toByte()
        rotateArray[3] = ROTATE_LEFT.toByte()
        rotateFrame = BluetoothMessageModel(
            BluetoothMessageModel.CMD_ONE,
            BluetoothAdressCodeModel.ROTATE_CARRIAGES,
            rotateArray
        )

        bluetoothUtils.writeMessage(rotateFrame.byteArray)
    }

    private fun rotateRight() {
        rotateArray[0] = front.toByte()
        rotateArray[1] = ROTATE_RIGHT.toByte()
        rotateArray[2] = rear.toByte()
        rotateArray[3] = ROTATE_RIGHT.toByte()
        rotateFrame = BluetoothMessageModel(
            BluetoothMessageModel.CMD_ONE,
            BluetoothAdressCodeModel.ROTATE_CARRIAGES,
            rotateArray
        )

        bluetoothUtils.writeMessage(rotateFrame.byteArray)
    }
}
