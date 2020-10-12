package eu.agribot.agribotapp.ui.fragment.settings

import eu.agribot.agribotapp.R
import eu.agribot.agribotapp.base.BaseViewModel
import eu.agribot.agribotapp.common.livedata.LiveEvent
import eu.agribot.agribotapp.common.livedata.MutableLiveEvent
import eu.agribot.agribotapp.common.navigation.events.BluetoothConnectionNavigationEvent
import eu.agribot.agribotapp.common.navigation.events.WelcomeActivityNavigationEvent
import eu.agribot.agribotapp.common.resources.ResourcesProvider
import eu.agribot.agribotapp.domain.service.AuthenticationService
import eu.agribot.agribotapp.domain.service.BluetoothConnectionService

class SettingsViewModel(
    private val authenticationService: AuthenticationService,
    private val resourcesProvider: ResourcesProvider,
    private val bluetoothConnectionService: BluetoothConnectionService
) : BaseViewModel() {
    val toastEvent: LiveEvent<String> = MutableLiveEvent()
    val user =
        resourcesProvider.getString(R.string.login_As_Text) + " " + authenticationService.getCurrentUser().userEmail
    val showDeleteAccountDialogEvent: LiveEvent<Boolean> = MutableLiveEvent()
    val showSignoutDialogEvent: MutableLiveEvent<Boolean> = MutableLiveEvent()

    fun logOut() {
        authenticationService.logOut(signOutInformation = this::logOutInfo)
        postWelcomeActivityEvent()
    }

    private fun removeUserSucessfull() {
        toastEvent.postValue(resourcesProvider.getString(R.string.toast_remove_account_sucess))
        postWelcomeActivityEvent()
    }

    private fun removeUserFailed(removeUserProblem: String) {
        toastEvent.postValue(removeUserProblem)
    }

    fun logOutButtonAction() {
        showSignoutDialogEvent.postValue(true)
    }

    fun removeTextClicked() {
        showDeleteAccountDialogEvent.postValue(true)
    }

    private fun postWelcomeActivityEvent() {
        navigationEvent.postValue(
            WelcomeActivityNavigationEvent()
        )
    }

    private fun logOutInfo() {
        toastEvent.postValue(resourcesProvider.getString(R.string.toast_logout_sucess))
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
        bluetoothConnectionService.disconnect()
        toastEvent.postValue(resourcesProvider.getString(R.string.disconnected))
    }
}
