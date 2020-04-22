package eu.agribot.agribotapp.ui.fragment.authentication.signup

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import eu.agribot.agribotapp.base.BaseViewModel
import eu.agribot.agribotapp.common.navigation.events.ToolControlNavigationEvent


class SignupViewModel : BaseViewModel() {
    private lateinit var auth: FirebaseAuth

    val email: MutableLiveData<String> = MutableLiveData()
    val password: MutableLiveData<String> = MutableLiveData()


    fun onEmailChanged(text: CharSequence ) {
        email.value = text.toString()
        Log.e("xd", "email from textEdit" + email.value)
    }

    fun onPasswordChanged(text: CharSequence) {
        password.value = text.toString()
    }

    fun onHaveAccountClicked(){
        navigationEvent.postValue(
            ToolControlNavigationEvent()
        )
    }

     fun performRegister() {


        val emailRegister = email.value.toString()
        val passwordRegister = password.value.toString()

        if (emailRegister.isBlank() || passwordRegister.isBlank()) {
           Log.e("xd","please enter text")
        }

        Log.e("xd", "Email is: " + emailRegister)
        Log.e("xd", "Password: $password")

        // Firebase Authentication to create a user with email and password
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(emailRegister,
            passwordRegister
        )
            .addOnCompleteListener {
                if (!it.isSuccessful) return@addOnCompleteListener

                // else if successful
                Log.e("xd", "Successfully created user with uid: ${it.result?.user?.uid}")
            }
            .addOnFailureListener {
                Log.e("xd", "Failed to create user: ${it.message}")

            }
    }
}
