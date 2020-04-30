package eu.agribot.agribotapp.ui.activity.authentication.welcome

import android.content.Context
import android.content.Intent
import eu.agribot.agribotapp.base.BaseActivity
import eu.agribot.agribotapp.common.navigation.NavigationEvent
import eu.agribot.agribotapp.common.navigation.events.LoginActivityNavigationEvent
import eu.agribot.agribotapp.common.navigation.events.SignupActivityNavigationEvent
import eu.agribot.agribotapp.databinding.ActivityWelcomeBinding
import eu.agribot.agribotapp.ui.activity.authentication.login.LoginActivity
import eu.agribot.agribotapp.ui.activity.authentication.signup.SignupActivity
import org.koin.androidx.viewmodel.ext.android.viewModel
import eu.agribot.agribotapp.R

class WelcomeActivity : BaseActivity<WelcomeViewModel, ActivityWelcomeBinding>() {
    companion object {
        fun getStartingIntent(context: Context): Intent {
            return Intent(context, WelcomeActivity::class.java)
        }
    }

    override val layoutResourceId: Int = R.layout.activity_welcome
    override val viewModel: WelcomeViewModel by viewModel()

    override fun bindViewVariables(viewBinding: ActivityWelcomeBinding) {
        viewBinding.viewModel = viewModel
    }

    override fun handleNavigationEvent(navigationEvent: NavigationEvent) = when (navigationEvent) {
        is LoginActivityNavigationEvent -> handleLoginActivityNavigationEvent()
        is SignupActivityNavigationEvent -> handleSignupActivityNavigationEvent()
        else -> Unit
    }

    private fun handleLoginActivityNavigationEvent() {
        this.finish()
        startActivity(LoginActivity.getStartingIntent(this))
    }

    private fun handleSignupActivityNavigationEvent() {
        this.finish()
        startActivity(SignupActivity.getStartingIntent(this))
    }
}
