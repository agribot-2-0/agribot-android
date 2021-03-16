package eu.agribot.agribotapp.ui.activity.authentication.login

import androidx.lifecycle.MutableLiveData
import eu.agribot.agribotapp.R
import eu.agribot.agribotapp.base.BaseViewModel
import eu.agribot.agribotapp.common.livedata.LiveEvent
import eu.agribot.agribotapp.common.livedata.MutableLiveEvent
import eu.agribot.agribotapp.common.navigation.events.MainActivityNavigationEvent
import eu.agribot.agribotapp.common.navigation.events.PasswordReminderActivityNavigationEvent
import eu.agribot.agribotapp.common.navigation.events.SignupActivityNavigationEvent
import eu.agribot.agribotapp.common.resources.ResourcesProvider
import eu.agribot.agribotapp.domain.service.AuthenticationService

class LoginViewModel(
    private val authenticationService: AuthenticationService,
    private val resourcesProvider: ResourcesProvider
) : BaseViewModel() {

    private val email: MutableLiveData<String> = MutableLiveData()
    private val password: MutableLiveData<String> = MutableLiveData()
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
        authenticationService.getUserRobotName(onGotRobotNameSucess = this::onSaveRobotSerialNumberSucess)
    }

    private fun onSaveRobotSerialNumberSucess() {
        navigationEvent.postValue(
            MainActivityNavigationEvent()
        )
        showToastEvent.postValue(resourcesProvider.getString(R.string.toast_login_sucess))
    }

    private fun onLoginFailed(loginError: String) {
        showToastEvent.postValue(loginError)
    }

    private fun login() {
        val loginEmail = email.value.toString()
        val loginPassword = password.value.toString()
        authenticationService.login(
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
