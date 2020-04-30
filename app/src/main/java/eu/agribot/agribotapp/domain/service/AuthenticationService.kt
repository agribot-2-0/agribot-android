package eu.agribot.agribotapp.domain.service

import eu.agribot.agribotapp.communication.firebase.FirebaseService

class AuthenticationService(val firebaseService: FirebaseService) {

    fun loginToFirebase(
        userLogin: String,
        userPassword: String,
        loginSucess: () -> Unit,
        loginFailed: () -> Unit
    ) {
        firebaseService.login(userLogin, userPassword, loginSucess, loginFailed)
    }

    fun resetEmailfromFirebase(
        userLogin: String,
        confirmSending: () -> Unit,
        errorSending: () -> Unit
    ) {
        firebaseService.sendResetPasswordEmail(userLogin, confirmSending, errorSending)
    }

    fun logOutFromFirebase(signOutInformation: () -> Unit) {
        firebaseService.signOut { signOutInformation() }
    }

    fun registerInFirebase(
        userLogin: String,
        userPassword: String,
        registerSucess: () -> Unit,
        registerFailed: () -> Unit
    ) {
        firebaseService.register(userLogin, userPassword, registerSucess, registerFailed)
    }

    data class AppUser(
        val userName: String,
        val userEmail: String
    )

    fun checkUserInApp(
        isInApp: () -> Unit,
        isNotInApp: () -> Unit
    ) {
        firebaseService.isUserAlreadyLogged(isInApp, isNotInApp)
    }

    fun getCurrentUser() = firebaseService.getCurrentUser().let { firebaseUser ->
        AppUser(
            userName = firebaseUser?.displayName.toString(),
            userEmail = firebaseUser?.email.toString()
        )
    }
}
