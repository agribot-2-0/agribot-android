package eu.agribot.agribotapp.ui.activity.splash

import androidx.lifecycle.viewModelScope
import eu.agribot.agribotapp.base.BaseViewModel
import eu.agribot.agribotapp.common.livedata.MutableLiveEvent
import eu.agribot.agribotapp.common.navigation.events.MainActivityNavigationEvent
import eu.agribot.agribotapp.common.navigation.events.WelcomeActivityNavigationEvent
import eu.agribot.agribotapp.domain.service.AuthenticationService
import eu.agribot.agribotapp.domain.service.BluetoothConnectionService
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashViewModel(
    private val authenticationService: AuthenticationService,
    private val bluetoothConnectionService: BluetoothConnectionService
) : BaseViewModel() {
    val postDialogEvent: MutableLiveEvent<Boolean> = MutableLiveEvent()
    companion object {
        const val SPLASH_SCREEN_TIMEOUT_MILLIS = 1000L
    }

    private fun beginSplashScreenDelay() = viewModelScope.launch {
        delay(SPLASH_SCREEN_TIMEOUT_MILLIS)
        checkLoginStatusAndRedirect()
    }

    private fun postMainActivityNavigationEvent() {
        navigationEvent.postValue(
            MainActivityNavigationEvent()
        )
    }

    private fun postWelcomeActivityNavigationEvent() {
        navigationEvent.postValue(WelcomeActivityNavigationEvent())
    }

    private fun checkLoginStatusAndRedirect() {
        when (authenticationService.checkUserInApp()) {
            true -> postMainActivityNavigationEvent()
            false -> postWelcomeActivityNavigationEvent()
        }
    }

    fun handleCurrentBluetoothStatus() {
        when (bluetoothConnectionService.checkBluetoothEnable()) {
            true -> beginSplashScreenDelay()
            false -> postDialogEvent.postValue(true)
        }
    }

    fun enableBluetooth() {
        bluetoothConnectionService.enableBluetooth()
        beginSplashScreenDelay()
    }
}
