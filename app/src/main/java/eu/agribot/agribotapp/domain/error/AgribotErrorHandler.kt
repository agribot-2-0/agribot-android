package eu.agribot.agribotapp.domain.error

import timber.log.Timber

/**
 * Service used to handle error from agribot
 */

class AgribotErrorHandler {

    fun handleAgribotError() {
        Timber.e("Error from Agribot")
    }
}
