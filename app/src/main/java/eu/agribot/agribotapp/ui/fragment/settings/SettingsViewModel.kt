package eu.agribot.agribotapp.ui.fragment.settings

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import eu.agribot.agribotapp.R
import eu.agribot.agribotapp.base.BaseViewModel
import eu.agribot.agribotapp.common.livedata.LiveEvent
import eu.agribot.agribotapp.common.livedata.MutableLiveEvent
import eu.agribot.agribotapp.common.navigation.events.BluetoothConnectionNavigationEvent
import eu.agribot.agribotapp.common.navigation.events.WelcomeActivityNavigationEvent
import eu.agribot.agribotapp.common.resources.ResourcesProvider
import eu.agribot.agribotapp.domain.service.AppStorageService
import eu.agribot.agribotapp.domain.service.AuthenticationService
import eu.agribot.agribotapp.domain.service.BluetoothConnectionService
import eu.agribot.agribotapp.domain.service.InternetConnectionService
import eu.agribot.agribotapp.ui.fragment.settings.recycler.settingsaction.SettingsActionViewModel
import eu.agribot.agribotapp.ui.fragment.settings.recycler.settingslist.ItemID
import eu.agribot.agribotapp.ui.fragment.settings.recycler.settingslist.SettingsListViewModel
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val authenticationService: AuthenticationService,
    private val resourcesProvider: ResourcesProvider,
    private val appStorageService: AppStorageService,
    private val bluetoothConnectionService: BluetoothConnectionService,
    private val internetConnectionService: InternetConnectionService

) : BaseViewModel() {
    val settingsList: MutableLiveData<List<SettingsListViewModel>> = MutableLiveData(listOf())
    val settingsActions: MutableLiveData<List<SettingsActionViewModel>> = MutableLiveData(listOf())
    val changeStreamUrlEvent: MutableLiveEvent<String> = MutableLiveEvent()
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
                ItemID.ACCOUNT.ID,
                resourcesProvider.getString(R.string.settings_category_user),
                resourcesProvider.getString(R.string.settiongs_account_description)
            ),
            SettingsListViewModel(
                ItemID.CONNECTION.ID,
                resourcesProvider.getString(R.string.settings_category_connection),
                resourcesProvider.getString(R.string.settings_connection_description)
            ),
            SettingsListViewModel(
                ItemID.OTHER.ID,
                resourcesProvider.getString(R.string.settings_category_other),
                resourcesProvider.getString(R.string.settiongs_other_description)
            )
        )
    }

    private fun onWakeupButtonCLicked(onError: (error: String) -> Unit) = viewModelScope.launch {
        internetConnectionService.connectWithAgribot(onConnectionError = onError)
    }

    private fun onWakeupError(errorMessage: String) {
        toastEvent.postValue(errorMessage)
    }

    fun onSettingsItemSelected(itemID: Int) {
        when (itemID) {
            ItemID.CONNECTION.ID -> {
                setConnectionSettingsDetails()
            }

            ItemID.ACCOUNT.ID -> {
                setUserSettingsDetails()
            }

            ItemID.OTHER.ID -> {
                setOtherSettingsDetails()
            }
        }
    }

    private fun setConnectionSettingsDetails() {
        settingsActions.value = listOf(
            SettingsActionViewModel(
                ItemID.SEARCH_BLUETOOTH_DEVICE.ID,
                resourcesProvider.getString(R.string.find_agribot)
            ),
            SettingsActionViewModel(
                ItemID.DISCONNECT_BLUETOOTH_DEVICE.ID,
                resourcesProvider.getString(R.string.disconnect_button)
            ),
            SettingsActionViewModel(
                ItemID.WAKEUP_MESSAGE.ID,
                resourcesProvider.getString(R.string.wakeup_button)
            )
        )
    }

    private fun setUserSettingsDetails() {
        settingsActions.value = listOf(
            SettingsActionViewModel(
                ItemID.LOGIN_AS.ID,
                String.format(resourcesProvider.getString(R.string.login_As_Text), user)
            ),
            SettingsActionViewModel(
                ItemID.ROBOT_RSN.ID,
                String.format(resourcesProvider.getString(R.string.your_robot_text), appStorageService.getRobotSerialNumber())
            ),
            SettingsActionViewModel(
                ItemID.LOG_OUT.ID,
                resourcesProvider.getString(R.string.sign_out)
            ),
            SettingsActionViewModel(
                ItemID.REMOVE_ACCOUNT.ID,
                resourcesProvider.getString(R.string.delete_account)
            )
        )
    }

    private fun setOtherSettingsDetails() {
        settingsActions.value = listOf(
            SettingsActionViewModel(
                ItemID.CHANGE_URL_STREAM.ID,
                resourcesProvider.getString(R.string.change_video_url)
            )
        )
    }

    private fun onStreamUrlChange() {
        changeStreamUrlEvent.postValue(appStorageService.getStreamUrl())
    }

    fun onStreamUrlChanged(newUrl: String) {
        appStorageService.saveStreamUrl(newUrl)
    }

    fun onSettingsActionSelected(actionID: Int) {
        when (actionID) {
            ItemID.SEARCH_BLUETOOTH_DEVICE.ID -> {
                onSearchBluetoothDeviceClicked()
            }

            ItemID.DISCONNECT_BLUETOOTH_DEVICE.ID -> {
                onDisconnectBluetoothDeviceClicked()
            }
            ItemID.LOG_OUT.ID -> {
                onUserSignOut()
            }

            ItemID.REMOVE_ACCOUNT.ID -> {
                onUserRemove()
            }
            ItemID.CHANGE_URL_STREAM.ID -> {
                onStreamUrlChange()
            }
            ItemID.WAKEUP_MESSAGE.ID -> {
                onWakeupButtonCLicked(onError = this::onWakeupError)
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
