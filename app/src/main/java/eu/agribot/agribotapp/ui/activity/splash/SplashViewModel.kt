package eu.agribot.agribotapp.ui.activity.splash

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import eu.agribot.agribotapp.R
import eu.agribot.agribotapp.base.BaseViewModel
import eu.agribot.agribotapp.common.livedata.MutableLiveEvent
import eu.agribot.agribotapp.common.navigation.events.MainActivityNavigationEvent
import eu.agribot.agribotapp.common.navigation.events.WelcomeActivityNavigationEvent
import eu.agribot.agribotapp.common.resources.ResourcesProvider
import eu.agribot.agribotapp.domain.service.AuthenticationService
import eu.agribot.agribotapp.domain.service.BluetoothConnectionService
import eu.agribot.agribotapp.domain.service.cloud.CloudDatabaseService
import eu.agribot.agribotapp.domain.service.cloud.CloudStorageService
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashViewModel(
    private val authenticationService: AuthenticationService,
    private val bluetoothConnectionService: BluetoothConnectionService,
    private val cloudDatabaseService: CloudDatabaseService,
    private val cloudStorageService: CloudStorageService,
    private val resourcesProvider: ResourcesProvider
) : BaseViewModel() {
    val postDialogEvent: MutableLiveEvent<Boolean> = MutableLiveEvent()
    val postProgressPlace: MutableLiveEvent<Boolean> = MutableLiveEvent()
    val toastMessage: MutableLiveEvent<String> = MutableLiveEvent()
    val progressBarData: MutableLiveData<Int> = MutableLiveData()
    val statusText: MutableLiveData<String> = MutableLiveData()

    companion object {
        const val SPLASH_SCREEN_TIMEOUT_MILLIS = 1000L
        const val PROGRESS_COMPLETE = 100
        const val DECIMAL_NUMBER = 10
    }

    private fun getUserRobotName() {
        cloudDatabaseService.getUserRobotName(onGotRobotName = this::onGotRobotName)
    }

    private fun onGotRobotName() {
        selectMapTiles()
    }

    private fun beginSplashDelay() = viewModelScope.launch {
        delay(SPLASH_SCREEN_TIMEOUT_MILLIS)
        checkLoginStatusAndRedirect()
    }

    private fun postMainActivityNavigationEvent() {
        navigationEvent.postValue(
            MainActivityNavigationEvent()
        )
    }

    private fun postWelcomeActivityNavigationEvent() {
        navigationEvent.postValue(WelcomeActivityNavigationEvent())
    }

    private fun checkLoginStatusAndRedirect() {
        when (authenticationService.checkUserInApp()) {
            true -> getUserRobotName()
            false -> postWelcomeActivityNavigationEvent()
        }
    }

    fun handleCurrentBluetoothStatus() {
        when (bluetoothConnectionService.checkBluetoothEnable()) {
            true -> checkLoginStatusAndRedirect()
            false -> postDialogEvent.postValue(true)
        }
    }

    fun enableBluetooth() {
        bluetoothConnectionService.enableBluetooth()
        getUserRobotName()
    }

    private fun selectMapTiles() {
        cloudStorageService.onMapTilesRequest(
            downloadProgress = this::onDownloadProgress,
            onGotMapTilesAlready = this::postMainActivityNavigationEvent,
            onDownloadedMapTiles = this::onDownloadedTiles,
            onDownloadedMapTilesError = this::onGetMapTilesError
        )
    }

    private fun onGetMapTilesError(error: String) {
        toastMessage.postValue(error)
    }

    private fun onDownloadProgress(downloadProgress: Long) {
        statusText.value = "Downloading.."
        postProgressPlace.postValue(true)
        if (downloadProgress.toInt() % DECIMAL_NUMBER == 0) {
            progressBarData.value = downloadProgress.toInt()
        }
        if (downloadProgress == 100.toLong()) {
            statusText.value = "Extracting..."
        }
    }

    private fun onDownloadedTiles() {
        cloudStorageService.extractDownloadedFile(this::onDecompressMapTileProgress)
    }

    private fun onDecompressMapTileProgress(progress: Int) {
        if (progress == PROGRESS_COMPLETE) {
            beginSplashDelay()
        } else {
            if (progress % DECIMAL_NUMBER == 0) {
                toastMessage.postValue(resourcesProvider.getString(R.string.extracting_progress) + progress + "%")
            }
        }
    }
}
