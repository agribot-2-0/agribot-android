package eu.agribot.agribotapp.ui.fragment.toolmanagement.findtag

import eu.agribot.agribotapp.base.BaseViewModel
import eu.agribot.agribotapp.common.navigation.events.ShotToolPhotoNavigationEvent

class FindToolTagViewModel : BaseViewModel()  {

    fun onNextButtonClicked(){
        navigationEvent.postValue(ShotToolPhotoNavigationEvent())
    }
}