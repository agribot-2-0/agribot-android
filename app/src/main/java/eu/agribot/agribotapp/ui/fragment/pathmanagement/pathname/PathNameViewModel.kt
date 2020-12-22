package eu.agribot.agribotapp.ui.fragment.pathmanagement.pathname

import androidx.lifecycle.MutableLiveData
import eu.agribot.agribotapp.R
import eu.agribot.agribotapp.base.BaseViewModel
import eu.agribot.agribotapp.common.livedata.MutableLiveEvent
import eu.agribot.agribotapp.common.navigation.events.NewPathNavigationEvent
import eu.agribot.agribotapp.common.resources.ResourcesProvider
import eu.agribot.agribotapp.domain.service.PathManagementService

class PathNameViewModel(
    private val pathManagementService: PathManagementService,
    private val resourcesProvider: ResourcesProvider
) : BaseViewModel() {
    private val pathName: MutableLiveData<String> = MutableLiveData()
    val toastMessageEvent: MutableLiveEvent<String> = MutableLiveEvent()

    fun pathNameChanged(text: CharSequence) {
        pathName.value = text.toString()
    }

    fun onNextButtonClicked() {
        if (pathName.value != null) {
            pathManagementService.updatePathName(pathName.value!!)
            navigationEvent.postValue(NewPathNavigationEvent())
        } else {
            toastMessageEvent.postValue(resourcesProvider.getString(R.string.blank_path_name_error))
        }
    }
}
