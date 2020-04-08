package eu.agribot.agribotapp.ui.fragment.settings

import eu.agribot.agribotapp.base.BaseViewModel
import eu.agribot.agribotapp.common.navigation.events.ChangeLanguageEnglishEvent
import eu.agribot.agribotapp.common.navigation.events.ChangeLanguagePolishEvent

class SettingsViewModel : BaseViewModel(){

    fun onButtonPolishClicked() {
        navigationEvent.postValue(
           ChangeLanguagePolishEvent()
        )
    }

    fun onButtonEnglishClicked() {
        navigationEvent.postValue(
            ChangeLanguageEnglishEvent()
        )
    }

}


