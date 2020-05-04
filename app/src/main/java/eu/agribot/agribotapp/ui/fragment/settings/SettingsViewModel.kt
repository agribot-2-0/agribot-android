package eu.agribot.agribotapp.ui.fragment.settings

import eu.agribot.agribotapp.R
import eu.agribot.agribotapp.base.BaseViewModel
import eu.agribot.agribotapp.common.livedata.LiveEvent
import eu.agribot.agribotapp.common.livedata.MutableLiveEvent
import eu.agribot.agribotapp.common.navigation.events.WelcomeActivityNavigationEvent
import eu.agribot.agribotapp.common.resources.ResourcesProvider
import eu.agribot.agribotapp.domain.service.AuthenticationService

class SettingsViewModel(
    private val authenticationService: AuthenticationService,
    private val resourcesProvider: ResourcesProvider
) : BaseViewModel() {
    val showToastEvent: LiveEvent<String> = MutableLiveEvent()
    val user =
        resourcesProvider.getString(R.string.loginAsText) + " " + authenticationService.getCurrentUser().userEmail.toString()
    val showSignoutDialogEvent: LiveEvent<Boolean> = MutableLiveEvent()
    val showDeleteAccountDialogEvent: LiveEvent<Boolean> = MutableLiveEvent()

    fun logOut() {
        authenticationService.logOutFromFirebase(signOutInformation = this::logOutInfo)
        welcomeActivityEvent()
    }

    fun removeUserSucessfull() {
        showToastEvent.postValue(resourcesProvider.getString(R.string.toast_remove_account_sucess))
        welcomeActivityEvent()
    }

    fun removeUserFailed() {
        showToastEvent.postValue(resourcesProvider.getString(R.string.toast_remove_account_failed))
    }

    fun logOutButtonAction() {
        showSignoutDialogEvent.postValue(true)
    }

    fun welcomeActivityEvent() {
        navigationEvent.postValue(
            WelcomeActivityNavigationEvent()
        )
    }

    fun logOutInfo() {
        showToastEvent.postValue(resourcesProvider.getString(R.string.toast_logout_sucess))
    }

    fun removeTextClicked() {
        showDeleteAccountDialogEvent.postValue(true)
    }

    fun removeUser() {
        authenticationService.removeUserFromFirebase(
            onSucessRemove = this::removeUserSucessfull,
            onErrorRemove = this::removeUserFailed
        )
    }
}
