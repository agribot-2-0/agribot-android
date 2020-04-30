package eu.agribot.agribotapp.ui.fragment.settings

import eu.agribot.agribotapp.base.BaseViewModel
import eu.agribot.agribotapp.common.livedata.LiveEvent
import eu.agribot.agribotapp.common.livedata.MutableLiveEvent
import eu.agribot.agribotapp.domain.service.AuthenticationService

class SettingsViewModel(private val authenticationService: AuthenticationService) : BaseViewModel() {
    val showToastEvent: LiveEvent<String> = MutableLiveEvent()
    val user = authenticationService.getCurrentUser()

    fun logOutButtonAction() {
        authenticationService.logOutFromFirebase(signOutInformation = this::logOutInfo)
    }

    fun logOutInfo() {
            showToastEvent.postValue("Log out from application sucessfull")
    }

    fun setCurrentUser() {
        user.userEmail.toString()
    }
}
