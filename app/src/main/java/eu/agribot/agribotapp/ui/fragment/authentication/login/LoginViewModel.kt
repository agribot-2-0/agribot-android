package eu.agribot.agribotapp.ui.fragment.authentication.login

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import eu.agribot.agribotapp.base.BaseViewModel

class LoginViewModel : BaseViewModel() {

    val email: MutableLiveData<String> = MutableLiveData()
    val password: MutableLiveData<String> = MutableLiveData()

    fun onEmailChanged(text: CharSequence) {
        email.value = text.toString()
        Log.e("xd", "email from textEdit" + email.value)
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
        Log.e("xd", "Attempt login with email/pw: $email/***")
        FirebaseAuth.getInstance().signInWithEmailAndPassword(loginEmail, loginPassword)
            .addOnCompleteListener {
                if (!it.isSuccessful) return@addOnCompleteListener

                // else if successful
                Log.e("xd", "Successfully login user with uid: ${it.result?.user?.uid}")
            }
            .addOnFailureListener {
                Log.e("xd", "Failed to login user: ${it.message}")

            }
    }

}