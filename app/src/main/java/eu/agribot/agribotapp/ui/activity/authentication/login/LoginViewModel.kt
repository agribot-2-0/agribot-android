package eu.agribot.agribotapp.ui.activity.authentication.login

import androidx.lifecycle.MutableLiveData
import eu.agribot.agribotapp.base.BaseViewModel
import eu.agribot.agribotapp.common.livedata.LiveEvent
import eu.agribot.agribotapp.common.livedata.MutableLiveEvent
import eu.agribot.agribotapp.common.navigation.events.MainActivityNavigationEvent
import eu.agribot.agribotapp.common.navigation.events.PasswordReminderActivityNavigationEvent
import eu.agribot.agribotapp.common.navigation.events.SignupActivityNavigationEvent
import eu.agribot.agribotapp.domain.service.AuthenticationService

class LoginViewModel(
    private val authenticationService: AuthenticationService
) : BaseViewModel() {

    private val email: MutableLiveData<String> = MutableLiveData()
    val password: MutableLiveData<String> = MutableLiveData()
    val showToastEvent: LiveEvent<String> = MutableLiveEvent()

    fun onEmailChanged(text: CharSequence) {
        email.value = text.toString()
    }

    fun onPasswordChanged(text: CharSequence) {
        password.value = text.toString()
    }

    fun onLoginButtonClicked() {
        login()
    }

    private fun onLoginSucess() {
        navigationEvent.postValue(
            MainActivityNavigationEvent()
        )
        showToastEvent.postValue("Login sucessfull")
    }

    private fun onLoginFailed() {
        showToastEvent.postValue("Login Failed")
    }

    private fun login() {
        val loginEmail = email.value.toString()
        val loginPassword = password.value.toString()
        authenticationService.loginToFirebase(
            loginEmail, loginPassword,
            loginSucess = this::onLoginSucess,
            loginFailed = this::onLoginFailed
        )
    }

    fun onSigninTextClicked() {
        navigationEvent.postValue(
            SignupActivityNavigationEvent()
        )
    }

    fun onForgotPasswordClicked() {
        navigationEvent.postValue(
            PasswordReminderActivityNavigationEvent()
        )
    }
}
