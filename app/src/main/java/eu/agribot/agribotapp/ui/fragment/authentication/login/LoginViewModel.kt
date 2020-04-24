package eu.agribot.agribotapp.ui.fragment.authentication.login

import androidx.lifecycle.MutableLiveData
import eu.agribot.agribotapp.base.BaseViewModel
import eu.agribot.agribotapp.common.livedata.LiveEvent
import eu.agribot.agribotapp.common.livedata.MutableLiveEvent
import eu.agribot.agribotapp.communication.firebase.FirebaseService

class LoginViewModel(
    private val firebaseService: FirebaseService
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
        LoginFragment().backToRegister()
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

    fun signOutInformation() {
        showToastEvent.postValue("Logged Out")
    }

    private fun login() {
        val loginEmail = email.value.toString()
        val loginPassword = password.value.toString()
        firebaseService.login(
            loginEmail = loginEmail,
            loginPassword = loginPassword,
            onSucess = this::onLoginSucess,
            onError = this::onLoginFailed
        )
    }

    fun sendResetEmail() {
        val email = email.value.toString()
        firebaseService.sendResetPasswordEmail(
            loginEmail = email,
            onSucess = this::onSendEmailSucess,
            onError = this::onSendEmailError
        )
    }

    fun signOut() {
        firebaseService.signOut(
            signOut = this::signOutInformation
        )
    }
}
