package eu.agribot.agribotapp.ui.activity.authentication.signup

import androidx.lifecycle.MutableLiveData
import eu.agribot.agribotapp.base.BaseViewModel
import eu.agribot.agribotapp.common.livedata.LiveEvent
import eu.agribot.agribotapp.common.livedata.MutableLiveEvent
import eu.agribot.agribotapp.common.navigation.events.LoginActivityNavigationEvent
import eu.agribot.agribotapp.domain.service.AuthenticationService

class SignupViewModel(
    private val authenticationService: AuthenticationService
) : BaseViewModel() {

    val email: MutableLiveData<String> = MutableLiveData()
    val password: MutableLiveData<String> = MutableLiveData()
    val showToastEvent: LiveEvent<String> = MutableLiveEvent()
    val canSignup: MutableLiveData<Boolean> = MutableLiveData()

    fun onEmailChanged(text: CharSequence) {
        email.value = text.toString()
    }

    fun onPasswordChanged(text: CharSequence) {
        password.value = text.toString()
    }

    fun onHaveAccountClicked() {
        navigationEvent.postValue(
            LoginActivityNavigationEvent()
        )
    }

    fun checkIfCanLogin(loginBox: String, passwordBox: String) {
        if (loginBox.isBlank() || passwordBox.isBlank() || passwordBox.length < 8) {
            canSignup.value = false
            showToastEvent.postValue("Email or Password Incorrect")
        } else
            canSignup.value = true
    }

    fun onClickButtonRegister() {
        if (canSignup.value == true) {
            performRegister()
        } else
            showToastEvent.postValue("Error")
    }

    fun performRegister() {
        checkIfCanLogin(email.value.toString(), password.value.toString())
        authenticationService.registerInFirebase(
            email.value.toString(), password.value.toString(),
            registerSucess = this::onRegisterSucess,
            registerFailed = this::onRegisterError
        )
    }

    fun onRegisterSucess() {
        showToastEvent.postValue("Register sucessfully")
    }

    fun onRegisterError() {
        showToastEvent.postValue("Register Failed")
    }
}
