package eu.agribot.agribotapp.communication.firebase

import com.google.firebase.auth.FirebaseAuth

/**
 * Interface used to communicate with Firebase
 * @see AndroidFirebaseService
 */
interface FirebaseService {
    val firebaseAuth: FirebaseAuth

    fun login(loginEmail: String, loginPassword: String, onSucess: () -> Unit, onError: () -> Unit)

    fun register(emailRegister: String, passwordRegister: String, onSucess: () -> Unit, onError: () -> Unit)

    fun signOut(signOut: () -> Unit)

    fun sendResetPasswordEmail(loginEmail: String, onSucess: () -> Unit, onError: () -> Unit)

    fun getCurrentUser() = firebaseAuth.currentUser

    fun isUserAlreadyLogged(isUserInApp: () -> Unit, noUserInApp: () -> Unit)

    fun removeUserFromFirebase(onSucess: () -> Unit, onError: () -> Unit)
}
