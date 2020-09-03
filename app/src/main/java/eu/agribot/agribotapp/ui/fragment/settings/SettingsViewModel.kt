package eu.agribot.agribotapp.ui.fragment.settings

import eu.agribot.agribotapp.R
import eu.agribot.agribotapp.base.BaseViewModel
import eu.agribot.agribotapp.common.livedata.LiveEvent
import eu.agribot.agribotapp.common.livedata.MutableLiveEvent
import eu.agribot.agribotapp.common.navigation.events.BluetoothConnectionNavigationEvent
import eu.agribot.agribotapp.common.navigation.events.WelcomeActivityNavigationEvent
import eu.agribot.agribotapp.common.resources.ResourcesProvider
import eu.agribot.agribotapp.domain.service.AuthenticationService

class SettingsViewModel(
    private val authenticationService: AuthenticationService,
    private val resourcesProvider: ResourcesProvider
) : BaseViewModel() {
    val showToastEvent: LiveEvent<String> = MutableLiveEvent()
    val user =
        resourcesProvider.getString(R.string.login_As_Text) + " " + authenticationService.getCurrentUser().userEmail
    val showSignoutDialogEvent: LiveEvent<Boolean> = MutableLiveEvent()
    val showDeleteAccountDialogEvent: LiveEvent<Boolean> = MutableLiveEvent()

    fun logOut() {
        authenticationService.logOut(signOutInformation = this::logOutInfo)
        postWelcomeActivityEvent()
    }

    private fun removeUserSucessfull() {
        showToastEvent.postValue(resourcesProvider.getString(R.string.toast_remove_account_sucess))
        postWelcomeActivityEvent()
    }

    private fun removeUserFailed(removeUserProblem: String) {
        showToastEvent.postValue(removeUserProblem)
    }

    fun logOutButtonAction() {
        showSignoutDialogEvent.postValue(true)
    }

    private fun postWelcomeActivityEvent() {
        navigationEvent.postValue(
            WelcomeActivityNavigationEvent()
        )
    }

    private fun logOutInfo() {
        showToastEvent.postValue(resourcesProvider.getString(R.string.toast_logout_sucess))
    }

    fun removeTextClicked() {
        showDeleteAccountDialogEvent.postValue(true)
    }

    fun removeUser() {
        authenticationService.removeUser(
            onSucessRemove = this::removeUserSucessfull,
            onErrorRemove = this::removeUserFailed
        )
    }

    fun onToolBluetoothConnectClicked() {
        navigationEvent.postValue(
            BluetoothConnectionNavigationEvent()
        )
    }

    fun disconnect() {
        showToastEvent.postValue("Now Agribot is disconnected")
    }
}
