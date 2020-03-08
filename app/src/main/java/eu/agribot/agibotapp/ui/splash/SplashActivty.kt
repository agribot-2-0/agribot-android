package eu.agribot.agibotapp.ui.splash

import android.os.Bundle
import eu.agribot.agibotapp.R
import eu.agribot.agibotapp.base.BaseActivity
import eu.agribot.agibotapp.common.navigation.NavigationEvent
import eu.agribot.agibotapp.common.navigation.events.MainActivityNavigationEvent
import eu.agribot.agibotapp.databinding.ActivitySplashBinding
import eu.agribot.agibotapp.ui.main.MainActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class SplashActivty : BaseActivity<SplashViewModel, ActivitySplashBinding>() {

    override val layoutResourceId: Int = R.layout.activity_splash
    override val viewModel: SplashViewModel by viewModel()

    override fun bindViewVariables(viewBinding: ActivitySplashBinding) {
        super.bindViewVariables(viewBinding)
        viewBinding.viewModel = viewModel
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.beginSplashScreenDelay()
    }

    override fun handleNavigationEvent(navigationEvent: NavigationEvent) = when (navigationEvent) {
        is MainActivityNavigationEvent -> handleMainActivityNavigationEvent()
        else -> Unit
    }

    private fun handleMainActivityNavigationEvent() {
        this.finish()
        startActivity(MainActivity.getStartingIntent(this))
    }
}
