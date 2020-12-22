package eu.agribot.agribotapp.domain.error

import com.google.firebase.auth.FirebaseAuthInvalidUserException
import eu.agribot.agribotapp.R
import eu.agribot.agribotapp.common.resources.ResourcesProvider

class CloudStorageErrorHandler(val resourcesProvider: ResourcesProvider) {

    // TODO ADD EXCEPTION
    fun getStorageError(exception: Throwable): String = when (exception) {
        is FirebaseAuthInvalidUserException -> resourcesProvider.getString(R.string.incorrect_email)
        else -> resourcesProvider.getString(R.string.unkown_error)
    }
}
