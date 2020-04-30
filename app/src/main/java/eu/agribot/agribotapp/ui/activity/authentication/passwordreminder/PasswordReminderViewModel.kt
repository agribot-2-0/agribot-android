package eu.agribot.agribotapp.ui.activity.authentication.passwordreminder

import androidx.lifecycle.MutableLiveData
import eu.agribot.agribotapp.base.BaseViewModel
import eu.agribot.agribotapp.common.livedata.LiveEvent
import eu.agribot.agribotapp.common.livedata.MutableLiveEvent
import eu.agribot.agribotapp.common.navigation.events.LoginActivityNavigationEvent
import eu.agribot.agribotapp.domain.service.AuthenticationService

class PasswordReminderViewModel(
    private val authenticationService: AuthenticationService

) : BaseViewModel() {
    val email: MutableLiveData<String> = MutableLiveData()
    val showToastEvent: LiveEvent<String> = MutableLiveEvent()

    fun onEmailChanged(text: CharSequence) {
        email.value = text.toString()
    }

    private fun remindPassword() {
        authenticationService.resetEmailfromFirebase(
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
        showToastEvent.postValue("Email sent sucessfully")
    }

    private fun failedSendEmail() {
        showToastEvent.postValue("Can't send Email")
    }

    fun postLoginActivityNavigationEvent() {
        navigationEvent.postValue(
            LoginActivityNavigationEvent()
        )
    }
}
