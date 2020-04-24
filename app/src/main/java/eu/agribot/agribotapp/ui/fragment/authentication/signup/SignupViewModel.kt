package eu.agribot.agribotapp.ui.fragment.authentication.signup

import androidx.lifecycle.MutableLiveData
import eu.agribot.agribotapp.base.BaseViewModel
import eu.agribot.agribotapp.common.livedata.LiveEvent
import eu.agribot.agribotapp.common.livedata.MutableLiveEvent
import eu.agribot.agribotapp.common.navigation.events.ToolControlNavigationEvent
import eu.agribot.agribotapp.communication.firebase.FirebaseService

class SignupViewModel(
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

    fun onHaveAccountClicked() {
        navigationEvent.postValue(
            ToolControlNavigationEvent()
        )
    }

    fun performRegister() {
        val emailRegister = email.value.toString()
        val passwordRegister = password.value.toString()
        if (emailRegister.isBlank() || passwordRegister.isBlank()) {
            showToastEvent.postValue("Email or Password Incorrect")
        }
        firebaseService.register(
            emailRegister = emailRegister,
            passwordRegister = passwordRegister,
            onSucess = this::onRegisterSucess,
            onError = this::onRegisterError
        )
    }

    fun onRegisterSucess() {
        showToastEvent.postValue("Register sucessfully")
    }

    fun onRegisterError() {
        showToastEvent.postValue("Register Failed")
    }
}
