package eu.agribot.agribotapp.communication.firebase

import com.google.firebase.auth.FirebaseAuth

class AndroidFirebaseService(override val firebaseAuth: FirebaseAuth) : FirebaseService {

    override fun login(
        loginEmail: String,
        loginPassword: String,
        onSucess: () -> Unit,
        onError: () -> Unit
    ) {
        firebaseAuth.signInWithEmailAndPassword(loginEmail, loginPassword)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) onSucess.invoke()
                else onError.invoke()
            }
            .addOnFailureListener {
                onError.invoke()
            }
    }

    override fun register(
        emailRegister: String,
        passwordRegister: String,
        onSucess: () -> Unit,
        onError: () -> Unit
    ) {
        firebaseAuth.createUserWithEmailAndPassword(
            emailRegister,
            passwordRegister
        )
            .addOnCompleteListener { task ->
                if (task.isSuccessful) onSucess.invoke()
                else onError.invoke()
            }
            .addOnFailureListener {
                onError.invoke()
            }
    }

    override fun signOut(signOut: () -> Unit) {
        firebaseAuth.signOut()
        signOut()
    }

    override fun sendResetPasswordEmail(
        loginEmail: String,
        onSucess: () -> Unit,
        onError: () -> Unit
    ) {
        firebaseAuth.sendPasswordResetEmail(loginEmail)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) onSucess.invoke()
                else onError.invoke()
            }
            .addOnFailureListener {
                onError.invoke()
            }
    }

    override fun isUserAlreadyLogged(isUserInApp: () -> Unit, noUserInApp: () -> Unit) {
        if (firebaseAuth.currentUser != null) {
            isUserInApp.invoke()
        }
        if (firebaseAuth.currentUser == null) {
            noUserInApp.invoke()
        }
    }
}
