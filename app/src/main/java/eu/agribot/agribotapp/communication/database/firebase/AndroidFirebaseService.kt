package eu.agribot.agribotapp.communication.database.firebase

import android.annotation.SuppressLint
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import eu.agribot.agribotapp.communication.database.clouddatabase.CloudDatabase
import timber.log.Timber

class AndroidFirebaseService(
    override val firebaseAuth: FirebaseAuth
) : FirebaseService {

    @SuppressLint("TimberArgCount")
    override fun login(
        loginEmail: String,
        loginPassword: String,
        onSucess: () -> Unit,
        onError: (Exception) -> Unit
    ) {

        firebaseAuth.signInWithEmailAndPassword(loginEmail, loginPassword)
            .addOnSuccessListener {
                onSucess.invoke()
            }
            .addOnFailureListener { exception ->
                onError(exception)
                Timber.e(exception, "Authentication Error %s")
            }
    }

    override fun register(
        emailRegister: String,
        passwordRegister: String,
        onSucess: () -> Unit,
        onError: (Throwable) -> Unit

    ) {
        firebaseAuth.createUserWithEmailAndPassword(
            emailRegister,
            passwordRegister
        )
            .addOnSuccessListener {
                onSucess.invoke()
            }
            .addOnFailureListener { exception ->
                onError(exception)
            }
    }

    override fun signOut(signOut: () -> Unit) {
        firebaseAuth.signOut()
        signOut()
    }

    override fun sendResetPasswordEmail(
        loginEmail: String,
        onSucess: () -> Unit,
        onError: (Exception) -> Unit
    ) {
        firebaseAuth.sendPasswordResetEmail(loginEmail)
            .addOnSuccessListener {
                onSucess.invoke()
            }
            .addOnFailureListener { exception ->
                onError(exception)
            }
    }

    override fun removeUserFromFirebase(onSucess: () -> Unit, onError: (Exception) -> Unit) {
        firebaseAuth.currentUser?.let { nonNullUser ->
            nonNullUser.delete()
                .addOnSuccessListener { onSucess() }
                .addOnFailureListener {
                    exception -> onError(exception)
                }
        } ?: run {
            onSucess()
        }
    }
}
