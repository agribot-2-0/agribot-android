package eu.agribot.agribotapp.ui.fragment.authentication.login

import androidx.lifecycle.MutableLiveData
import eu.agribot.agribotapp.base.BaseViewModel
import eu.agribot.agribotapp.communication.firebase.FirebaseService

class LoginViewModel(private val mFirebaseService: FirebaseService) : BaseViewModel() {

    val email: MutableLiveData<String> = MutableLiveData()
    val password: MutableLiveData<String> = MutableLiveData()

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

    private fun login() {
        val loginEmail = email.value.toString()
        val loginPassword = password.value.toString()
        mFirebaseService.login(loginEmail, loginPassword)
    }
}
