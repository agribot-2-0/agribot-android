package eu.agribot.agribotapp.ui.activity.authentication.welcome

import eu.agribot.agribotapp.base.BaseViewModel
import eu.agribot.agribotapp.common.navigation.events.LoginActivityNavigationEvent
import eu.agribot.agribotapp.common.navigation.events.SignupActivityNavigationEvent

class WelcomeViewModel() : BaseViewModel() {

    fun onLoginButtonClicked() {
        navigationEvent.postValue(
            LoginActivityNavigationEvent()
        )
    }

    fun onSignupTextClicked() {
        navigationEvent.postValue(
            SignupActivityNavigationEvent()
        )
    }
}
