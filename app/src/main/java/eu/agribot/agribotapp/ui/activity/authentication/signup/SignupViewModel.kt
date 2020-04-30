package eu.agribot.agribotapp.ui.activity.authentication.signup

import androidx.lifecycle.MutableLiveData
import eu.agribot.agribotapp.base.BaseViewModel
import eu.agribot.agribotapp.common.livedata.LiveEvent
import eu.agribot.agribotapp.common.livedata.MutableLiveEvent
import eu.agribot.agribotapp.common.navigation.events.LoginActivityNavigationEvent
import eu.agribot.agribotapp.common.navigation.events.MainActivityNavigationEvent
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

    fun onClickButtonRegister() {
        performRegister()
    }

    fun performRegister() {
        authenticationService.registerInFirebase(
            email.value.toString(), password.value.toString(),
            registerSucess = this::onRegisterSucess,
            registerFailed = this::onRegisterError
        )
    }

    fun onRegisterSucess() {
        showToastEvent.postValue("Register sucessfully")
        authenticationService.loginToFirebase(
            email.value.toString(), password.value.toString(),
            loginSucess = this::postMainActivityNavigationEvent, loginFailed = this::onLoginFailed
        )
    }

    fun onRegisterError() {
        showToastEvent.postValue("Register Failed")
    }

    fun onLoginFailed() {
        showToastEvent.postValue("Login Failed")
    }

    fun postMainActivityNavigationEvent() {
        navigationEvent.postValue(
            MainActivityNavigationEvent()
        )
    }
}
