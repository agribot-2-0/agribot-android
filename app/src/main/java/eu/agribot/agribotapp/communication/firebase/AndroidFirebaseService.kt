package eu.agribot.agribotapp.communication.firebase

import com.google.firebase.auth.FirebaseAuth

class AndroidFirebaseService : FirebaseService {
    var mAuth = FirebaseAuth.getInstance()

    override fun login(
        loginEmail: String,
        loginPassword: String,
        onSucess: () -> Unit,
        onError: () -> Unit
    ) {
        mAuth.signInWithEmailAndPassword(loginEmail, loginPassword)
            .addOnCompleteListener {
                if (!it.isSuccessful) return@addOnCompleteListener
                onSucess()
            }
            .addOnFailureListener {
                onError()
            }
    }

    override fun register(
        emailRegister: String,
        passwordRegister: String,
        onSucess: () -> Unit,
        onError: () -> Unit
    ) {
        mAuth.createUserWithEmailAndPassword(
            emailRegister,
            passwordRegister
        )
            .addOnCompleteListener {
                if (!it.isSuccessful) return@addOnCompleteListener
                onSucess()
            }
            .addOnFailureListener {
                onError()
            }
    }

    override fun signOut(signOut: () -> Unit) {
        mAuth.signOut()
    }

    override fun sendResetPasswordEmail(
        loginEmail: String,
        onSucess: () -> Unit,
        onError: () -> Unit
    ) {
        mAuth.sendPasswordResetEmail(loginEmail)
            .addOnCompleteListener {
                if (!it.isSuccessful) return@addOnCompleteListener
                onSucess()
            }
            .addOnFailureListener {
                onError()
            }
    }
}
