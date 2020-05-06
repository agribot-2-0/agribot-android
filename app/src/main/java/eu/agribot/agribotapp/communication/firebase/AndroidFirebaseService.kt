package eu.agribot.agribotapp.communication.firebase

import com.google.firebase.auth.FirebaseAuth

class AndroidFirebaseService(
    override val firebaseAuth: FirebaseAuth
) : FirebaseService {

    override fun login(
        loginEmail: String,
        loginPassword: String,
        onSucess: () -> Unit,
        onError: (Exception) -> Unit
    ) {

        firebaseAuth.signInWithEmailAndPassword(loginEmail, loginPassword)
            .addOnSuccessListener { task ->
                onSucess.invoke()
            }
            .addOnFailureListener { exception ->
                onError(exception)
            }
    }

    override fun register(
        emailRegister: String,
        passwordRegister: String,
        onSucess: () -> Unit,
        onError: (Exception) -> Unit

    ) {
        firebaseAuth.createUserWithEmailAndPassword(
            emailRegister,
            passwordRegister
        )
            .addOnSuccessListener { task ->
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
            .addOnSuccessListener { task ->
                onSucess.invoke()
            }
            .addOnFailureListener { exception ->
                onError(exception)
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

    override fun removeUserFromFirebase(onSucess: () -> Unit, onError: (Exception) -> Unit) {
        firebaseAuth.currentUser?.delete()
            ?.addOnSuccessListener { task ->
                onSucess.invoke()
            }
            ?.addOnFailureListener { exception ->
                onError(exception)
            }
    }
}
