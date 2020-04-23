package eu.agribot.agribotapp.communication.firebase

import android.util.Log
import com.google.firebase.auth.FirebaseAuth

class AndroidFirebaseService : FirebaseService {
    var mAuth = FirebaseAuth.getInstance()

    override fun login(loginEmail: String, loginPassword: String) {
        mAuth.signInWithEmailAndPassword(loginEmail, loginPassword)
            .addOnCompleteListener {

                if (!it.isSuccessful) return@addOnCompleteListener
                Log.e("xd", "Successfully login user with uid: ${it.result?.user?.uid}")
            }
            .addOnFailureListener {
                Log.e("xd", "Failed to login user: ${it.message}")
            }
    }

    override fun register(emailRegister: String, passwordRegister: String) {
        mAuth.createUserWithEmailAndPassword(
            emailRegister,
            passwordRegister
        )
            .addOnCompleteListener {
                if (!it.isSuccessful) return@addOnCompleteListener
                Log.e("xd", "Successfully created user with uid: ${it.result?.user?.uid}")
            }
            .addOnFailureListener {
                Log.e("xd", "Failed to create user: ${it.message}")
            }
    }

    override fun signOut() {
        mAuth.signOut()
    }

    override fun sendResetPasswordEmail(loginEmail: String) {
        mAuth.sendPasswordResetEmail(loginEmail)
            .addOnCompleteListener {
                if (!it.isSuccessful) return@addOnCompleteListener
                Log.e("xd", "Successfully send Reset Email")
            }
            .addOnFailureListener {
                Log.e("xd", "Failed to send reset email")
            }
    }
}
