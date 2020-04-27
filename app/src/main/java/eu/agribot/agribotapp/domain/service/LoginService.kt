package eu.agribot.agribotapp.domain.service

import eu.agribot.agribotapp.communication.firebase.FirebaseService

class LoginService(
    private val firebaseService: FirebaseService
) {

    fun loginToFirebase(
        userLogin: String, userPassword: String, loginSucess: () -> Unit,
        loginFailed: () -> Unit
    ) {
        firebaseService.login(userLogin, userPassword, loginSucess, loginFailed)
    }

    fun resetEmailfromFirebase(
        userLogin: String, confirmSending: () -> Unit,
        errorSending: () -> Unit
    ) {
        firebaseService.sendResetPasswordEmail(userLogin, confirmSending, errorSending)
    }

    fun logOutFromFirebase(signOutInformation: () -> Unit) {
        firebaseService.signOut { signOutInformation() }
    }
}