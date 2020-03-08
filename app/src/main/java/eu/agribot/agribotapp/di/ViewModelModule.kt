package eu.agribot.agribotapp.di

import eu.agribot.agribotapp.ui.activity.main.MainViewModel
import eu.agribot.agribotapp.ui.activity.splash.SplashViewModel
import eu.agribot.agribotapp.ui.fragment.autonomousmode.AutonomousModeViewModel
import eu.agribot.agribotapp.ui.fragment.manualcontrol.ManualControlViewModel
import eu.agribot.agribotapp.ui.fragment.pathmanagement.PathManagementViewModel
import eu.agribot.agribotapp.ui.fragment.settings.SettingsViewModel
import eu.agribot.agribotapp.ui.fragment.toolmanagement.ToolManagementViewModel
import eu.agribot.agribotapp.ui.fragment.weather.WeatherViewModel
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
    viewModel { AutonomousModeViewModel() }
    viewModel { ToolManagementViewModel() }
    viewModel { PathManagementViewModel() }
    viewModel { WeatherViewModel() }
    viewModel { SettingsViewModel() }
}
