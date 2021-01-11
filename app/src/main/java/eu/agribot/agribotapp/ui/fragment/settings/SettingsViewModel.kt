package eu.agribot.agribotapp.ui.fragment.settings

import androidx.lifecycle.MutableLiveData
import eu.agribot.agribotapp.R
import eu.agribot.agribotapp.base.BaseViewModel
import eu.agribot.agribotapp.common.livedata.LiveEvent
import eu.agribot.agribotapp.common.livedata.MutableLiveEvent
import eu.agribot.agribotapp.common.navigation.events.BluetoothConnectionNavigationEvent
import eu.agribot.agribotapp.common.navigation.events.WelcomeActivityNavigationEvent
import eu.agribot.agribotapp.common.resources.ResourcesProvider
import eu.agribot.agribotapp.domain.service.AuthenticationService
import eu.agribot.agribotapp.domain.service.BluetoothConnectionService
import eu.agribot.agribotapp.ui.fragment.settings.recycler.ItemID
import eu.agribot.agribotapp.ui.fragment.settings.recycler.settingsaction.SettingsActionViewModel
import eu.agribot.agribotapp.ui.fragment.settings.recycler.settingslist.SettingsListViewModel

class SettingsViewModel(
    private val authenticationService: AuthenticationService,
    private val resourcesProvider: ResourcesProvider,
    private val bluetoothConnectionService: BluetoothConnectionService
) : BaseViewModel() {
    val settingsList: MutableLiveData<List<SettingsListViewModel>> = MutableLiveData(listOf())
    val settingsActions: MutableLiveData<List<SettingsActionViewModel>> = MutableLiveData(listOf())

    val toastEvent: LiveEvent<String> = MutableLiveEvent()
    private val user = authenticationService.getCurrentUser().userEmail
    val showDeleteAccountDialogEvent: LiveEvent<Boolean> = MutableLiveEvent()
    val showSignoutDialogEvent: MutableLiveEvent<Boolean> = MutableLiveEvent()

    fun logOut() {
        authenticationService.logOut(signOutInformation = this::logOutInfo)
        postWelcomeActivityEvent()
    }

    fun prepareSettingsItem() {
        settingsList.value = listOf(
            SettingsListViewModel(
                ItemID.LOGIN_AS.ID,
                resourcesProvider.getString(R.string.login_As_Text) + user
            ),
            SettingsListViewModel(
                ItemID.SIGN_OUT.ID,
                resourcesProvider.getString(R.string.sign_out)
            ),
            SettingsListViewModel(
                ItemID.DELETE_ACCOUNT.ID,
                resourcesProvider.getString(R.string.delete_account)
            ),
            SettingsListViewModel(
                ItemID.CHANGE_URL.ID,
                resourcesProvider.getString(R.string.change_stream_url)
            )
        )
    }

    fun prepareSettingsAction() {
        settingsActions.value = listOf(
            SettingsActionViewModel(
                ItemID.SEARCH_DEVICE.ID,
                resourcesProvider.getString(R.string.find_agribot)
            ),
            SettingsActionViewModel(
                ItemID.CONNECT_WITH_LAST_DEVICE.ID,
                resourcesProvider.getString(R.string.connect_with_last_used_device)
            ),
            SettingsActionViewModel(
                ItemID.DISCONNECT.ID,
                resourcesProvider.getString(R.string.disconnect_button)
            )
        )
    }

    fun onSettingsItemSelected(itemID: Int) {
        when (itemID) {
            ItemID.SIGN_OUT.ID -> {
                onUserSignOut()
            }

            ItemID.DELETE_ACCOUNT.ID -> {
                onUserRemove()
            }

            ItemID.CHANGE_URL.ID -> {
                onStreamUrlChange()
            }
        }
    }

    private fun onStreamUrlChange() {
    }

    fun onSettingsActionSelected(actionID: Int) {
        when (actionID) {
            ItemID.SEARCH_DEVICE.ID -> {
                onSearchBluetoothDeviceClicked()
            }

            ItemID.CONNECT_WITH_LAST_DEVICE.ID -> {
            }

            ItemID.DISCONNECT.ID -> {
                onDisconnectBluetoothDeviceClicked()
            }
        }
    }

    private fun removeUserSucessfull() {
        toastEvent.postValue(resourcesProvider.getString(R.string.toast_remove_account_sucess))
        postWelcomeActivityEvent()
    }

    private fun removeUserFailed(removeUserProblem: String) {
        toastEvent.postValue(removeUserProblem)
    }

    private fun onUserSignOut() {
        showSignoutDialogEvent.postValue(true)
    }

    private fun onUserRemove() {
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

    private fun onSearchBluetoothDeviceClicked() {
        navigationEvent.postValue(
            BluetoothConnectionNavigationEvent()
        )
    }

    private fun onDisconnectBluetoothDeviceClicked() {
        bluetoothConnectionService.disconnect()
        toastEvent.postValue(resourcesProvider.getString(R.string.disconnected))
    }
}
