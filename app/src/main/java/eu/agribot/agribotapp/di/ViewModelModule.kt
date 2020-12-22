package eu.agribot.agribotapp.di

import android.content.Context
import eu.agribot.agribotapp.ui.activity.authentication.login.LoginViewModel
import eu.agribot.agribotapp.ui.activity.authentication.passwordreminder.PasswordReminderViewModel
import eu.agribot.agribotapp.ui.activity.authentication.signup.SignupViewModel
import eu.agribot.agribotapp.ui.activity.authentication.welcome.WelcomeViewModel
import eu.agribot.agribotapp.ui.activity.main.MainViewModel
import eu.agribot.agribotapp.ui.activity.splash.SplashViewModel
import eu.agribot.agribotapp.ui.fragment.bluetoothconnection.BluetoothConnectionViewModel
import eu.agribot.agribotapp.ui.fragment.bluetoothconnection.recycler.BluetoothNearbyListItemViewModel
import eu.agribot.agribotapp.ui.fragment.controlPanel.ControlPanelViewModel
import eu.agribot.agribotapp.ui.fragment.controlPanel.taskselect.TaskSelectViewModel
import eu.agribot.agribotapp.ui.fragment.manualcontrol.ManualControlViewModel
import eu.agribot.agribotapp.ui.fragment.pathmanagement.pathname.PathNameViewModel
import eu.agribot.agribotapp.ui.fragment.pathmanagement.mainpathscreen.PathManagementViewModel
import eu.agribot.agribotapp.ui.fragment.pathmanagement.newpath.NewPathViewModel
import eu.agribot.agribotapp.ui.fragment.settings.SettingsViewModel
import eu.agribot.agribotapp.ui.fragment.taskcreator.mainscreen.TaskCreatorViewModel
import eu.agribot.agribotapp.ui.fragment.taskcreator.mainscreen.recycler.TaskListItemViewModel
import eu.agribot.agribotapp.ui.fragment.taskcreator.pathselect.CreatorPathSelectViewModel
import eu.agribot.agribotapp.ui.fragment.taskcreator.taskname.CreatorTaskNameViewModel
import eu.agribot.agribotapp.ui.fragment.taskcreator.timeselect.CreatorTimeSelectViewModel
import eu.agribot.agribotapp.ui.fragment.taskcreator.toolselect.CreatorToolSelectViewModel
import eu.agribot.agribotapp.ui.fragment.taskcreator.weatherselect.CreatorWeatherSelectViewModel
import eu.agribot.agribotapp.ui.fragment.taskcreator.weatherselect.recycler.WeatherSettingsListItemViewModel
import eu.agribot.agribotapp.ui.fragment.toolcontrol.ToolControlViewModel
import eu.agribot.agribotapp.ui.fragment.toolcontrol.recycler.HitchListItemViewModel
import eu.agribot.agribotapp.ui.fragment.toolmanagement.mainscreen.ToolManagementViewModel
import eu.agribot.agribotapp.ui.fragment.toolmanagement.mainscreen.recycler.ToolListItemViewModel
import eu.agribot.agribotapp.ui.fragment.toolmanagement.toolname.ToolNameViewModel
import eu.agribot.agribotapp.ui.fragment.toolmanagement.toolparams.ToolParamsViewModel
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

    viewModel { (context: Context) ->
        BluetoothConnectionViewModel(
            bluetoothConnectionService = get(),
            resourcesProvider = get { parametersOf(context) },
            permissionManager = get(),
            manualModeService = get()
        )
    }
    viewModel {
        SplashViewModel(
            authenticationService = get(),
            bluetoothConnectionService = get(),
            cloudDatabaseService = get()
        )
    }
    viewModel {
        MainViewModel(
            bluetoothConnectionService = get()
        )
    }
    viewModel { (context: Context) ->
        ManualControlViewModel(
            manualModeService = get(),
            resourcesProvider = get { parametersOf(context) }
        )
    }
    viewModel { (context: Context) ->
        PasswordReminderViewModel(
            authenticationService = get(),
            resourcesProvider = get { parametersOf(context) }
        )
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
            resourcesProvider = get { parametersOf(context) }
        )
    }
    viewModel {
        ToolControlViewModel(
            manualModeService = get()
        )
    }

    viewModel {
        PathManagementViewModel(
            pathManagementService = get(),
            cloudStorageService = get()
        )
    }
    viewModel {
        WeatherViewModel(
            cloudDatabaseService = get()
        )
    }

    viewModel { (context: Context) ->
        SettingsViewModel(
            authenticationService = get(),
            resourcesProvider = get { parametersOf(context) },
            bluetoothConnectionService = get()
        )
    }
    viewModel {
        BluetoothNearbyListItemViewModel(
            bluetoothName = get(),
            bluetoothAddress = get()
        )
    }

    viewModel {
        HitchListItemViewModel(
            toolName = get(),
            switchNumber = get()
        )
    }

    viewModel {
        TTControlViewModel(
            ttControlService = get()
        )
    }

    viewModel {
        TaskListItemViewModel(
            taskName = get(),
            pathName = get()
        )
    }

    viewModel { (context: Context) ->
        CreatorPathSelectViewModel(
            cloudStorageService = get(),
            taskCreatorDataManager = get(),
            resourcesProvider = get { parametersOf(context) },
            kmlLayerService = get(),
            filePathProvider = get()

        )
    }

    viewModel { (context: Context) ->
        ControlPanelViewModel(
            cloudDatabaseService = get(),
            resourcesProvider = get { parametersOf(context) },
            cloudStorageService = get(),
            kmlLayerService = get(),
            filePathProvider = get()

        )
    }

    viewModel {
        CreatorTimeSelectViewModel(
            taskCreatorDataManager = get()
        )
    }

    viewModel { (context: Context) ->
        CreatorToolSelectViewModel(
            taskCreatorDataManager = get(),
            cloudDatabaseService = get(),
            resourcesProvider = get { parametersOf(context) }
        )
    }

    viewModel { (context: Context) ->
        CreatorWeatherSelectViewModel(
            taskCreatorDataManager = get(),
            cloudDatabaseService = get(),
            resourcesProvider = get { parametersOf(context) }
        )
    }

    viewModel {
        WeatherSettingsListItemViewModel(
            id = get(),
            conditionArray = get(),
            currentValue = get(),
            minimumPickerValue = get(),
            maximumPickerValue = get(),
            isSwitchActive = get(),
            settedValue = get(),
            presetCondition = get()
        )
    }

    viewModel { (context: Context) ->
        TaskCreatorViewModel(
            resourcesProvider = get { parametersOf(context) },
            cloudDatabaseService = get(),
            taskCreatorDataManager = get(),
            cloudStorageService = get(),
            kmlLayerService = get(),
            filePathProvider = get()
        )
    }

    viewModel { TaskSelectViewModel(cloudDatabaseService = get()) }

    viewModel { ToolListItemViewModel(toolName = get()) }

    viewModel { (context: Context) ->
        ToolManagementViewModel(
            resourcesProvider = get { parametersOf(context) },
            cloudDatabaseService = get(),
            toolDataManager = get()
        )
    }

    viewModel { (context: Context) ->
        ToolNameViewModel(
            toolDataManager = get(),
            resourcesProvider = get { parametersOf(context) }
        )
    }

    viewModel { (context: Context) ->
        ToolParamsViewModel(
            toolDataManager = get(),
            resourcesProvider = get { parametersOf(context) },
            cloudDatabaseService = get()
        )
    }

    viewModel { (context: Context) ->
        CreatorTaskNameViewModel(
            taskCreatorDataManager = get(),
            resourcesProvider = get { parametersOf(context) })
    }

    viewModel { (context: Context) ->
        NewPathViewModel(
            cloudDatabaseService = get(),
            pathManagementService = get(),
            filePathProvider = get(),
            resourcesProvider = get { parametersOf(context) }
        )
    }

    viewModel {
        PathNameViewModel()
    }
}
