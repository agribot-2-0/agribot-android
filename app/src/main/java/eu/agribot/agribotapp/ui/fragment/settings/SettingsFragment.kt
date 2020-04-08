package eu.agribot.agribotapp.ui.fragment.settings

import android.app.Activity
import android.content.Intent
import android.content.res.Configuration
import eu.agribot.agribotapp.R
import eu.agribot.agribotapp.base.BaseFragment
import eu.agribot.agribotapp.common.navigation.NavigationEvent
import eu.agribot.agribotapp.common.navigation.events.ChangeLanguageEnglishEvent
import eu.agribot.agribotapp.common.navigation.events.ChangeLanguagePolishEvent
import eu.agribot.agribotapp.databinding.FragmentSettingsBinding
import eu.agribot.agribotapp.ui.activity.main.MainActivity
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*


class SettingsFragment : BaseFragment<SettingsViewModel, FragmentSettingsBinding>() {

    override val layoutResourceId: Int = R.layout.fragment_settings
    override val viewModel: SettingsViewModel by viewModel()


    override fun bindViewVariables(viewBinding: FragmentSettingsBinding) {
        viewBinding.viewModel = viewModel
    }

    override fun handleNavigationEvent(navigationEvent: NavigationEvent) = when (navigationEvent) {
        is ChangeLanguagePolishEvent  -> selectPolishLanguage()
        is ChangeLanguageEnglishEvent -> selectEnglishLanguage()
        else -> Unit
    }
   fun selectEnglishLanguage(){
   }

   fun selectPolishLanguage(){
   }

    // TODO add change language

}


