package eu.agribot.agribotapp.di

import eu.agribot.agribotapp.ui.activity.main.MainViewModel
import eu.agribot.agribotapp.ui.activity.splash.SplashViewModel
import eu.agribot.agribotapp.ui.fragment.manualcontrol.ManualControlViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * Koin module storing definitions
 * For all ViewModels
 */
val viewModelModule = module {

    viewModel { SplashViewModel() }
    viewModel { MainViewModel() }
    viewModel { ManualControlViewModel() }
}
