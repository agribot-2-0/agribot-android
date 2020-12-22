package eu.agribot.agribotapp.domain.error

import eu.agribot.agribotapp.R
import eu.agribot.agribotapp.common.resources.ResourcesProvider
import java.lang.Exception

class PathManagementErrorHandler(val resourcesProvider: ResourcesProvider) {

    // TODO ADD EXCEPTION
    fun getCreateKmlError(exception: Throwable): String = when (exception) {
        is Exception -> resourcesProvider.getString(R.string.incorrect_email)
        else -> resourcesProvider.getString(R.string.unkown_error)
    }
}
