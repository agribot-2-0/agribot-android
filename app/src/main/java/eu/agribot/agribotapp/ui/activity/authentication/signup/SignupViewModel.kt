package eu.agribot.agribotapp.ui.activity.authentication.signup

import androidx.lifecycle.MutableLiveData
import eu.agribot.agribotapp.R
import eu.agribot.agribotapp.base.BaseViewModel
import eu.agribot.agribotapp.common.livedata.LiveEvent
import eu.agribot.agribotapp.common.livedata.MutableLiveEvent
import eu.agribot.agribotapp.common.navigation.events.LoginActivityNavigationEvent
import eu.agribot.agribotapp.common.navigation.events.MainActivityNavigationEvent
import eu.agribot.agribotapp.common.resources.ResourcesProvider
import eu.agribot.agribotapp.domain.service.AuthenticationService

class SignupViewModel(
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

    fun onHaveAccountClicked() {
        navigationEvent.postValue(
            LoginActivityNavigationEvent()
        )
    }

    fun onClickButtonRegister() {
        performRegister()
    }

    private fun performRegister() {
        authenticationService.register(
            email.value.toString(), password.value.toString(),
            registerSucess = this::onRegisterSucess, registerFailed = this::onRegisterError

        )
    }

    private fun onRegisterError(registerError: String) {
        showToastEvent.postValue(registerError)
    }

    private fun onRegisterSucess() {
        showToastEvent.postValue(resourcesProvider.getString(R.string.toast_register_sucess))
        authenticationService.login(
            email.value.toString(), password.value.toString(),
            loginSucess = this::postMainActivityNavigationEvent, loginFailed = this::onLoginFailed
        )
        authenticationService.setDefaultUserRole(onError = this::onRegisterError)

    }

    private fun onLoginFailed(LoginError: String) {
        showToastEvent.postValue(LoginError)
    }

    private fun postMainActivityNavigationEvent() {
        navigationEvent.postValue(
            MainActivityNavigationEvent()
        )
    }
}
