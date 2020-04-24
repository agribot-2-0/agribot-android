package eu.agribot.agribotapp.communication.firebase

/**
 * Interface used to communicate with Firebase
 * @see AndroidFirebaseService
 */
interface FirebaseService {

    fun login(loginEmail: String, loginPassword: String, onSucess: () -> Unit, onError: () -> Unit)

    fun register(emailRegister: String, passwordRegister: String, onSucess: () -> Unit, onError: () -> Unit)

    fun signOut(signOut: () -> Unit)

    fun sendResetPasswordEmail(loginEmail: String, onSucess: () -> Unit, onError: () -> Unit)
}
