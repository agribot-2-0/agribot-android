package eu.agribot.agribotapp.domain.service

import eu.agribot.agribotapp.communication.database.clouddatabase.CloudDatabase
import eu.agribot.agribotapp.communication.database.firebase.FirebaseService
import eu.agribot.agribotapp.communication.database.firestorage.FirestorageService
import eu.agribot.agribotapp.domain.error.AuthenticationErrorHandler

class AuthenticationService(
    val firebaseService: FirebaseService,
    val authenticationErrorHandler: AuthenticationErrorHandler,
    val cloudDatabase: CloudDatabase,
    val firestorageService: FirestorageService
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

    fun setDefaultUserRole(
        onError: (error: String) -> Unit
    ) {
        cloudDatabase.setUserRole(user = getCurrentUser().userUID, onDatabaseError = {
            onError(authenticationErrorHandler.getMessageForSignupException(it))
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
        val userEmail: String,
        val userUID: String
    )

    fun checkUserInApp() = firebaseService.isUserAlreadyLoggedIn()

    fun getCurrentUser() = firebaseService.getCurrentUser().let { firebaseUser ->
        AppUser(
            userName = firebaseUser?.displayName.toString(),
            userEmail = firebaseUser?.email.toString(),
            userUID = firebaseUser?.uid.toString()

        )
    }

    fun removeUser(onSucessRemove: () -> Unit, onErrorRemove: (error: String) -> Unit) {
       cloudDatabase.removeUserDocuments()
        firestorageService.removeUserFolder()
        firebaseService.removeUserFromFirebase(
            onSucessRemove,
            onError = {
                onErrorRemove(authenticationErrorHandler.getMessageForSignupException(it))
            })
    }
}
