package eu.agribot.agribotapp.communication.firebase

/**
 * Interface used to communicate with Firebase
 * @see AndroidFirebaseService
 */
interface FirebaseService {

    fun login(loginEmail: String, loginPassword: String)

    fun register(emailRegister: String, passwordRegister: String)

    fun signOut()

    fun sendResetPasswordEmail(loginEmail: String)
}
