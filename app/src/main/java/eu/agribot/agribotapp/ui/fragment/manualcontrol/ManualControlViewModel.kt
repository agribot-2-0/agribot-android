package eu.agribot.agribotapp.ui.fragment.manualcontrol

import android.annotation.SuppressLint
import android.os.Handler
import android.util.Log
import eu.agribot.agribotapp.base.BaseViewModel
import eu.agribot.agribotapp.common.navigation.events.BluetoothConnectionNavigationEvent
import eu.agribot.agribotapp.common.navigation.events.ToolControlNavigationEvent
import eu.agribot.agribotapp.domain.model.BluetoothAdressCodeModel.Companion.RIDE_THE_CURVE
import eu.agribot.agribotapp.domain.model.BluetoothMessageModel
import eu.agribot.agribotapp.domain.service.BluetoothUtils
import eu.agribot.agribotapp.domain.service.ManualModeService
import eu.agribot.agribotapp.domain.service.RemoteControlService
import timber.log.Timber

class ManualControlViewModel(
    private val remoteControlService: RemoteControlService,
    private val bluetoothUtils: BluetoothUtils,
    private val manualModeService: ManualModeService
) : BaseViewModel() {

    companion object {
        private const val MAX_ARCH = 11.0
        private const val MIN_ARCH = 1.8
        private const val A_FACTOR_LEFT = 0.15333333333333332
        private const val B_FACTOR_LEFT = 13.3
        private const val A_FACTOR_LEFT_SECOND = -0.15333333333333332
        private const val B_FACTOR_LEFT_SECOND = -14.299999999999997
        private const val A_FACTOR_RIGHT = 0.15333333333333332
        private const val B_FACTOR_RIGHT = -13.3
        private const val A_FACTOR_RIGHT_SECOND = -0.15333333333333332
        private const val B_FACTOR_RIGHT_SECOND = 14.299999999999997
    }

    var connectionStatus: String = if (bluetoothUtils.connectionState) {
        "Agribot  jest połączony"
    } else {
        "Agribot nie jest połączony - Kliknij, aby połączyć"
    }

    @SuppressLint("LogNotTimber")
    fun funOnJoystickAngleChanged(angle: Int, strength: Int) {
        var joystickAngle = 0
        var radius: Double = MAX_ARCH

        when (angle) {
            in 90..269 -> {
                joystickAngle = -angle + 90
            }
            in 0..89 -> {
                joystickAngle = -angle + 90
            }
            in 270..360 -> {
                val beta = -(angle - 90)
                joystickAngle = if (beta <= 180) {
                    beta + 360
                } else {
                    beta
                }
            }
        }

        if (joystickAngle < -15 && joystickAngle > 15) {
            radius = MAX_ARCH
        } else if (joystickAngle < -165 || joystickAngle > 165) {
            radius = MAX_ARCH
        } else if (joystickAngle < -15 && joystickAngle > -75) {
            radius = A_FACTOR_LEFT * joystickAngle + B_FACTOR_LEFT
        } else if (joystickAngle < -75 && joystickAngle > -105) {
            radius = MIN_ARCH
        } else if (joystickAngle < -105 && joystickAngle > -165) {
            radius = A_FACTOR_LEFT_SECOND * joystickAngle + B_FACTOR_LEFT_SECOND
        } else if (joystickAngle in 15..75) {
            radius = A_FACTOR_RIGHT * joystickAngle + B_FACTOR_RIGHT
        } else if (joystickAngle in 76..105) {
            radius = -MIN_ARCH
        } else if (joystickAngle in 106..165) {
            radius = A_FACTOR_RIGHT_SECOND * joystickAngle + B_FACTOR_RIGHT_SECOND
        }
        val direction = if (radius > 0) 1 else 0
        val speed = (strength * 2.3) * 0.3 / 152.0f
        val forward = if (angle in 181..359) 1 else 0
        Log.e(
            "joystickAtributes", "radius :" + radius +
                    "\n" + " direction : " + direction +
                    "\n" + " forward : " + forward +
                    "\n" + " power : " + strength +
                    "\n" + " speed : " + speed
        )
    }

    internal var handler = Handler()
    var moveArray = ByteArray(8)
    val handlerTime = 100L
    var moveArcArray = ByteArray(8)

    var arcRunnable = object : Runnable {
        override fun run() {
            val moveBackFrame =
                BluetoothMessageModel(BluetoothMessageModel.CMD_ONE, RIDE_THE_CURVE, moveArcArray)
            bluetoothUtils.writeMessage(moveBackFrame.byteArray)
            Log.e("xd", "ARC RUNABLE")
            handler.postDelayed(this, handlerTime)
        }
    }

    fun onToolControlClicked() {
        navigationEvent.postValue(
            ToolControlNavigationEvent()
        )
    }

    fun manualControlNavEvent() {
        if (!bluetoothUtils.connectionState) {
            navigationEvent.postValue(
                BluetoothConnectionNavigationEvent()
            )
        } else {
            Timber.e("Couldn't manualControlNavEvent")
        }
    }

    fun sendEngineStopCommand() {
        if (bluetoothUtils.isAgribotConnect) {
            manualModeService.stopAgribotEngine()
            manualModeService.runAgribotEngine()
        } else {
            Timber.e("Agribot Disconnected")
        }
    }

    fun sendManualSteeringInitRequest() {
        if (bluetoothUtils.isAgribotConnect) {
            manualModeService.sendManualControlInitRequest()
        } else {
            Timber.e("Agribot Disconnected")
        }
    }

    fun sendManualSteeringStartFrame() {
        if (bluetoothUtils.isAgribotConnect) {
            manualModeService.sendStartManualControlFrame()
        } else {
            Timber.e("Agribot Disconnected")
        }
    }
}
