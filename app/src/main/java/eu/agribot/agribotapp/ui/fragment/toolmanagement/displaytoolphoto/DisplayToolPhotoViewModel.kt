package eu.agribot.agribotapp.ui.fragment.toolmanagement.displaytoolphoto

import eu.agribot.agribotapp.base.BaseViewModel
import eu.agribot.agribotapp.common.navigation.events.ShotToolPhotoNavigationEvent
import eu.agribot.agribotapp.common.navigation.events.ToolNavigationEvent
import eu.agribot.agribotapp.domain.service.cloud.ToolDataManager

class DisplayToolPhotoViewModel(private val toolDataManager: ToolDataManager) : BaseViewModel() {

    fun getToolPhotoName() : String{
        return "/" + toolDataManager.toolName + ".jpg"
    }

    fun onPhotoRepeatButtonClicked(){
        navigationEvent.postValue(ShotToolPhotoNavigationEvent())
    }

    fun onNextButtonClicked(){
        navigationEvent.postValue(ToolNavigationEvent())
    }
}