package eu.agribot.agribotapp.ui.activity.splash

import androidx.lifecycle.viewModelScope
import eu.agribot.agribotapp.base.BaseViewModel
import eu.agribot.agribotapp.common.livedata.MutableLiveEvent
import eu.agribot.agribotapp.common.navigation.events.MainActivityNavigationEvent
import eu.agribot.agribotapp.common.navigation.events.WelcomeActivityNavigationEvent
import eu.agribot.agribotapp.domain.service.AuthenticationService
import eu.agribot.agribotapp.domain.service.BluetoothConnectionService
import eu.agribot.agribotapp.domain.service.cloud.CloudDatabaseService
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class SplashViewModel(
    private val authenticationService: AuthenticationService,
    private val bluetoothConnectionService: BluetoothConnectionService,
    private val cloudDatabaseService: CloudDatabaseService
) : BaseViewModel() {
    val postDialogEvent: MutableLiveEvent<Boolean> = MutableLiveEvent()

    companion object {
        const val SPLASH_SCREEN_TIMEOUT_MILLIS = 1000L
    }

    fun getRobotName() {
        cloudDatabaseService.getUserRobotName()
    }


    private fun beginSplashScreenDelay() = viewModelScope.launch {
        cloudDatabaseService.getUserRobotName()
       runBlocking {
           !waitForRobotNameIsNonNull()
        }
        delay(SPLASH_SCREEN_TIMEOUT_MILLIS)
        checkLoginStatusAndRedirect()
    }

     fun waitForRobotNameIsNonNull(): Boolean {
        return cloudDatabaseService.getuserRobotNameStatus()
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
