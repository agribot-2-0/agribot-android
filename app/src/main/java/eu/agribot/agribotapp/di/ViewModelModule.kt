package eu.agribot.agribotapp.di

import eu.agribot.agribotapp.ui.activity.authentication.login.LoginViewModel
import eu.agribot.agribotapp.ui.activity.authentication.passwordreminder.PasswordReminderViewModel
import eu.agribot.agribotapp.ui.activity.authentication.signup.SignupViewModel
import eu.agribot.agribotapp.ui.activity.authentication.welcome.WelcomeViewModel
import eu.agribot.agribotapp.ui.activity.main.MainViewModel
import eu.agribot.agribotapp.ui.activity.splash.SplashViewModel
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

    viewModel {
        SplashViewModel(
            authenticationService = get()
        )
    }
    viewModel { MainViewModel() }
    viewModel {
        ManualControlViewModel(
            remoteControlService = get()
        )
    }
    viewModel {
        PasswordReminderViewModel(
            authenticationService = get()
        )
    }

    viewModel { WelcomeViewModel() }

    viewModel {
        LoginViewModel(
            authenticationService = get()
        )
    }
    viewModel {
        SignupViewModel(
            authenticationService = get()
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
    viewModel {
        SettingsViewModel(
            authenticationService = get()
        )
    }
}
