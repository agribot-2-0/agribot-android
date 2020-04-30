package eu.agribot.agribotapp.ui.fragment.settings

import eu.agribot.agribotapp.base.BaseViewModel
import eu.agribot.agribotapp.common.livedata.LiveEvent
import eu.agribot.agribotapp.common.livedata.MutableLiveEvent
import eu.agribot.agribotapp.common.navigation.events.WelcomeActivityNavigationEvent
import eu.agribot.agribotapp.domain.service.AuthenticationService

class SettingsViewModel(private val authenticationService: AuthenticationService) : BaseViewModel() {
    val showToastEvent: LiveEvent<String> = MutableLiveEvent()
    val user = "Logged in as: " + authenticationService.getCurrentUser().userEmail.toString()
    val showSignoutDialogEvent: LiveEvent<Boolean> = MutableLiveEvent()
    val showDeleteAccountDialogEvent: LiveEvent<Boolean> = MutableLiveEvent()

    fun logOut() {
        authenticationService.logOutFromFirebase(signOutInformation = this::logOutInfo)
        welcomeActivityEvent()
    }

    fun removeUserSucessfull() {
        showToastEvent.postValue("Remove your account sucessfully")
        welcomeActivityEvent()
    }

    fun removeUserFailed() {
        showToastEvent.postValue("Remove your account failed")
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
            showToastEvent.postValue("Log out from application sucessfull")
    }

    fun removeTextClicked() {
        showDeleteAccountDialogEvent.postValue(true)
    }

    fun removeUser() {
        authenticationService.removeUserFromFirebase(onSucessRemove = this::removeUserSucessfull,
            onErrorRemove = this::removeUserFailed)
    }
}
