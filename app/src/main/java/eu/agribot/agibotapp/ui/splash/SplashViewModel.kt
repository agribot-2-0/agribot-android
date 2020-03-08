package eu.agribot.agibotapp.ui.splash

import androidx.lifecycle.viewModelScope
import eu.agribot.agibotapp.base.BaseViewModel
import eu.agribot.agibotapp.common.navigation.events.MainActivityNavigationEvent
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashViewModel : BaseViewModel() {

    companion object {
        const val SPLASH_SCREEN_TIMEOUT_MILLIS = 1000L
    }

    fun beginSplashScreenDelay() = viewModelScope.launch {
        delay(SPLASH_SCREEN_TIMEOUT_MILLIS)
        postMainActivityNavigationEvent()
    }

    private fun postMainActivityNavigationEvent() {
        navigationEvent.postValue(
            MainActivityNavigationEvent()
        )
    }
}
