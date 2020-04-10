package eu.agribot.agribotapp.ui.fragment.settings

import eu.agribot.agribotapp.base.BaseViewModel
import eu.agribot.agribotapp.common.navigation.events.ChangeLanguageEvent

class SettingsViewModel : BaseViewModel(){

    fun onButtonChangeLanguage() {
        navigationEvent.postValue(
            ChangeLanguageEvent()
        )
    }
}


