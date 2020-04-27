package eu.agribot.agribotapp.di

import eu.agribot.agribotapp.domain.service.LoginService
import eu.agribot.agribotapp.ui.activity.main.MainViewModel
import eu.agribot.agribotapp.ui.activity.splash.SplashViewModel
import eu.agribot.agribotapp.ui.fragment.authentication.login.LoginViewModel
import eu.agribot.agribotapp.ui.fragment.authentication.signup.SignupViewModel
import eu.agribot.agribotapp.ui.fragment.autonomousmode.AutonomousModeViewModel
import eu.agribot.agribotapp.ui.fragment.manualcontrol.ManualControlViewModel
import eu.agribot.agribotapp.ui.fragment.pathmanagement.PathManagementViewModel
import eu.agribot.agribotapp.ui.fragment.settings.SettingsViewModel
import eu.agribot.agribotapp.ui.fragment.toolcontrol.ToolControlViewModel
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
    viewModel {
        ManualControlViewModel(
            remoteControlService = get()
        )
    }
    viewModel { ToolControlViewModel() }
    viewModel { AutonomousModeViewModel() }
    viewModel { ToolManagementViewModel() }
    viewModel {
        PathManagementViewModel(
            pathManagementService = get()
        )
    }
    viewModel { WeatherViewModel() }
    viewModel { SettingsViewModel() }
    viewModel { LoginViewModel(
        loginService = get()
    ) }
    viewModel { SignupViewModel(
        signupService = get()
    ) }
}
