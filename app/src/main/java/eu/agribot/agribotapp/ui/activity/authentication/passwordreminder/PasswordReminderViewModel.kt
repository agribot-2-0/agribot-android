package eu.agribot.agribotapp.ui.activity.authentication.passwordreminder

import androidx.lifecycle.MutableLiveData
import eu.agribot.agribotapp.R
import eu.agribot.agribotapp.base.BaseViewModel
import eu.agribot.agribotapp.common.livedata.LiveEvent
import eu.agribot.agribotapp.common.livedata.MutableLiveEvent
import eu.agribot.agribotapp.common.navigation.events.LoginActivityNavigationEvent
import eu.agribot.agribotapp.common.resources.ResourcesProvider
import eu.agribot.agribotapp.domain.service.AuthenticationService

class PasswordReminderViewModel(
    private val authenticationService: AuthenticationService,
    private val resourcesProvider: ResourcesProvider

) : BaseViewModel() {
    val email: MutableLiveData<String> = MutableLiveData()
    val showToastEvent: LiveEvent<String> = MutableLiveEvent()

    fun onEmailChanged(text: CharSequence) {
        email.value = text.toString()
    }

    private fun remindPassword() {
        authenticationService.resetEmail(
            email.value.toString(),
            confirmSending = this::confirmSendEmail,
            errorSending = this::failedSendEmail
        )
    }

    fun onButtonNextClicked() {
        remindPassword()
    }

    private fun confirmSendEmail() {
        postLoginActivityNavigationEvent()
        showToastEvent.postValue(resourcesProvider.getString(R.string.toast_email_send_sucess))
    }

    private fun failedSendEmail(sendMailError: String) {
        showToastEvent.postValue(sendMailError)
    }

    fun postLoginActivityNavigationEvent() {
        navigationEvent.postValue(
            LoginActivityNavigationEvent()
        )
    }
}
