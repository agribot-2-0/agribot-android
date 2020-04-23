package eu.agribot.agribotapp.ui.fragment.authentication.signup

import androidx.lifecycle.MutableLiveData
import eu.agribot.agribotapp.base.BaseViewModel
import eu.agribot.agribotapp.common.navigation.events.ToolControlNavigationEvent
import eu.agribot.agribotapp.communication.firebase.FirebaseService

class SignupViewModel(private val mFirebaseService: FirebaseService) : BaseViewModel() {

    val email: MutableLiveData<String> = MutableLiveData()
    val password: MutableLiveData<String> = MutableLiveData()

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
        }
        mFirebaseService.register(emailRegister, passwordRegister)
    }
}
