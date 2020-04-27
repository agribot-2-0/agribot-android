package eu.agribot.agribotapp.ui.fragment.authentication.login

import androidx.lifecycle.MutableLiveData
import eu.agribot.agribotapp.base.BaseViewModel
import eu.agribot.agribotapp.common.livedata.LiveEvent
import eu.agribot.agribotapp.common.livedata.MutableLiveEvent
import eu.agribot.agribotapp.common.navigation.events.ToolControlNavigationEvent
import eu.agribot.agribotapp.domain.service.LoginService

class LoginViewModel(
    private val loginService: LoginService
) : BaseViewModel() {

    val email: MutableLiveData<String> = MutableLiveData()
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

    fun onReturnButtonClicked() {
        navigationEvent.postValue(
            ToolControlNavigationEvent()
        )
    }

    fun onSendEmailSucess() {
        showToastEvent.postValue("Mail send sucessfully")
    }

    fun onSendEmailError() {
        showToastEvent.postValue("Error send Mail")
    }

    fun onLoginSucess() {
        showToastEvent.postValue("Login sucessfull")
    }

    fun onLoginFailed() {
        showToastEvent.postValue("Login Failed")
    }

    fun showSignOutInformation() {
        showToastEvent.postValue("Logged Out")
    }

    private fun login() {
        val loginEmail = email.value.toString()
        val loginPassword = password.value.toString()
        loginService.loginToFirebase(
            loginEmail, loginPassword,
            loginSucess = this::onLoginSucess,
            loginFailed = this::onLoginFailed
        )
    }

    fun sendResetEmail() {
        val email = email.value.toString()
        loginService.resetEmailfromFirebase(
            email,
            confirmSending = this::onSendEmailSucess,
            errorSending = this::onSendEmailError
        )
    }

    fun signOut() {
        loginService.logOutFromFirebase(signOutInformation = this::showSignOutInformation)
    }
}
