package eu.agribot.agribotapp.domain.service

import eu.agribot.agribotapp.communication.firebase.FirebaseService

class SignupService(
    private val firebaseService: FirebaseService
) {
    fun registerInFirebase(
        userLogin: String, userPassword: String, registerSucess: () -> Unit,
        registerFailed: () -> Unit
    ) {
        firebaseService.register(userLogin, userPassword, registerSucess, registerFailed)
    }
}