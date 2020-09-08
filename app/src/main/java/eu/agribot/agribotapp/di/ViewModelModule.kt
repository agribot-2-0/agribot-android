package eu.agribot.agribotapp.di

import android.content.Context
import eu.agribot.agribotapp.ui.activity.authentication.login.LoginViewModel
import eu.agribot.agribotapp.ui.activity.authentication.passwordreminder.PasswordReminderViewModel
import eu.agribot.agribotapp.ui.activity.authentication.signup.SignupViewModel
import eu.agribot.agribotapp.ui.activity.authentication.welcome.WelcomeViewModel
import eu.agribot.agribotapp.ui.activity.main.MainViewModel
import eu.agribot.agribotapp.ui.activity.splash.SplashViewModel
import eu.agribot.agribotapp.ui.fragment.autonomousmode.AutonomousModeViewModel
import eu.agribot.agribotapp.ui.fragment.bluetoothconnection.BluetoothConnectionViewModel
import eu.agribot.agribotapp.ui.fragment.bluetoothconnection.recycler.BluetoothNearbyListItemViewModel
import eu.agribot.agribotapp.ui.fragment.manualcontrol.ManualControlViewModel
import eu.agribot.agribotapp.ui.fragment.pathmanagement.PathManagementViewModel
import eu.agribot.agribotapp.ui.fragment.settings.SettingsViewModel
import eu.agribot.agribotapp.ui.fragment.toolcontrol.ToolControlViewModel
import eu.agribot.agribotapp.ui.fragment.toolmanagement.ToolManagementViewModel
import eu.agribot.agribotapp.ui.fragment.ttcontrol.TTControlViewModel
import eu.agribot.agribotapp.ui.fragment.weather.WeatherViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module

/**
 * Koin module storing definitions
 * For all ViewModels
 */

/**
 * Koin module storing definitions
 * For all ViewModels
 */
val viewModelModule = module {

    viewModel {
        BluetoothConnectionViewModel(
            bluetoothConnectionService = get(), manualModeService = get()
        )
    }
    viewModel {
        SplashViewModel(
            authenticationService = get(),
            bluetoothConnectionService = get()
        )
    }
    viewModel {
        MainViewModel(
            bluetoothConnectionService = get()
        )
    }
    viewModel {
        ManualControlViewModel(
            remoteControlService = get(),
            manualModeService = get()
        )
    }
    viewModel { (context: Context) ->
        PasswordReminderViewModel(
            authenticationService = get(),
            resourcesProvider = get { parametersOf(context) })
    }
    viewModel { WelcomeViewModel() }

    viewModel { (context: Context) ->
        LoginViewModel(
            authenticationService = get(),
            resourcesProvider = get { parametersOf(context) }
        )
    }
    viewModel { (context: Context) ->
        SignupViewModel(
            authenticationService = get(),
            resourcesProvider = get { parametersOf(context) })
    }
    viewModel {
        ToolControlViewModel(

        )
    }
    viewModel { AutonomousModeViewModel() }
    viewModel { ToolManagementViewModel() }
    viewModel {
        PathManagementViewModel(
            pathManagementService = get()
        )
    }
    viewModel { WeatherViewModel() }
    viewModel { (context: Context) ->
        SettingsViewModel(
            authenticationService = get(),
            resourcesProvider = get { parametersOf(context) })
    }
    viewModel {
        BluetoothNearbyListItemViewModel(
            bluetoothName = get(),
            bluetoothAddress = get()
        )
    }

    viewModel {
        TTControlViewModel(ttControlService = get())
    }
}
