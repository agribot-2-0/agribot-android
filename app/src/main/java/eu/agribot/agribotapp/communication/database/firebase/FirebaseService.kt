package eu.agribot.agribotapp.communication.database.firebase

import com.google.firebase.auth.FirebaseAuth

/**
 * Interface used to communicate with Firebase
 * @see AndroidFirebaseService
 */
interface FirebaseService {
    val firebaseAuth: FirebaseAuth
    fun login(
        loginEmail: String,
        loginPassword: String,
        onSucess: () -> Unit,
        onError: (Exception) -> Unit
    )

    fun register(
        emailRegister: String,
        passwordRegister: String,
        onSucess: () -> Unit,
        onError: (Exception) -> Unit
    )

    fun signOut(signOut: () -> Unit)

    fun sendResetPasswordEmail(
        loginEmail: String,
        onSucess: () -> Unit,
        onError: (Exception) -> Unit
    )

    fun getCurrentUser() = firebaseAuth.currentUser

    fun isUserAlreadyLoggedIn() = (firebaseAuth.currentUser != null)

    fun removeUserFromFirebase(onSucess: () -> Unit, onError: (Exception) -> Unit)
}
