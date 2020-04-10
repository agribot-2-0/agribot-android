package eu.agribot.agribotapp.ui.fragment.settings

import eu.agribot.agribotapp.R
import eu.agribot.agribotapp.base.BaseFragment
import eu.agribot.agribotapp.common.navigation.NavigationEvent
import eu.agribot.agribotapp.common.navigation.events.ChangeLanguageEvent
import eu.agribot.agribotapp.databinding.FragmentSettingsBinding
import org.koin.androidx.viewmodel.ext.android.viewModel


class SettingsFragment : BaseFragment<SettingsViewModel, FragmentSettingsBinding>() {

    override val layoutResourceId: Int = R.layout.fragment_settings
    override val viewModel: SettingsViewModel by viewModel()


    override fun bindViewVariables(viewBinding: FragmentSettingsBinding) {
        viewBinding.viewModel = viewModel
    }

    override fun handleNavigationEvent(navigationEvent: NavigationEvent) = when (navigationEvent) {
        is ChangeLanguageEvent -> selectEnglishLanguage()
        else -> Unit
    }

   fun selectEnglishLanguage(){
   }

   fun selectPolishLanguage(){
   }

    // TODO add change language

}


