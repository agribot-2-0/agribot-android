package eu.agribot.agribotapp.domain.service

import eu.agribot.agribotapp.communication.firebase.FirebaseService
import eu.agribot.agribotapp.domain.error.AuthenticationErrorHandler

class AuthenticationService(
    val firebaseService: FirebaseService,
    val authenticationErrorHandler: AuthenticationErrorHandler
) {

    fun login(
        userLogin: String,
        userPassword: String,
        loginSucess: () -> Unit,
        loginFailed: (error: String) -> Unit
    ) {
        firebaseService.login(userLogin, userPassword, loginSucess, onError = {
            loginFailed(authenticationErrorHandler.getMessageForLoginException(it))
        })
    }

    fun resetEmail(
        userLogin: String,
        confirmSending: () -> Unit,
        errorSending: (error: String) -> Unit
    ) {
        firebaseService.sendResetPasswordEmail(userLogin, confirmSending, onError = {
            errorSending(authenticationErrorHandler.getMessageForPasswordReminderException(it))
        })
    }

    fun logOut(signOutInformation: () -> Unit) {
        firebaseService.signOut { signOutInformation() }
    }

    fun register(
        userLogin: String,
        userPassword: String,
        registerSucess: () -> Unit,
        registerFailed: (error: String) -> Unit
    ) {
        firebaseService.register(userLogin, userPassword, registerSucess, onError = {
            registerFailed(authenticationErrorHandler.getMessageForSignupException(it))
        })
    }

    data class AppUser(
        val userName: String,
        val userEmail: String
    )

    fun checkUserInApp() = firebaseService.isUserAlreadyLoggedIn()

    fun getCurrentUser() = firebaseService.getCurrentUser().let { firebaseUser ->
        AppUser(
            userName = firebaseUser?.displayName.toString(),
            userEmail = firebaseUser?.email.toString()
        )
    }

    fun removeUser(onSucessRemove: () -> Unit, onErrorRemove: (error: String) -> Unit) {
        firebaseService.removeUserFromFirebase(
            onSucessRemove,
            onError = {
                onErrorRemove(authenticationErrorHandler.getMessageForSignupException(it))
            })
    }
}
