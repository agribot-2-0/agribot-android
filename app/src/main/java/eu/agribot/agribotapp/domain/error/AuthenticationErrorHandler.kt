package eu.agribot.agribotapp.domain.error

import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuthEmailException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import eu.agribot.agribotapp.R
import eu.agribot.agribotapp.common.resources.ResourcesProvider

class AuthenticationErrorHandler(val resourcesProvider: ResourcesProvider) {

    fun getMessageForLoginException(exception: Exception): String = when (exception) {
        is FirebaseAuthInvalidUserException -> resourcesProvider.getString(R.string.incorrect_email)
        is FirebaseAuthInvalidCredentialsException -> resourcesProvider.getString(R.string.incorrect_password)
        is FirebaseAuthWeakPasswordException -> resourcesProvider.getString(R.string.too_short_password)
        is FirebaseNetworkException -> resourcesProvider.getString(R.string.no_internet_connection)
        else -> resourcesProvider.getString(R.string.unkown_error)
    }

    fun getMessageForSignupException(exception: Throwable): String = when (exception) {
        is FirebaseAuthInvalidCredentialsException -> resourcesProvider.getString(R.string.error_signup_data)
        is FirebaseAuthUserCollisionException -> resourcesProvider.getString(R.string.email_already_used)
        is FirebaseNetworkException -> resourcesProvider.getString(R.string.no_internet_connection)
        else -> resourcesProvider.getString(R.string.unkown_error)
    }

    fun getMessageForPasswordReminderException(exception: Exception): String = when (exception) {
        is FirebaseAuthEmailException -> resourcesProvider.getString(R.string.incorrect_email)
        is FirebaseAuthInvalidCredentialsException -> resourcesProvider.getString(R.string.badly_email_formated)
        is FirebaseNetworkException -> resourcesProvider.getString(R.string.no_internet_connection)
        else -> resourcesProvider.getString(R.string.unkown_error)
    }
}
