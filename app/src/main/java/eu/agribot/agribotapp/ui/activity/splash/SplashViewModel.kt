package eu.agribot.agribotapp.ui.activity.splash

import android.util.Log
import androidx.lifecycle.viewModelScope
import eu.agribot.agribotapp.base.BaseViewModel
import eu.agribot.agribotapp.common.navigation.events.MainActivityNavigationEvent
import eu.agribot.agribotapp.common.navigation.events.WelcomeActivityNavigationEvent
import eu.agribot.agribotapp.domain.service.AuthenticationService
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashViewModel(private val authenticationService: AuthenticationService) : BaseViewModel() {
    val user = authenticationService.getCurrentUser()

    companion object {
        const val SPLASH_SCREEN_TIMEOUT_MILLIS = 1000L
    }

    fun beginSplashScreenDelay() = viewModelScope.launch {
        delay(SPLASH_SCREEN_TIMEOUT_MILLIS)
        checkIsUserInApp()
    }

    private fun postMainActivityNavigationEvent() {
        Log.e("xd", "Zalogowany" + user.toString())
        navigationEvent.postValue(
            MainActivityNavigationEvent()
        )
    }

    private fun postWelcomeActivityNavigationEvent() {
        Log.e("xd", "Nie ma usera")
            navigationEvent.postValue(WelcomeActivityNavigationEvent())
    }

    private fun checkIsUserInApp() {
        Log.e("xd", "dupa")
        authenticationService.checkUserInApp(isInApp = this::postMainActivityNavigationEvent,
        isNotInApp = this::postWelcomeActivityNavigationEvent)
    }
}
