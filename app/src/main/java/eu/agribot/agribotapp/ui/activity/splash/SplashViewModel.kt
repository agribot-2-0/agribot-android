package eu.agribot.agribotapp.ui.activity.splash

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
}
