package eu.agribot.agribotapp.domain.error

import timber.log.Timber

class AgribotErrorHandler {

    fun handleAgribotError() {
        Timber.e("Error from Agribot")
    }
}
