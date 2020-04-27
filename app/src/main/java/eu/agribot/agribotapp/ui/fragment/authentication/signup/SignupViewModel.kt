package eu.agribot.agribotapp.ui.fragment.authentication.signup

import androidx.lifecycle.MutableLiveData
import eu.agribot.agribotapp.base.BaseViewModel
import eu.agribot.agribotapp.common.livedata.LiveEvent
import eu.agribot.agribotapp.common.livedata.MutableLiveEvent
import eu.agribot.agribotapp.common.navigation.events.ToolControlNavigationEvent
import eu.agribot.agribotapp.domain.service.SignupService

class SignupViewModel(
    private val signupService: SignupService
) : BaseViewModel() {

    val email: MutableLiveData<String> = MutableLiveData()
    val password: MutableLiveData<String> = MutableLiveData()
    val showToastEvent: LiveEvent<String> = MutableLiveEvent()
    val canLogin: MutableLiveData<Boolean> = MutableLiveData()

    fun onEmailChanged(text: CharSequence) {
        email.value = text.toString()
    }

    fun onPasswordChanged(text: CharSequence) {
        password.value = text.toString()
    }

    fun onHaveAccountClicked() {
        navigationEvent.postValue(
            ToolControlNavigationEvent()
        )
    }

    fun checkIfCanLogin(loginBox: String, passwordBox: String) {
        if (loginBox.isBlank() || passwordBox.isBlank()) {
            canLogin.value = false
            showToastEvent.postValue("Email or Password Incorrect")
        } else
            canLogin.value = true
    }

    fun onClickButtonRegister() {
        if (canLogin.value == true) {
            performRegister()
        } else
            showToastEvent.postValue("Error")
    }

    fun performRegister() {
        val emailRegister = email.value.toString()
        val passwordRegister = password.value.toString()
        checkIfCanLogin(emailRegister, passwordRegister)
        signupService.registerInFirebase(
            emailRegister, passwordRegister,
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
