package eu.agribot.agribotapp.ui.activity.splash

import android.os.Bundle
import eu.agribot.agribotapp.R
import eu.agribot.agribotapp.base.BaseActivity
import eu.agribot.agribotapp.common.dialog.DialogManager
import eu.agribot.agribotapp.common.navigation.NavigationEvent
import eu.agribot.agribotapp.common.navigation.events.MainActivityNavigationEvent
import eu.agribot.agribotapp.common.navigation.events.WelcomeActivityNavigationEvent
import eu.agribot.agribotapp.databinding.ActivitySplashBinding
import eu.agribot.agribotapp.ui.activity.authentication.welcome.WelcomeActivity
import eu.agribot.agribotapp.ui.activity.main.MainActivity
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class SplashActivty : BaseActivity<SplashViewModel, ActivitySplashBinding>() {

    override val layoutResourceId: Int = R.layout.activity_splash
    override val viewModel: SplashViewModel by viewModel()
    private val dialogManager: DialogManager by inject { parametersOf(this) }

    override fun bindViewVariables(viewBinding: ActivitySplashBinding) {
        viewBinding.viewModel = viewModel
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.handleCurrentBluetoothStatus()
        viewModel.postDialogEvent.observe(this::showBluetoothDialog)
    }

    override fun handleNavigationEvent(navigationEvent: NavigationEvent) = when (navigationEvent) {
        is MainActivityNavigationEvent -> handleMainActivityNavigationEvent()
        is WelcomeActivityNavigationEvent -> handleWelcomeActivityNavigationEvent()
        else -> Unit
    }

    private fun handleWelcomeActivityNavigationEvent() {
        this.finish()
        startActivity(WelcomeActivity.getStartingIntent(this))
    }

    private fun handleMainActivityNavigationEvent() {
        this.finish()
        startActivity(MainActivity.getStartingIntent(this))
    }

    private fun showBluetoothDialog(showDiaglog: Boolean) {
        if (showDiaglog)
            dialogManager.showBluetoothDialog { viewModel.enableBluetooth() }
    }
}
